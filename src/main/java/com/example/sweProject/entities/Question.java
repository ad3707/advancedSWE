package com.example.sweProject.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name="QUESTIONS")
public class Question {

  // Instance Variables

  @Id
  @GeneratedValue // id is automatically generated when new Question is created via POST
  private Integer id;

  @Column(name="NAME")
  private String name;

  @Column(name="A")
  private String a;

  @Column(name="B")
  private String b;

  @Column(name="C")
  private String c;

  @Column(name="D")
  private String d;

  @Column(name="ANSWER")
  private String answer;

  // Controllers

  public Question(){
  }

  public Question(Integer id, String name, String a, String b, String c, String d, String answer){
    this.id = id;
    this.name = name;
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.answer = answer;
  }

  // Getters

  public Integer getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getA() {
    return this.a;
  }
  public String getB() {
    return this.b;
  }
  public String getC() {
    return this.c;
  }
  public String getD() {
    return this.d;
  }

  public String getAnswer() {
    return this.answer;
  }

  // Setters

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setA(String a) {
    this.a = a;
  }

  public void setB(String b) {
    this.b = b;
  }

  public void setC(String c) {
    this.c = c;
  }

  public void setD(String d) {
    this.d = d;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }
}



