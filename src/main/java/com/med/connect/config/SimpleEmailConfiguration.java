package com.med.connect.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class SimpleEmailConfiguration {
    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendSimpleEmail(String email , String content , String subject)
    {
        // Try block to check for exceptions
        try {
            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            // Setting up necessary details
            mailMessage.setFrom(email);
            mailMessage.setTo(email);
            mailMessage.setText(content);
            mailMessage.setSubject(subject);

            // Sending the mail
            javaMailSender.send(mailMessage);
            return  true;
        }
        // Catch block to handle the exceptions
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



//    // To send an email with attachment
//    public String
//    sendMailWithAttachment(String email , String content , String subject)
//    {
//        // Creating a mime message
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper;
//
//        try {
//
//            // Setting multipart as true for attachments to
//            // be send
//            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//            mimeMessageHelper.setFrom(email);
//            mimeMessageHelper.setTo(email);
//            mimeMessageHelper.setText(content);
//            mimeMessageHelper.setSubject( subject );
//
//            // Adding the attachment
////            FileSystemResource file= new FileSystemResource(new File(details.getAttachment()));
////
////            mimeMessageHelper.addAttachment(
////                    file.getFilename(), file);
////
////            // Sending the mail
////            javaMailSender.send(mimeMessage);
////            return "Mail sent Successfully";
//        }
//
//        // Catch block to handle MessagingException
//        catch (MessagingException e) {
//
//            // Display message when exception occurred
//            return "Error while sending mail!!!";
//        }
//        return email;
//    }

}
