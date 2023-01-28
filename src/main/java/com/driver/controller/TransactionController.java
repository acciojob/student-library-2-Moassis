package com.driver.controller;

import com.driver.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Add required annotations
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    // Issue book: POST /transaction/issueBook Given cardId and bookId,
    // assign the book to the given card Return success message wrapped in a
    // ResponseEntity object Controller Name - issueBook
    //
    // Return book: POST /transaction/returnBook Given cardId and bookId,
    // return the book from the given card Return success message wrapped in a
    // ResponseEntity object
    // Controller Name - returnBook
    // Add required annotations
    @PostMapping("/issueBook")
    public ResponseEntity<String> issueBook(@RequestParam("cardId") int cardId, @RequestParam("bookId") int bookId)
            throws Exception {
        String transId = transactionService.issueBook(cardId, bookId);
        return new ResponseEntity<>("transaction completed, Transaction id is " + transId, HttpStatus.ACCEPTED);
    }

    // Add required annotations
    @PostMapping("/returnBook")
    public ResponseEntity<String> returnBook(@RequestParam("cardId") int cardId, @RequestParam("bookId") int bookId)
            throws Exception {

        return new ResponseEntity<>("transaction completed", HttpStatus.ACCEPTED);
    }
}
