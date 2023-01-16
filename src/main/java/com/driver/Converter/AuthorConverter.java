package com.driver.Converter;

import com.driver.DTO.AuthorRequestDto;
import com.driver.models.Author;

public class AuthorConverter {

    public static Author convertDtoToEntity(AuthorRequestDto authorRequestDto) {
        Author author = Author.builder()
                .name(authorRequestDto.getName())
                .age(authorRequestDto.getAge())
                .email(authorRequestDto.getEmail())
                .country(authorRequestDto.getCountry())
                .build();

        return author;
    }
}
