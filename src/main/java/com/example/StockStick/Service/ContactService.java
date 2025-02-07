package com.example.StockStick.Service;

import com.example.StockStick.Model.Contact;
import com.example.StockStick.Repositery.ContactRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private JavaMailSender javaMailSender;
    @Transactional
    public void saveAndSendEmail(Contact contact)
    {
        contactRepo.save(contact);
        //Send acknowledgement to the user
        SimpleMailMessage UserMessage=new SimpleMailMessage();
        UserMessage.setTo(contact.getEmailAddress());
        UserMessage.setSubject("Thank You For Contacting Us!");
        UserMessage.setText("Dear "+contact.getUsername() +", \n\nThank You For reaching out. We have received Your messages and will get back To You shortly.\n\nBest Regards,\nStockStick.com" );
        javaMailSender.send(UserMessage);
        //Send User's messages  to StockStick.com
        SimpleMailMessage adminMessage=new SimpleMailMessage();
        adminMessage.setTo("shobhitsrivastava2004@gmail.com");
        adminMessage.setSubject("New Contact Us Messages");
        adminMessage.setText("Message from: " +contact.getEmailAddress() +"\n\n" + contact.getMessages());
        javaMailSender.send(adminMessage);
    }



}
