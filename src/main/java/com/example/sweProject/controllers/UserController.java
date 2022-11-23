package com.example.sweProject.controllers;

import com.example.sweProject.entities.Question;
import com.example.sweProject.entities.User;
import com.example.sweProject.repositories.QuestionRepository;
import com.example.sweProject.repositories.UserRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

@RestController
@CrossOrigin

public class UserController {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public UserController(final UserRepository userRepository,
                          final QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    // GET Mappings
    @GetMapping(value = "/users", produces = "application/json")
    public @ResponseBody Iterable<User> getAllUsers(
            final HttpServletRequest request) {
        String ipAddr = request.getRemoteAddr();
        Iterable<User> users = this.userRepository.findByClientId(ipAddr);
        return users;
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUserById(final @PathVariable("id") Integer id,
                                      final HttpServletRequest request) {
        String ipAddr = request.getRemoteAddr();
        return this.userRepository.findBySpecificUser(ipAddr, id);
    }

    // POST Mappings
    @PostMapping(value = "/users", produces = "application/json")
    public @ResponseBody User createNewUser(final @RequestBody User user,
                                            final HttpServletRequest request) {
        String ipAddr = request.getRemoteAddr();
        user.setClientId(ipAddr);

        User newUser = this.userRepository.save(user);
        return newUser;
    }

    // PUT Mappings
    @PutMapping(value = "/users/{id}") // produces="application/json")
    public User updateUser(final @PathVariable("id") Integer id,
                           final @RequestBody User updatedUser,
                           final HttpServletRequest request) {
        String ipAddr = request.getRemoteAddr();
        Optional<User> userToUpdateOptional =
                this.userRepository.findBySpecificUser(ipAddr, id);

        if (!userToUpdateOptional.isPresent()) {
            return null;
        }

        User userToUpdate = userToUpdateOptional.get();

        if (updatedUser.getName() != null) {
            userToUpdate.setName(updatedUser.getName());
        }
        if (updatedUser.getAttempted() != -1) {
            userToUpdate.setAttempted(updatedUser.getAttempted());
        }
        if (updatedUser.getCorrect() != -1) {
            userToUpdate.setCorrect(updatedUser.getCorrect());
        }

        this.userRepository.save(userToUpdate);

        return userToUpdate;
    }

    // DELETE Mappings
    @DeleteMapping("/users/{id}")
    public User deleteUser(final @PathVariable("id") Integer id,
                           final HttpServletRequest request) {
        String ipAddr = request.getRemoteAddr();
        Optional<User> userToDeleteOptional =
                this.userRepository.findBySpecificUser(ipAddr, id);
        if (!userToDeleteOptional.isPresent()) {
            return null;
        }
        User userToDelete = userToDeleteOptional.get();
        this.userRepository.delete(userToDelete);
        return userToDelete;
    }

    @PutMapping("/users/{userid}/answer/{questionid}")
    public User updateUserLeaderboard(
            final @PathVariable("userid") Integer userid,
            final @PathVariable("questionid") Integer questionid,
            final @RequestBody String choice,
            final HttpServletRequest request) {

        String ipAddr = request.getRemoteAddr();
        // See if user exists

        Optional<User> userToUpdateOptional =
                this.userRepository.findBySpecificUser(ipAddr, userid);
        if (!userToUpdateOptional.isPresent()) {
            System.out.println("user doesnt exist");
            return null;
        }

        // See if question exists
        Optional<Question> questionToUpdateOptional =
                this.questionRepository.findBySpecificQuestion(ipAddr,
                        questionid);
        if (!questionToUpdateOptional.isPresent()) {
            System.out.println("question doesnt exist");
            return null;
        }

        // See if the choice is valid
        if (choice.length() != 1
                || !(choice.charAt(0) >= 'A' && choice.charAt(0) <= 'D'
                ||
                choice.charAt(0) >= 'a' && choice.charAt(0) <= 'd')) {
            System.out.println("content length/answer is not correct");
            return null;
        }

        // At this point, a valid input is guaranteed

        User userToUpdate = userToUpdateOptional.get();
        Question questionAttempted = questionToUpdateOptional.get();

        userToUpdate.incrementAttempted();
        if (choice.toLowerCase()
                .equals(questionAttempted.getAnswer().toLowerCase())) {
            userToUpdate.incrementCorrect();
        }

        this.userRepository.save(userToUpdate);
        return userToUpdate;
    }

    // Get top k users
    @GetMapping("/leaderboard/{k}")
    public List<User> getTopKUsers(final @PathVariable("k") Integer k,
                                   final HttpServletRequest request) {

        if (k == null) {
            return null;
        }

        PriorityQueue<User> pq = new PriorityQueue<>(
                (a, b) -> Double.compare(a.getPercentCorrect(),
                        b.getPercentCorrect()));

        for (User user : this.getAllUsers(request)) {
            pq.add(user);

            // Remove bottom tier users if size is greater than k
            if (pq.size() > k) {
                pq.poll();
            }

        }

        // Convert to LinkedList
        List<User> out = new LinkedList<>();
        while (!pq.isEmpty()) {
            out.add(0, pq.poll());
        }

        return out;
    }
}
