package com.leverage.ApplicationServices.service;

import java.io.File;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

	private final JavaMailSender emailSender;
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	public EmailService(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	public void sendEmail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("deepndhar2deepu@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
		logger.info("Mail sent to {}", to);
	}

	public void sendEmailToCandidate(String to, String password, String subject, String text) {
		String fullText = text + "\n\nYour Password is: " + password
				+ "\n\nBest regards,\nLeverage Technologies Inc,\nATS Team";
		sendEmail(to, subject, fullText);
	}

	public void sendEmailWithAttachment(String to, String subject, String text, File pdfFile) {
		try {
			MimeMessage mimeMessage = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text);
			helper.addAttachment(pdfFile.getName(),pdfFile );
			emailSender.send(mimeMessage);
			logger.info("Email with attachment sent to {}", to);
		} catch (MessagingException e) {
			logger.error("Failed to send email with attachment to {}: {}", to, e.getMessage());
		}
	}

	public void sendEmailForJobDetails(File pdfFile) {
		System.out.println("sendEmailForJobDetails email sent");
		String subject = "Weekly Job Applications Report";
		String text = "Attached is the weekly report of job applications.";
		sendEmailWithAttachment("deepndhar2deepu@gmail.com", subject, text, pdfFile);
	}

	public void sendEmailOfNewUsers(File pdfFile) {
		System.out.println("sendEmailOfNewUsers email sent");
		String subject = "Weekly Registrations Report";
		String text = "Attached is the details of new registrations.";
		sendEmailWithAttachment("deepndhar2deepu@gmail.com", subject, text, pdfFile);
	}
}
