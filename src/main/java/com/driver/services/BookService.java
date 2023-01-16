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
        book.setAvailable(true);
        bookRepository2.save(book);

        Author author = book.getAuthor();
        List<Book> books = author.getBooksWritten();
        if (books == null) {
            books = new ArrayList<>();
        }
        books.add(book);
        author.setBooksWritten(books);
    }

    // • Get Books: GET /book/ Pass nullable parameters genre, availability, and
    // author to filter out books For example:
    // i) If genre=”X ”, availability =
    // true, and author=null; we require the list of all books which are available
    // and have genre “X”. Note that these books can be written by any author.
    // ii)
    // If genre=”Y”, availability = false, and author=”A”; we require the list of
    // all books which are written by author “A”, have genre “Y”, and are currently
    // unavailable. Return success message wrapped in a ResponseEntity object
    // Controller Name - getBooks

    public List<Book> getBooks(String genre, boolean available, String author) {
        List<Book> books = new ArrayList<>(); // find the elements of the list by yourself
        if (genre != null && available == true && author == null) {
            books = bookRepository2.findBooksByGenre(genre, available);
        } else if (genre != null && available == false && author != null) {
            books = bookRepository2.findBooksByGenreAuthor(genre, author, available);
        }
        return books;
    }
}
