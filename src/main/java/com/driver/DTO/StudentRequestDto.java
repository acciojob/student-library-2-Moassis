package com.driver.DTO;

import com.driver.models.Card;

import lombok.Data;

@Data
public class StudentRequestDto {

    private String emailId;
    private String name;
    private int age;
    private String country;
    private Card card;
}
