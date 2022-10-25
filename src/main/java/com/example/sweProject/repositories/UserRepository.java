package com.example.sweProject.repositories;
import org.springframework.data.repository.CrudRepository;
import com.example.sweProject.entities.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}

