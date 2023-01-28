package com.driver.controller;

import com.driver.models.Book;
import com.driver.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//Add required annotations
@RestController
@RequestMapping("book")
public class BookController {
//Create a Book: POST /book/ Pass the Book object as request body Return
// success message “Success” wrapped in a ResponseEntity object Controller Name - createBook

    //Write createBook API with required annotations
@Autowired
    BookService bookService;
    //Add required annotations
    @PostMapping("/createBook")
    public ResponseEntity createBook(@RequestBody() Book book)
    {
        bookService.createBook(book);
        return new ResponseEntity<>("Success",HttpStatus.CREATED);
    }
    //Get Books: GET /book/ Pass nullable parameters genre, availability, and author to filter out books
    // For example: i) If genre=”X”, availability = true, and author=null; we require the list of all books
    // which are available and have genre “X”. Note that these books can be written by any author.
    // ii) If genre=”Y”, availability = false, and author=”A”; we require the list of all books
    // which are written by author “A”, have genre “Y”, and are currently unavailable.
    // Return success message wrapped in a ResponseEntity object Controller Name -
    @GetMapping("/getBooks")
    public ResponseEntity<List<Book>> getBooks(@RequestParam(value = "genre", required = false) String genre,
                                   @RequestParam(value = "available", required = false, defaultValue = "false") boolean available,
                                   @RequestParam(value = "author", required = false) String author){


        List<Book> bookList = bookService.getBooks(genre,available,author);
        return new ResponseEntity<>(bookList, HttpStatus.OK);

    }
}
