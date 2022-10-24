
package com.example.sweProject.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

import javax.persistence.GenerationType;

@Entity
@Table(name="QUESTIONS")
public class User {
    @Id
    // @Column(name="ID")
    @GeneratedValue
    private Integer id;

    @Column(name="NAME")
    private String name;

    @Column(name="ATTEMPTED")
    private Integer attempted;

    @Column(name="CORRECT")
    private Integer correct;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getAttempted() {
        return this.attempted;
    }

    public Integer getCorrect() {
        return this.correct;
    }

    public double getPercentCorrect() {
        return ((double) this.getCorrect()) / ((double) this.getAttempted());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttempted(Integer attempted) {
        this.attempted = attempted;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public void incrementAttempted(){
        this.attempted++;
    }

    public void incrementCorrect(){
        this.correct++;
    }

    public User(){
  }

  public User(Integer id, String name, Integer attempted, Integer correct){
    this.id = id;
    this.name = name;
    this.attempted = attempted;
    this.correct = correct;
  }
}

