package com.example.sweProject.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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

    public User() {
        this.attempted = 0;
        this.correct = 0;
    }

    public User(final Integer id, final String name) {
        this.id = id;
        this.name = name;
        this.attempted = 0;
        this.correct = 0;
    }

    public User(final Integer id, final String name, final Integer attempted,
            final Integer correct) {
        this.id = id;
        this.name = name;
        this.attempted = attempted;
        this.correct = correct;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getAttempted() {
        return this.attempted;
    }

    public void setAttempted(final int attempted) {
        this.attempted = attempted;
    }

    public int getCorrect() {
        return this.correct;
    }

    public void setCorrect(final int correct) {
        this.correct = correct;
    }

    public double getPercentCorrect() {
        return ((double) this.getCorrect()) / ((double) this.getAttempted());
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public void incrementAttempted() {
        this.attempted++;
    }

    public void incrementCorrect() {
        this.correct++;
    }
}
