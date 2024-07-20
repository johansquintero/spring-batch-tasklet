package com.batch.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private Long id;
    private String name;
    private String lastName;
    private int age;
    private String insertionDate;
}
