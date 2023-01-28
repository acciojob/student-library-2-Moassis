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

        Transaction transaction = new Transaction();

        Book book = bookRepository5.findById(bookId).get();
        Card card = cardRepository5.findById(cardId).get();

        transaction.setBook(book);
        transaction.setCard(card);
        transaction.setTransactionDate(new Date());
        transaction.setIssueOperation(true);

        if (book == null || book.isAvailable() == false) {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository5.save(transaction);
            throw new Exception("Book is either unavailable or not present");
        }

        if (card == null || card.getCardStatus().equals(DEACTIVATED)) {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository5.save(transaction);
            throw new Exception("Card is invalid");

        }

        if (card.getBooks().size() >= max_allowed_books) {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository5.save(transaction);

        }

        book.setAvailable(false);

        List<Book> bookList = card.getBooks();
        bookList.add(book);
        card.setBooks(bookList);

        book.setCard(card);

        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);

        cardRepository5.save(card);

        bookRepository5.updateBook(book);

        transactionRepository5.save(transaction);

        return transaction.getTransactionId();
    }

    public Transaction returnBook(int cardId, int bookId) throws Exception {

        Book book = bookRepository5.findById(bookId).get();
        Card card = cardRepository5.findById(cardId).get();

        List<Transaction> transactionList = transactionRepository5.find(cardId, bookId, TransactionStatus.SUCCESSFUL,
                true);
        Transaction lastTransaction = transactionList.get(transactionList.size() - 1);

        Transaction returnTransaction = new Transaction();

        returnTransaction.setBook(book);
        returnTransaction.setCard(card);

        returnTransaction.setTransactionDate(new Date());
        returnTransaction.setIssueOperation(false);

        int fine = 0;
        Date issueDate = lastTransaction.getTransactionDate();
        // number of days elapsed
        long numberOfDaysElapsed = (System.currentTimeMillis() - issueDate.getTime()) / (1000 * 60 * 60 * 24);
        if (numberOfDaysElapsed > getMax_allowed_days) {
            fine = (int) (numberOfDaysElapsed - getMax_allowed_days) * fine_per_day;
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

        cardRepository5.save(card);
        // update book
        bookRepository5.updateBook(book);

        // save return transaction
        transactionRepository5.save(returnTransaction);
        return returnTransaction;

        // return the transaction after updating all details
    }
}
