package com.example.sweProject.repositories;

import com.example.sweProject.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByClientId(String clientId);

    @Query(value = "SELECT * FROM USERS WHERE CLIENT_ID = ?1 "
            + "AND ID = ?2", nativeQuery = true)
    Optional<User> findBySpecificUser(String clientId, Integer id);
}

