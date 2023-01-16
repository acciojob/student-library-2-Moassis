package com.driver.Converter;

import com.driver.DTO.BookRequestDto;
import com.driver.models.Book;

public class BookConverter {
    public static Book convertDtoToEntity(BookRequestDto bookRequestDto) {
        Book book = Book.builder()
                .name(bookRequestDto.getName())
                .genre(bookRequestDto.getGenre())
                .author(bookRequestDto.getAuthor())
                .card(bookRequestDto.getCard())
                .build();
        return book;

    }
}
