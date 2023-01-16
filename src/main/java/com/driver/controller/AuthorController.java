package com.driver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.driver.models.Author;
import com.driver.services.AuthorService;

//Add required annotations
@RestController
@RequestMapping("/author")
public class AuthorController {
    // Write createAuthor API with required annotations
    @Autowired
    AuthorService authorService;

    @PostMapping
    public ResponseEntity<String> createAuthor(@RequestBody Author author) {
        authorService.createAuthor(author);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

}
