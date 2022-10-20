package com.example.questionnaireapi.repositories;
import org.springframework.data.repository.CrudRepository;
import com.example.questionnaireapi.entities.Question;

public interface QuestionRepository extends CrudRepository<Question, Integer> {
}