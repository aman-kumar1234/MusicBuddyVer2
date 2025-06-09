package com.musicapp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static EmailService instance;
    
    // Email configuration
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";
    private static final String USERNAME = "musicapp258@gmail.com";
    private static final String PASSWORD = "ujgl pzsq rvko jylf";
    
    private final Properties properties;
    private final Session session;
    
    private EmailService() {
        properties = new Properties();
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
        
        session.setDebug(true);
    }
    
    public static synchronized EmailService getInstance() {
        if (instance == null) {
            instance = new EmailService();
        }
        return instance;
    }
    
    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
    
    public boolean sendOTP(String toEmail, String otp) {
        try {
            logger.info("Attempting to send OTP to: {}", toEmail);
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your MusicApp OTP");
            
            String emailContent = String.format("""
                Dear User,
                
                Your One-Time Password (OTP) for MusicApp authentication is: %s
                
                This OTP will expire in 5 minutes.
                
                If you didn't request this OTP, please ignore this email.
                
                Best regards,
                MusicApp Team
                """, otp);
            
            message.setText(emailContent);
            
            logger.info("Connecting to SMTP server...");
            Transport transport = session.getTransport("smtp");
            transport.connect(HOST, Integer.parseInt(PORT), USERNAME, PASSWORD);
            
            logger.info("Sending email...");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            
            logger.info("OTP sent successfully to {}", toEmail);
            return true;
            
        } catch (MessagingException e) {
            logger.error("Failed to send OTP email. Error details:", e);
            logger.error("Error message: {}", e.getMessage());
            if (e.getNextException() != null) {
                logger.error("Nested exception: {}", e.getNextException().getMessage());
            }
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error while sending OTP:", e);
            return false;
        }
    }
} 