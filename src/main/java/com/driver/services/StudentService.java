package com.driver.services;

import com.driver.Converter.StudentConverter;
import com.driver.DTO.StudentRequestDto;
import com.driver.models.Card;
import com.driver.models.CardStatus;
import com.driver.models.Student;
import com.driver.repositories.CardRepository;
import com.driver.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    CardService cardService4;

    @Autowired
    StudentRepository studentRepository4;

    @Autowired
    CardRepository cardRepository3;

    public Student getDetailsByEmail(String email) {
        Student student = studentRepository4.findByEmailId(email);
        return student;
    }

    public Student getDetailsById(int id) {
        Student student = studentRepository4.findById(id).get();
        return student;
    }

    public void createStudent(StudentRequestDto studentRequestDto) {
        Student student = StudentConverter.convertDtoToEntity(studentRequestDto);
        int id = student.getId();
        Card card = new Card();
        card.setId(id);
        card.setCardStatus(CardStatus.ACTIVATED);
        cardRepository3.save(card);
        studentRepository4.save(student);
    }

    public void updateStudent(StudentRequestDto studentRequestDto) {
        Student student = StudentConverter.convertDtoToEntity(studentRequestDto);
        studentRepository4.updateStudentDetails(student);
    }

    public void deleteStudent(int id) {
        // Delete student and deactivate corresponding card
        studentRepository4.deleteCustom(id);
    }
}
