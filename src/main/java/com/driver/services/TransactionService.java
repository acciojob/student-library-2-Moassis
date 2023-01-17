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
        books.add(book);

        Transaction newTransaction = new Transaction();
        newTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        newTransaction.setBook(book);
        newTransaction.setCard(card);
        newTransaction.setIssueOperation(true);

        books.add(book);

        transactionRepository5.save(newTransaction);

        List<Transaction> transactions = book.getTransactions();
        if (transactions == null)
            transactions = new ArrayList<>();
        transactions.add(newTransaction);
        book.setAvailable(false);

        transactionRepository5.save(newTransaction);
        bookRepository5.save(book);

        int id = newTransaction.getId();
        return String.valueOf(id); // return transactionId instead
    }

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
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        if (days > getMax_allowed_days) {
            long daysDiff = days - getMax_allowed_days;
            long fine = daysDiff * fine_per_day;
            transaction.setFineAmount((int) fine);
        }

        Book book = transaction.getBook();
        book.setAvailable(true);
        bookRepository5.save(book);

        Card card = transaction.getCard();
        List<Book> books = card.getBooks();
        ListIterator<Book> itr = books.listIterator();

        while (itr.hasNext()) {
            if (itr.next().equals(book)) {
                itr.remove();
            }
        }

        transactionRepository5.save(transaction);
        Transaction returnBookTransaction = transaction;
        return returnBookTransaction; // return the transaction after updating alldetails
    }
}
