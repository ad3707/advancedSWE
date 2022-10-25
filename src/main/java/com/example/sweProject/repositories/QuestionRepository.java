package com.example.sweProject.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.sweProject.entities.Question;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Integer> {
    List<Question> findByClientId(String clientId);

    @Query(value = "SELECT * FROM QUESTIONS WHERE CLIENT_ID = ?1 AND ID = ?2", nativeQuery = true)
    Optional<Question> findBySpecificQuestion(String clientId, Integer id);
}
