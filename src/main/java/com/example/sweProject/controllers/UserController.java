package com.example.sweProject.controllers;

import com.example.sweProject.entities.User;
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500/")

public class UserController {
    private final UserRepository userRepository;

    public UserController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET Mappings
    @GetMapping(value = "/users", produces = "application/json")
    public @ResponseBody Iterable<User> getAllUsers(
            final HttpServletRequest request) {
        String ipAddr = request.getRemoteAddr();
        return this.userRepository.findByClientId(ipAddr);
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

        return this.userRepository.save(user);
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

    // Get top k users
    @GetMapping("/leaderboard/{k}")
    public List<User> getTopKUsers(final @PathVariable("k") Integer k,
                                   final HttpServletRequest request) {

        if (k == null) {
            return Collections.emptyList();
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
