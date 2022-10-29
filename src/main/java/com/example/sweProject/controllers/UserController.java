package com.example.sweProject.controllers;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.sweProject.entities.User;
import com.example.sweProject.entities.Question;
import com.example.sweProject.repositories.UserRepository;
import com.example.sweProject.repositories.QuestionRepository;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public UserController(final UserRepository userRepository, final QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    // GET Mappings
    @GetMapping(value = "/users", produces = "application/json")
    public @ResponseBody Iterable<User> getAllUsers(HttpServletRequest request) {
        String ipAddr = request.getRemoteAddr();
        Iterable<User> users = this.userRepository.findByClientId(ipAddr);
        return users;
    }

    // // GET Mappings

    // @GetMapping(value = "/users", produces = "application/json")
    // public @ResponseBody Iterable<User> getAllUsers() {
    // Iterable<User> users = this.userRepository.findAll();
    // return users;
    // }

    @GetMapping("/users/{id}")
    public Optional<User> getUserById(@PathVariable("id") Integer id) {
        return this.userRepository.findById(id);
    }

    // POST Mappings
    @PostMapping(value = "/users", produces = "application/json")
    public @ResponseBody User createNewUser(@RequestBody User user, HttpServletRequest request) {
        String ipAddr = request.getRemoteAddr();
        user.setClientId(ipAddr);

        // should handle empty request body, bad request body, and good request body
        User newUser = this.userRepository.save(user);
        return newUser;
    }

    // PUT Mappings
    @PutMapping(value = "/users/{id}") // produces="application/json")
    public User updateUser(@PathVariable("id") Integer id, @RequestBody User updatedUser, HttpServletRequest request) {
        String ipAddr = request.getRemoteAddr();
        Optional<User> userToUpdateOptional = this.userRepository.findBySpecificUser(ipAddr, id);

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
    public User deleteUser(@PathVariable("id") Integer id, HttpServletRequest request) {
        String ipAddr = request.getRemoteAddr();
        Optional<User> userToDeleteOptional = this.userRepository.findBySpecificUser(ipAddr, id);
        if (!userToDeleteOptional.isPresent()) {
            return null;
        }
        User userToDelete = userToDeleteOptional.get();
        this.userRepository.delete(userToDelete);
        return userToDelete;
    }

    // Update user to the score they gave (api: THIS user answered THIS question
    // with THIS choice)
    //

    /*
     * @PutMapping("/users/{userid}/answer/{questionid}/{choice}")
     * public User updateUserLeaderboard(@PathVariable("userid") Integer userid,
     * 
     * @PathVariable("questionid") Integer questionid,
     * 
     * @PathVariable("choice") String choice, HttpServletRequest request) {
     */
    @PutMapping("/users/{userid}/answer/{questionid}")
    public User updateUserLeaderboard(@PathVariable("userid") Integer userid,
            @PathVariable("questionid") Integer questionid,
            @RequestBody String choice, HttpServletRequest request) {

        String ipAddr = request.getRemoteAddr();
        // See if user exists

        Optional<User> userToUpdateOptional = this.userRepository.findBySpecificUser(ipAddr, userid);
        if (!userToUpdateOptional.isPresent()) {
            System.out.println("user doesnt exist");
            return null;
        }

        // See if question exists
        Optional<Question> questionToUpdateOptional = this.questionRepository.findBySpecificQuestion(ipAddr,
                questionid);
        if (!questionToUpdateOptional.isPresent()) {
            System.out.println("question doesnt exist");
            return null;
        }

        // See if the choice is valid
        if (choice.length() != 1 || !(choice.charAt(0) >= 'A' && choice.charAt(0) <= 'D'
                || choice.charAt(0) >= 'a' && choice.charAt(0) <= 'd')) {
            System.out.println("content length/answer is not correct");
            return null;
        }
        System.out.println("ALLWORKED");

        // At this point, a valid input is guaranteed

        User userToUpdate = userToUpdateOptional.get();
        Question questionAttempted = questionToUpdateOptional.get();

        userToUpdate.incrementAttempted();
        if (choice.toLowerCase().equals(questionAttempted.getAnswer().toLowerCase())) {
            userToUpdate.incrementCorrect();
        }

        User outUser = this.userRepository.save(userToUpdate);
        return userToUpdate;
    }

    // Get top k users
    @GetMapping("/leaderboard/{k}")
    public List<User> getTopKUsers(@PathVariable("k") Integer k, HttpServletRequest request) {

        if (k == null) {
            return null;
        }

        PriorityQueue<User> pq = new PriorityQueue<>(
                (a, b) -> Double.compare(a.getPercentCorrect(), b.getPercentCorrect()));

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
