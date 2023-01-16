package com.driver.Converter;

import com.driver.DTO.StudentRequestDto;
import com.driver.models.Student;

public class StudentConverter {
    public static Student convertDtoToEntity(StudentRequestDto studentRequestDto) {
        Student student = Student.builder()
                .name(studentRequestDto.getName())
                .emailId(studentRequestDto.getEmailId())
                .age(studentRequestDto.getAge())
                .country(studentRequestDto.getCountry())
                .card(studentRequestDto.getCard())
                .build();
        return student;
    }
}
