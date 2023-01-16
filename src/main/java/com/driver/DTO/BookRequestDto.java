package com.driver.DTO;

import com.driver.models.Author;
import com.driver.models.Card;
import com.driver.models.Genre;
import lombok.Data;

@Data
public class BookRequestDto {

    private String name;
    private Genre genre;
    private Author author;
    private Card card;
}
