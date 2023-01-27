package com.driver.services;

import com.driver.models.Book;
import com.driver.repositories.AuthorRepository;
import com.driver.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository2;

    @Autowired
    AuthorRepository authorRepository;

    public void createBook(Book book) {
        bookRepository2.save(book);

        // // for Book Repository
        // book.setAvailable(true);
        // Author author = book.getAuthor();

        // List<Book> books = author.getBooksWritten();
        // if (books == null)
        // books = new ArrayList<>();
        // books.add(book);
        // author.setBooksWritten(books);
        // authorRepository.save(author);

    }

    public List<Book> getBooks(String genre, boolean available, String author) {
        // 1 both author and genre are not null
        if (genre != null && available == true && author != null)
            return bookRepository2.findBooksByGenreAuthor(genre, author, available);
        // 2 genre is null and author is not null
        else if (author != null)
            return bookRepository2.findBooksByAuthor(author, available);
        // 3 author is null and genre is not null
        else if (genre != null)
            return bookRepository2.findBooksByGenre(genre, available);
        // 4 both genre and author are null
        else
            return bookRepository2.findByAvailability(available);
    }
}
