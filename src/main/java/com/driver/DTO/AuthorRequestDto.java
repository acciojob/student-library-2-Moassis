package com.driver.DTO;

import lombok.Data;

@Data
public class AuthorRequestDto {
    private String name;
    private int age;
    private String email;
    private String country;
}
