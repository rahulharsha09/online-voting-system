package com.example.rtp2;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailUtil {

    // ✅ TEMP: direct values (use this first)
    private static final String FROM_EMAIL = "rahulharsha3131@gmail.com";
    private static final String PASSWORD   = "iood wbxn tila qduk";

    public static boolean sendOTP(String toEmail, String otp) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        try {
            Session session = Session.getInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
                        }
                    });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Email Verification OTP");
            message.setText("Your OTP is: " + otp);

            System.out.println("Sending OTP to: " + toEmail);

            Transport.send(message);

            System.out.println("OTP sent successfully ✅");

            return true;

        } catch (Exception e) {
            System.out.println("EMAIL ERROR: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}