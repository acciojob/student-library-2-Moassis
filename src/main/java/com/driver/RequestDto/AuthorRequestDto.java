package com.driver.RequestDto;

import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;

import javax.persistence.Column;

public class AuthorRequestDto {
    private String name;
    private String country;
    private int age;
    @Column(unique = true)
    private String email;

    public AuthorRequestDto(String name, String country, int age, String email) {
        this.name = name;
        this.country = country;
        this.age = age;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
