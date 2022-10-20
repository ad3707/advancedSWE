package com.example.sweProject.repositories;
import org.springframework.data.repository.CrudRepository;
import com.example.sweProject.entities.Question;

public interface QuestionRepository extends CrudRepository<Question, Integer> {
}