package com.driver.controller;

import com.driver.DTO.StudentRequestDto;
import com.driver.services.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Add required annotations
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    // Add required annotations
    @GetMapping("/studentByEmail/")
    public ResponseEntity<String> getStudentByEmail(@RequestParam("email") String email) {
        studentService.getDetailsByEmail(email);
        return new ResponseEntity<>("Student details printed successfully ",
                HttpStatus.OK);
    }

    // Add required annotations
    @GetMapping("/studentById/ ")
    public ResponseEntity<String> getStudentById(@RequestParam("id") int id) {
        studentService.getDetailsById(id);
        return new ResponseEntity<>("Student details printed successfully ",
                HttpStatus.OK);
    }

    // Add required annotations
    @PostMapping
    public ResponseEntity<String> createStudent(@RequestBody StudentRequestDto studentRequestDto) {
        studentService.createStudent(studentRequestDto);
        return new ResponseEntity<>("the student is successfully added to the system", HttpStatus.CREATED);
    }

    // Add required annotations
    @PutMapping
    public ResponseEntity<String> updateStudent(@RequestBody StudentRequestDto studentRequestDto) {
        studentService.updateStudent(studentRequestDto);
        return new ResponseEntity<>("student is updated", HttpStatus.ACCEPTED);
    }

    // Add required annotations
    @DeleteMapping
    public ResponseEntity<String> deleteStudent(@RequestParam("id") int id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>("student is deleted", HttpStatus.ACCEPTED);
    }

}
