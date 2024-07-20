package com.batch.persistence.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "persons")
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "last_name")
    private String lastName;

    private int age;

    @Column(name = "insertion_date")
    private String insertionDate;
}
