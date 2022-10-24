package com.example.sweProject.controllers;

import java.io.ByteArrayOutputStream;
import java.util.*;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.sweProject.entities.Question;
import com.example.sweProject.repositories.QuestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
public class QuestionController{
    private final QuestionRepository questionRepository;

    public QuestionController(final QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping(value="/questions", produces="application/json")
    public @ResponseBody Iterable<Question> getAllQuestions(){
        Iterable<Question> questions = this.questionRepository.findAll();

        // List<Question> result = new ArrayList<Question>();
        // questions.forEach(result::add);
        // questions.forEach((Question item) -> System.out.println("HELLO:::" + item));

        // System.out.println("BUFFALO:::" + result.get(0));

        // ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // ByteArrayOutputStream out = new ByteArrayOutputStream();

        // try {
        //     objectMapper.writeValue(out, result);
        // } catch (Exception error){
        //     error.printStackTrace();
        // }

        // final byte[] data = out.toByteArray();

        // System.out.println("ALL QUESTIONS::: " + new String(data));
        
        return questions;
    }

    @GetMapping("/questions/{id}")
    public Optional<Question> getQuestionById(@PathVariable("id") Integer id){
        return this.questionRepository.findById(id);
    }

    @PostMapping(value="/questions", produces="application/json")
    public @ResponseBody Question createNewQuestion(@RequestBody Question question){
        // should handle empty request body, bad request body, and good request body
        System.out.println(question);
        Question newQuestion = this.questionRepository.save(question);
        return newQuestion;
    }
        // public ResponseEntity<Question> createNewQuestion (@RequestBody Question question){
    //     //code
    //     Question newQuestion = this.questionRepository.save(question);
    //     return new ResponseEntity<Question>(newQuestion, HttpStatus.CREATED);
    // }

    @PutMapping("/questions/{id}")
    public Question updateQuestion(@PathVariable("id") Integer id, @RequestBody Question p) {
        Optional<Question> questionToUpdateOptional = this.questionRepository.findById(id);
        if (!questionToUpdateOptional.isPresent()) {
            return null;
        }

        Question questionToUpdate = questionToUpdateOptional.get();

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

        Question updatedQuestion = this.questionRepository.save(questionToUpdate);
        return updatedQuestion;
    }

    @DeleteMapping("/questions/{id}")
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