package com.example.sweProject.controllers;

import java.util.*;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestParam;

import com.example.sweProject.entities.Question;
import com.example.sweProject.repositories.QuestionRepository;

import antlr.debug.NewLineListener;

@RestController
public class QuestionController{
    private final QuestionRepository questionRepository;

    public QuestionController(final QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping("/helloworld")
    public String helloWorld() {
        return "Hello World!";
    }

    @GetMapping("/questions")
    public Iterable<Question> getAllQuestions() {
        return this.questionRepository.findAll();
    }

    @GetMapping("/questions/{id}")
    public Optional<Question> getQuestionById(@PathVariable("id") Integer id){
        return this.questionRepository.findById(id);
    }

    @PostMapping("/questions")
    public Question createNewQuestion(@RequestBody Question question){
        Question newQuestion = this.questionRepository.save(question);
        return newQuestion;
    }

    @PutMapping("/update/{id}")
    public Question updateQuestion(@PathVariable("id") Integer id, @RequestBody Question p) {
        Optional<Question> questionToUpdateOptional = this.questionRepository.findById(id);
        if (!questionToUpdateOptional.isPresent()) {
            return null;
        }

        Question questionToUpdate = questionToUpdateOptional.get();
        if (p.getName() != null) {
            questionToUpdate.setName(p.getName());
        }
        // if (p.getChoices() != null) {
        //     questionToUpdate.setChoices(p.getChoices());
        // }
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

        Question updatedQuestion = this.questionRepository.save(questionToUpdate);
        return updatedQuestion;
    }

    @DeleteMapping("/delete/{id}")
    public Question deleteQuestion(@PathVariable("id") Integer id) {
        Optional<Question> questionToDeleteOptional = this.questionRepository.findById(id);
        if (!questionToDeleteOptional.isPresent()) {
            return null;
        }
        Question questionToDelete = questionToDeleteOptional.get();
        this.questionRepository.delete(questionToDelete);
        return questionToDelete;
    }
}