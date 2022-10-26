package com.example.sweProject.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ATTEMPTED")
    private int attempted;

    @Column(name = "CORRECT")
    private int correct;

    @Column(name = "CLIENT_ID")
    private String clientId;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAttempted() {
        return this.attempted;
    }

    public int getCorrect() {
        return this.correct;
    }

    public double getPercentCorrect() {
        return ((double) this.getCorrect()) / ((double) this.getAttempted());
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttempted(int attempted) {
        this.attempted = attempted;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public String setClientId(String clientId) {
        return this.clientId = clientId;
    }

    public void incrementAttempted() {
        this.attempted++;
    }

    public void incrementCorrect() {
        this.correct++;
    }

    public User() {
        this.attempted = 0;
        this.correct = 0;
    }

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.attempted = 0;
        this.correct = 0;
    }

    public User(Integer id, String name, int attempted, int correct) {
        this.id = id;
        this.name = name;
        this.attempted = attempted;
        this.correct = correct;
    }
}
