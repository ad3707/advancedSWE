package com.example.sweProject.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "QUESTIONS")
public class Question {

    // Instance Variables/Table columns
    @Id
    @GeneratedValue // generates id when new Question is created
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "A")
    private String a;

    @Column(name = "B")
    private String b;

    @Column(name = "C")
    private String c;

    @Column(name = "D")
    private String d;

    @Column(name = "ANSWER")
    private String answer;

    @Column(name = "CLIENT_ID")
    private String clientId;

    // Constructors
    public Question() {
    }

    public Question(final Integer id, final String name, final String a,
                    final String b, final String c,
                    final String d, final String answer) {
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

    // Setters
    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getA() {
        return this.a;
    }

    public void setA(final String a) {
        this.a = a;
    }

    public String getB() {
        return this.b;
    }

    public void setB(final String b) {
        this.b = b;
    }

    public String getC() {
        return this.c;
    }

    public void setC(final String c) {
        this.c = c;
    }

    public String getD() {
        return this.d;
    }

    public void setD(final String d) {
        this.d = d;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(final String answer) {
        this.answer = answer;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }
}
