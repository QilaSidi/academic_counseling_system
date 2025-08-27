package com.example.newappointmentsystem.services;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.newappointmentsystem.models.Appointment;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;



    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a");

    /**
     * Step 1: Notify counselor about new appointment request
     */
    public void sendAppointmentConfirmation(Appointment appointment, String counselorEmail) {
        try {
            MimeMessage counselorMessage = mailSender.createMimeMessage();
            MimeMessageHelper counselorHelper = new MimeMessageHelper(counselorMessage, true, StandardCharsets.UTF_8.name());

            String htmlContent = createCounselorEmailHtml(appointment);

            counselorHelper.setTo(counselorEmail);
            counselorHelper.setFrom("your-email@example.com");
            counselorHelper.setSubject("New Appointment Request");
            counselorHelper.setText(htmlContent, true);

            mailSender.send(counselorMessage);
            System.out.println("Notification email sent to counselor: " + counselorEmail);

        } catch (Exception e) {
            System.err.println("Failed to send counselor notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Step 2: Send status email to student and counselor after confirmation/rejection
     */
    public void sendAppointmentStatusEmail(Appointment appointment, String studentEmail, String counselorEmail) {
        try {
            // Student email
            MimeMessage studentMessage = mailSender.createMimeMessage();
            MimeMessageHelper studentHelper = new MimeMessageHelper(studentMessage, true, StandardCharsets.UTF_8.name());

            studentHelper.setTo(studentEmail);
            studentHelper.setFrom("your-email@example.com");
            studentHelper.setSubject("Your Appointment Status: " + appointment.getStatus());
            studentHelper.setText(createStudentConfirmationHtml(appointment), true);

            // Counselor email
            MimeMessage counselorMessage = mailSender.createMimeMessage();
            MimeMessageHelper counselorHelper = new MimeMessageHelper(counselorMessage, true, StandardCharsets.UTF_8.name());

            counselorHelper.setTo(counselorEmail);
            counselorHelper.setFrom("your-email@example.com");
            counselorHelper.setSubject("Appointment Status Updated");
            counselorHelper.setText(createCounselorConfirmationHtml(appointment), true);

            // Send both emails
            mailSender.send(studentMessage);
            mailSender.send(counselorMessage);

            System.out.println("Status emails sent successfully for appointment: " + appointment.getId());

        } catch (Exception e) {
            System.err.println("Failed to send status email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------
    // HTML Templates
    // ------------------------
    private String createCounselorEmailHtml(Appointment appointment) {
        String studentName = appointment.getStudent().getFirstName() + " " + appointment.getStudent().getLastName();
        String formattedDate = appointment.getAppointmentDate().format(DATE_FORMAT);
        String formattedTime = appointment.getAppointmentTime().format(TIME_FORMAT);


        return "<html><body style='font-family: sans-serif; color: #333;'>"
                + "<div style='background-color: #f4f4f4; padding: 20px; border-radius: 10px;'>"
                + "<h2 style='color:#2196F3; text-align:center;'>New Appointment Request</h2>"
                + "<p>Dear " + appointment.getCounselor().getFirstName() + ",</p>"
                + "<p>A new appointment has been booked with you. Please review the details and confirm or reject the appointment:</p>"
                + "<table style='width:100%; border-collapse: collapse; margin-top:20px;'>"
                + "<tr><td style='padding:8px; border:1px solid #ddd; font-weight:bold;'>Student:</td><td style='padding:8px; border:1px solid #ddd;'>" + studentName + "</td></tr>"
                + "<tr><td style='padding:8px; border:1px solid #ddd; font-weight:bold;'>Date:</td><td style='padding:8px; border:1px solid #ddd;'>" + formattedDate + "</td></tr>"
                + "<tr><td style='padding:8px; border:1px solid #ddd; font-weight:bold;'>Time:</td><td style='padding:8px; border:1px solid #ddd;'>" + formattedTime + "</td></tr>"
                + "<tr><td style='padding:8px; border:1px solid #ddd; font-weight:bold;'>Topic:</td><td style='padding:8px; border:1px solid #ddd;'>" + appointment.getTopic() + "</td></tr>"
                + "</table>"
                + "<p style='margin-top:30px; text-align:center;'>"
                + "</p>"
                + "<p>Thank you,<br>Your Counseling Team</p>"
                + "</div></body></html>";
    }

    private String createStudentConfirmationHtml(Appointment appointment) {
        Appointment.Status status = appointment.getStatus();
        String counselorName = appointment.getCounselor().getFirstName() + " " + appointment.getCounselor().getLastName();
        String formattedDate = appointment.getAppointmentDate().format(DATE_FORMAT);
        String formattedTime = appointment.getAppointmentTime().format(TIME_FORMAT);

        return "<html><body style='font-family: sans-serif; color:#333;'>"
                + "<div style='background-color:#f4f4f4; padding:20px; border-radius:10px;'>"
                + "<h2 style='color:#4CAF50; text-align:center;'>Appointment " + status + "</h2>"
                + "<p>Dear " + appointment.getStudent().getFirstName() + ",</p>"
                + "<p>Your appointment with " + counselorName + " on " + formattedDate + " at " + formattedTime
                + " has been <strong>" + status + "</strong>.</p>"
                + "<p>Thank you,<br>Your Counseling Team</p>"
                + "</div></body></html>";
    }

    private String createCounselorConfirmationHtml(Appointment appointment) {
        Appointment.Status status = appointment.getStatus();
        String studentName = appointment.getStudent().getFirstName() + " " + appointment.getStudent().getLastName();
        String formattedDate = appointment.getAppointmentDate().format(DATE_FORMAT);
        String formattedTime = appointment.getAppointmentTime().format(TIME_FORMAT);

        return "<html><body style='font-family: sans-serif; color:#333;'>"
                + "<div style='background-color:#f4f4f4; padding:20px; border-radius:10px;'>"
                + "<h2 style='color:#2196F3; text-align:center;'>Appointment " + status + "</h2>"
                + "<p>Dear " + appointment.getCounselor().getFirstName() + ",</p>"
                + "<p>You have <strong>" + status + "</strong> the appointment with " + studentName
                + " scheduled on " + formattedDate + " at " + formattedTime + ".</p>"
                + "<p>Thank you,<br>Your Counseling Team</p>"
                + "</div></body></html>";
    }
}
