package com.driver.controller;

import com.driver.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping("/issueBook")
    public ResponseEntity<String> issueBook(@RequestParam("cardId") int cardId, @RequestParam("bookId") int bookId)
            throws Exception {
        String transId = transactionService.issueBook(cardId, bookId);
        return new ResponseEntity<>("transaction completed, Transaction id is " + transId, HttpStatus.ACCEPTED);
    }

    @PostMapping("/returnBook")
    public ResponseEntity<String> returnBook(@RequestParam("cardId") int cardId, @RequestParam("bookId") int bookId)
            throws Exception {

        return new ResponseEntity<>("transaction completed", HttpStatus.ACCEPTED);
    }
}
