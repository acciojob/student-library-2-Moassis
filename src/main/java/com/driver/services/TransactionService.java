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

        Card card = cardRepository5.findById(cardId).get();
        Book book = bookRepository5.findById(bookId).get();

        if (card != null && book != null) {
            if (book.isAvailable() == true) {
                if (card.getCardStatus() == CardStatus.ACTIVATED) {
                    int currentNumberOfBooks = card.getBooks().size();
                    if (currentNumberOfBooks < max_allowed_books - 1) {

                        // for card repository
                        List<Book> books = card.getBooks();
                        books.add(book);
                        card.setBooks(books);

                        // for transction repository
                        Transaction transaction = new Transaction();
                        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
                        transaction.setBook(book);
                        transaction.setCard(card);
                        transaction.setIssueOperation(true);

                        // for book repository
                        List<Transaction> transactions = book.getTransactions();
                        if (transactions == null)
                            transactions = new ArrayList<>();
                        transactions.add(transaction);
                        book.setTransactions(transactions);
                        book.setAvailable(false);
                        book.setCard(card);

                        transactionRepository5.save(transaction);

                        int id = transaction.getId();
                        return String.valueOf(id);

                    } else
                        throw new Exception("Book limit has reached for this card");
                } else {
                    throw new Exception("Card is invalid");
                }
            } else {
                throw new Exception("Book is either unavailable or not present");
            }
        }
        return null;
    }

    // Book book = bookRepository5.findById(bookId).get();
    // if (book == null || book.isAvailable() == false) {
    // return "Book is either unavailable or not present";
    // }

    // Card card = cardRepository5.findById(cardId).get();
    // if (card == null || card.getCardStatus() != CardStatus.ACTIVATED) {
    // return "Card is invalid";
    // }

    // List<Book> books = card.getBooks();
    // if (books == null)
    // books = new ArrayList<>();
    // if (books.size() > max_allowed_books) {
    // return "Book limit has reached for this card";
    // }

    // books.add(book);
    // card.setBooks(books);

    // Transaction newTransaction = new Transaction();
    // newTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
    // newTransaction.setBook(book);
    // newTransaction.setCard(card);
    // newTransaction.setIssueOperation(true);

    // List<Transaction> transactions = book.getTransactions();
    // if (transactions == null)
    // transactions = new ArrayList<>();
    // transactions.add(newTransaction);
    // book.setTransactions(transactions);
    // book.setAvailable(false);
    // book.setCard(card);

    // cardRepository5.save(card);
    // transactionRepository5.save(newTransaction);

    // int id = newTransaction.getId();
    // return String.valueOf(id); // return transactionId instead
    // }

    public Transaction returnBook(int cardId, int bookId) throws Exception {

        List<Transaction> transactions = transactionRepository5.find(cardId, bookId,
                TransactionStatus.SUCCESSFUL,
                true);
        Transaction transaction = transactions.get(transactions.size() - 1);

        // for the given transaction calculate the fine amount considering the book has
        // been returned exactly when this function is called
        // make the book available for other users
        // make a new transaction for return book which contains the fine amount as well
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

        // for Card Repository
        Card card = cardRepository5.findById(cardId).get();
        List<Book> books = card.getBooks();
        ListIterator<Book> itr = books.listIterator();
        while (itr.hasNext()) {
            if (itr.next().equals(book)) {
                itr.remove();
            }
        }

        // For transaction repository
        Transaction returnBookTransaction = new Transaction();
        returnBookTransaction.setBook(book);
        returnBookTransaction.setCard(card);
        returnBookTransaction.setFineAmount(fine);
        returnBookTransaction.setIssueOperation(true);
        returnBookTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);

        transactionRepository5.save(returnBookTransaction);
        cardRepository5.save(card);

        return returnBookTransaction; // return the transaction after updating alldetails
    }
}
