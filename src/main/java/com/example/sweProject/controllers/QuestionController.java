package com.example.sweProject.controllers;

import com.example.sweProject.entities.Question;
import com.example.sweProject.repositories.QuestionRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@CrossOrigin
public class QuestionController {
    private final QuestionRepository questionRepository;

    public QuestionController(final QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // GET Mappings
    @GetMapping(value = "/questions", produces = "application/json")
    public @ResponseBody Iterable<Question> getAllQuestions(
            final HttpServletRequest request) {
        String ipAddr = request.getRemoteAddr();
        Iterable<Question> questions = this.questionRepository.findByClientId(ipAddr);
        return questions;
    }

    @GetMapping("/questions/{id}")
    public Optional<Question> getQuestionById(
            final @PathVariable("id") Integer id,
            final HttpServletRequest request) {
        String ipAddr = request.getRemoteAddr();
        return this.questionRepository.findBySpecificQuestion(ipAddr, id);
    }

    // POST Mappings
    @PostMapping(value = "/questions", produces = "application/json")
    public @ResponseBody Question createNewQuestion(
            final @RequestBody Question question,
            final HttpServletRequest request) {
        String ipAddr = request.getRemoteAddr();
        question.setClientId(ipAddr);

        Question newQuestion = this.questionRepository.save(question);
        return newQuestion;
    }

    // PUT Mappings
    @PutMapping("/questions/{id}")
    public Question updateQuestion(final @PathVariable("id") Integer id,
            final @RequestBody Question p,
            final HttpServletRequest request) {
        // check if question with {id} exists in database
        String ipAddr = request.getRemoteAddr();
        Optional<Question> questionToUpdateOptional = this.questionRepository.findBySpecificQuestion(ipAddr, id);

        // if question with {id} does not exist in database, return null
        if (!questionToUpdateOptional.isPresent()) {
            return null;
        }

        /*
         * if question with {id} exists
         * retrieve Question object from questionToUpdateOptional
         */
        Question questionToUpdate = questionToUpdateOptional.get();

        // update instance variables if not null
        if (p.getName() != null) {
            questionToUpdate.setName(p.getName());
        }
        if (p.getA() != null) {
            questionToUpdate.setA(p.getA());
        }
        if (p.getB() != null) {
            questionToUpdate.setB(p.getB());
        }
        if (p.getC() != null) {
            questionToUpdate.setC(p.getC());
        }
        if (p.getD() != null) {
            questionToUpdate.setD(p.getD());
        }
        if (p.getAnswer() != null) {
            questionToUpdate.setAnswer(p.getAnswer());
        }

        // saves update in database
        this.questionRepository.save(questionToUpdate);

        return questionToUpdate;
    }

    // DELETE Mappings
    @DeleteMapping("/questions/{id}")
    public Question deleteQuestion(final @PathVariable("id") Integer id,
            final HttpServletRequest request) {
        // check if question with {id} exists in database
        String ipAddr = request.getRemoteAddr();
        Optional<Question> questionToDeleteOptional = this.questionRepository.findBySpecificQuestion(ipAddr, id);

        // if {id} does not exist in database, return null
        if (!questionToDeleteOptional.isPresent()) {
            return null;
        }

        /*
         * if {id} exists
         * retrieve Question object from questionToDeleteOptional
         */
        Question questionToDelete = questionToDeleteOptional.get();
        this.questionRepository.delete(questionToDelete);
        return questionToDelete; // returns object that was deleted
    }
}
