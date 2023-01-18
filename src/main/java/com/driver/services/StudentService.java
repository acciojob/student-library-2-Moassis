package com.driver.services;

import com.driver.models.Book;
import com.driver.models.Card;
import com.driver.models.Student;
import com.driver.repositories.CardRepository;
import com.driver.repositories.StudentRepository;

import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    CardService cardService4;

    @Autowired
    StudentRepository studentRepository4;

    @Autowired
    CardRepository cardRepository3;

    @Autowired
    TransactionService transactionService;

    public Student getDetailsByEmail(String email) {
        Student student = studentRepository4.findByEmailId(email);
        return student;
    }

    public Student getDetailsById(int id) {
        Student student = studentRepository4.findById(id).get();
        return student;
    }

    public void createStudent(Student student) {
        Card card = cardService4.createAndReturn(student);
        student.setCard(card);
        studentRepository4.save(student);
    }

    public void updateStudent(Student student) {
        studentRepository4.updateStudentDetails(student);
    }

    public void deleteStudent(int id) throws Exception {
        // Delete student and deactivate corresponding card
        Student student = studentRepository4.findById(id).get();
        Card card = student.getCard();
        int cardId = card.getId();
        List<Book> books = card.getBooks();
        ListIterator<Book> itr = books.listIterator();
        while (itr.hasNext()) {
            Book book = itr.next();
            int bookId = book.getId();
            transactionService.returnBook(cardId, bookId);
        }
        cardService4.deactivateCard(id);
        studentRepository4.deleteCustom(id);
    }
}
