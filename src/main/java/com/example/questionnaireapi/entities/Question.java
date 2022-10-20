package com.example.questionnaireapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

import javax.persistence.GenerationType;

@Entity
@Table(name="QUESTIONS")
public class Question {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;

  @Column(name="NAME")
  private String name;

  @Column(name="CHOICES")
  private String[] choices;

  @Column(name="ANSWER")
  private String answer;

  public Integer getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String[] getChoices() {
    return this.choices;
  }

  public String getAnswer() {
    return this.answer;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setChoices(String[] choices) {
    this.choices = choices;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }
}



