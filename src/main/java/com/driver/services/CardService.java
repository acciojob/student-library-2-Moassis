package com.driver.services;

import com.driver.models.Student;
import com.driver.models.Card;
import com.driver.models.CardStatus;
import com.driver.repositories.CardRepository;
import com.driver.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CardService {


    @Autowired
    CardRepository cardRepository3;
    @Autowired
    StudentRepository studentRepository;

    public Card createAndReturn(Student student){
        Card card = new Card();
        //link student with a new card
      // card.setCreatedOn(new Date());
     //   card.setUpdatedOn(new Date());
        card.setStudent(student);
        student.setCard(card);

     //studentRepository.save(student);
        cardRepository3.save(card);
        return card;
    }

    public void deactivateCard(int student_id){
        cardRepository3.deactivateCard(student_id, CardStatus.DEACTIVATED.toString());
    }
}
