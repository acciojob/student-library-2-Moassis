package com.driver.services;

import com.driver.Converter.AuthorConverter;
import com.driver.DTO.AuthorRequestDto;
import com.driver.models.Author;
import com.driver.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository1;

    public void createAuthor(AuthorRequestDto authorRequestDto) {
        Author author = AuthorConverter.convertDtoToEntity(authorRequestDto);
        authorRepository1.save(author);
    }
}
