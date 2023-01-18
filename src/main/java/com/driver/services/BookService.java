package com.driver.services;

import com.driver.models.Author;
import com.driver.models.Book;
import com.driver.repositories.AuthorRepository;
import com.driver.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository2;

    @Autowired
    AuthorRepository authorRepository;

    public void createBook(Book book) {
        Author author = book.getAuthor();
        book.setAuthor(author);

        List<Book> currentListOfBooks = author.getBooksWritten();
        if (currentListOfBooks == null)
            currentListOfBooks = new ArrayList<>();
        currentListOfBooks.add(book);
        author.setBooksWritten(currentListOfBooks);
        authorRepository.save(author);

    }

    public List<Book> getBooks(String genre, boolean available, String author) {
        if (genre != null && available == true && author != null)
            return bookRepository2.findBooksByGenreAuthor(genre, author, available);

        else if (author != null)
            return bookRepository2.findBooksByAuthor(author, available);
        else if (genre != null)
            return bookRepository2.findBooksByGenre(genre, available);
        else
            return bookRepository2.findByAvailability(available);

    }
}
