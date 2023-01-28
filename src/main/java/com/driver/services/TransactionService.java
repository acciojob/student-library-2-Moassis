package com.driver.services;

import com.driver.models.*;
import com.driver.repositories.BookRepository;
import com.driver.repositories.CardRepository;
import com.driver.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.driver.models.CardStatus.ACTIVATED;
import static com.driver.models.CardStatus.DEACTIVATED;

@Service
public class TransactionService {

    @Autowired
    BookRepository bookRepository5;

    @Autowired
    CardRepository cardRepository5;

    @Autowired
    TransactionRepository transactionRepository5;

    @Value("${books.max_allowed}")
    public int max_allowed_books;

    @Value("${books.max_allowed_days}")
    public int getMax_allowed_days;

    @Value("${books.fine.per_day}")
    public int fine_per_day;

    public String issueBook(int cardId, int bookId) throws Exception {

        //conditions required for successful transaction of issue book:
        //1. book is present and available
        // If it fails: throw new Exception("Book is either unavailable or not present");
        //2. card is present and activated
        // If it fails: throw new Exception("Card is invalid");
        //3. number of books issued against the card is strictly less than max_allowed_books
        // If it fails: throw new Exception("Book limit has reached for this card");
        //If the transaction is successful, save the transaction to the list of transactions and return the id

        //Note that the error message should match exactly in all cases

        //check whether bookId and cardId already exist

         Transaction transaction = new Transaction();

        // getting book and card
        Book book = bookRepository5.findById(bookId).get();
        Card card = cardRepository5.findById(cardId).get();

        // set book and card with transaction

        transaction.setBook(book);
        transaction.setCard(card);

        // set general attributes of transaction which will be triggered whether the book is issued or not
        transaction.setTransactionDate(new Date());
        transaction.setIssueOperation(true);

        // when book is not present or is unavailable
        if(book == null || book.isAvailable()==false)
        {
           transaction.setTransactionStatus(TransactionStatus.FAILED);
           // save the transaction since book is unavailable or is not present
            transactionRepository5.save(transaction);
            throw new Exception("Book is either unavailable or not present");
        }
        // when card is deactivated or is not present
        if(card == null || card.getCardStatus().equals(DEACTIVATED))
        {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository5.save(transaction);
            throw new Exception("Card is invalid");

        }

        // when card limit is reached
        if(card.getBooks().size() >= max_allowed_books)
        {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository5.save(transaction);
                
        }
        // if it comes here then book is present and availble ,card is present ,activated and the limit is not crossed

        // make book unavaiable
        book.setAvailable(false);

        // add book in card
        List<Book> bookList = card.getBooks();
        bookList.add(book);
        card.setBooks(bookList);

        //set card in book
        book.setCard(card);

        // set attributes of transaction
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);

        // save transaction in book
//        List<Transaction> booktransactionList = book.getTransactions();
//        booktransactionList.add(transaction);
//        book.setTransactions(booktransactionList);


        // save card
        cardRepository5.save(card);
        // update book
        bookRepository5.updateBook(book);
        // save transaction
        transactionRepository5.save(transaction);

        return transaction.getTransactionId();
     //  return null; //return transactionId instead
    }

    public Transaction returnBook(int cardId, int bookId) throws Exception{



        //for the given transaction calculate the fine amount considering the book has been returned exactly when this function is called
        //make the book available for other users
        //make a new transaction for return book which contains the fine amount as well

        // get book and card
        Book book = bookRepository5.findById(bookId).get();
        Card card = cardRepository5.findById(cardId).get();

        // getting the last transaction of book
        List<Transaction> transactionList = transactionRepository5.find(cardId,bookId,TransactionStatus.SUCCESSFUL,true);
        Transaction lastTransaction = transactionList.get(transactionList.size()-1);


        //create return transaction
        Transaction returnTransaction = new Transaction();

        // set book and card for return transaction
        returnTransaction.setBook(book);
        returnTransaction.setCard(card);

        // set general atributes of return transaction
        returnTransaction.setTransactionDate(new Date());
        returnTransaction.setIssueOperation(false);

        // calculate fine

        int fine =0;
        Date issueDate = lastTransaction.getTransactionDate();
        // number of days elapsed
        long numberOfDaysElapsed = (System.currentTimeMillis() - issueDate.getTime())/(1000*60*60*24);
        if(numberOfDaysElapsed > getMax_allowed_days)
        {
            fine= (int)(numberOfDaysElapsed-getMax_allowed_days)*fine_per_day;
        }

        // set book unavailable and remove card from book
        book.setAvailable(true);
        book.setCard(null);// important

        // remove book from card
        List<Book> bookList = card.getBooks();
        bookList.remove(book);

        // set attributes of returnTransaction
        returnTransaction.setFineAmount(fine);
        returnTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);

        // set return transaction in booktransaction list

//        List<Transaction> bookstransactions = book.getTransactions();
//        bookstransactions.add(returnTransaction);
//        book.setTransactions(bookstransactions);

        // save card
        cardRepository5.save(card);
        //update book
        bookRepository5.updateBook(book);


        // save return transaction
        transactionRepository5.save(returnTransaction);
        return returnTransaction;

        //return the transaction after updating all details
    }
}
