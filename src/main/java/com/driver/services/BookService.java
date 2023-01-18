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

        List<Book> books = author.getBooksWritten();
        if (books == null)
            books = new ArrayList<>();
        books.add(book);
        author.setBooksWritten(books);
        bookRepository2.save(book);

    }

    public List<Book> getBooks(String genre, boolean available, String author) {
        List<Book> books = null;
        if (genre != null && available == true && author == null)
            books = bookRepository2.findBooksByGenre(genre, available);
        else if (genre != null && author != null)
            books = bookRepository2.findBooksByGenreAuthor(genre, author, available);
        return books;
    }
}
