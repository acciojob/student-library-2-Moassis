package com.driver.services;

import com.driver.models.*;
import com.driver.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

        Book book = bookRepository5.findById(bookId).get();
        if (book == null || book.isAvailable() == false) {
            return "Book is either unavailable or not present";
        }

        Card card = cardRepository5.findById(cardId).get();
        if (card == null || card.getCardStatus() != CardStatus.ACTIVATED) {
            return "Card is invalid";
        }

        List<Book> books = card.getBooks();
        if (books == null)
            books = new ArrayList<>();
        if (books.size() > max_allowed_books) {
            return "Book limit has reached for this card";
        }

        // for transction repository
        Transaction transaction = new Transaction();
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transaction.setBook(book);
        transaction.setCard(card);
        transaction.setIssueOperation(true);

        // for book repository
        List<Transaction> bookTransactions = book.getTransactions();
        if (bookTransactions == null)
            bookTransactions = new ArrayList<>();
        bookTransactions.add(transaction);
        book.setTransactions(bookTransactions);
        book.setAvailable(false);
        book.setCard(card);

        // for card repository
        books.add(book);
        card.setBooks(books);
        List<Transaction> cardTransactions = card.getTransactions();
        if (cardTransactions == null) {
            cardTransactions = new ArrayList<>();
        }
        cardTransactions.add(transaction);
        card.setTransactions(cardTransactions);

        // for saving
        cardRepository5.save(card);

        int id = transaction.getId();
        return String.valueOf(id);
    }

    public Transaction returnBook(int cardId, int bookId) {

        List<Transaction> transactions = transactionRepository5.find(cardId, bookId,
                TransactionStatus.SUCCESSFUL,
                true);
        Transaction transaction = transactions.get(transactions.size() - 1);

        // Days and fine calculation
        Date transactionDate = transaction.getTransactionDate();
        Date currentDate = new Date();
        long diff = currentDate.getTime() - transactionDate.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        int fine = 0;
        if (days > getMax_allowed_days) {
            long daysDiff = days - getMax_allowed_days;
            fine = (int) daysDiff * fine_per_day;
        }

        // for Book Repository
        Book book = bookRepository5.findById(bookId).get();
        book.setAvailable(true);

        Card card = cardRepository5.findById(cardId).get();

        // For transaction repository
        Transaction returnBookTransaction = new Transaction();
        returnBookTransaction.setBook(book);
        returnBookTransaction.setCard(card);
        returnBookTransaction.setFineAmount(fine);
        returnBookTransaction.setIssueOperation(true);
        returnBookTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);

        // for Card Repository
        List<Book> books = card.getBooks();
        ListIterator<Book> itr = books.listIterator();
        while (itr.hasNext()) {
            if (itr.next().equals(book)) {
                itr.remove();
            }
        }
        List<Transaction> cardTransactions = card.getTransactions();
        cardTransactions.add(returnBookTransaction);
        card.setTransactions(cardTransactions);

        // for saving in all repositories
        cardRepository5.save(card);

        return returnBookTransaction; // return the transaction after updating alldetails
    }
}
