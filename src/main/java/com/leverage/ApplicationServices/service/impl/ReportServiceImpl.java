package com.leverage.ApplicationServices.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import com.leverage.ApplicationServices.enums.Roles;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.leverage.ApplicationServices.model.JobApplications;
import com.leverage.ApplicationServices.model.User;
import com.leverage.ApplicationServices.repo.JobApplicationsRepo;
import com.leverage.ApplicationServices.repo.UserRepo;
import com.leverage.ApplicationServices.service.EmailService;
import com.leverage.ApplicationServices.service.ReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final JobApplicationsRepo jobApplicationsRepo;
    private final UserRepo userRepo;
    private final EmailService emailService;

    @Override
    public void generateReport() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(7);

        List<JobApplications> applications = jobApplicationsRepo.findBycreatedDateBetween(startDate, endDate);
        List<User> users = userRepo.findBycreatedDateBetween(startDate, endDate);

        generateJobApplicationsReport(applications, startDate, endDate);
        generateUserRegistrationReport(users, startDate, endDate);
    }

    private void generateJobApplicationsReport(List<JobApplications> applications, LocalDateTime startDate, LocalDateTime endDate) {
        String excelPath = "weekly_job_application_report.xlsx";
        
        try (Workbook workbook = new XSSFWorkbook(); 
        		FileOutputStream fileOut = new FileOutputStream(new File(excelPath))) {
            Sheet sheet = workbook.createSheet("Job Applications");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Name", "Email", "Technology", "Company Name", "Role", "Status", "Marketing Member", "Mobile", "Platform"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }
            int rowNum = 1;
            for (JobApplications app : applications) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(app.getCandidate().getUser().getFirstName() + " " + app.getCandidate().getUser().getLastName());
                row.createCell(1).setCellValue(app.getCandidate().getUser().getMail());
                row.createCell(2).setCellValue(app.getTechnology());
                row.createCell(3).setCellValue(app.getCompanyName());
                row.createCell(4).setCellValue(app.getRole());
                row.createCell(5).setCellValue(app.getStatus().toString());
                row.createCell(6).setCellValue(app.getMarketingMember().getUser().getFirstName() + " " + app.getMarketingMember().getUser().getLastName());
                row.createCell(7).setCellValue(app.getCandidate().getUser().getMobileNumber());
                row.createCell(8).setCellValue(app.getApplicationPlatform());
            }
            workbook.write(fileOut);
            File excelFile = new File(excelPath);
            emailService.sendEmailForJobDetails(excelFile);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
    private void generateUserRegistrationReport(List<User> users, LocalDateTime startDate, LocalDateTime endDate) {
        String excelPath = "weekly_registration_report.xlsx";

        try (Workbook workbook = new XSSFWorkbook(); 
             FileOutputStream fileOut = new FileOutputStream(new File(excelPath))) {
            
            Sheet sheet = workbook.createSheet("User Registrations");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Name", "Email", "Role", "Mobile Number"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook)); // Method to style headers
            }
            
            // Add user details
            int rowNum = 1;
            for (User usr : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(usr.getFirstName() + " " + usr.getLastName());
                row.createCell(1).setCellValue(usr.getMail());
                row.createCell(2).setCellValue(usr.getRoles() == Roles.ADMIN ? "Admin" : usr.getRoles() == Roles.MARKETING ? "Marketing Member" : "Candidate");
                row.createCell(3).setCellValue(usr.getMobileNumber());
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(fileOut);
            File excelFile = new File(excelPath);
            emailService.sendEmailOfNewUsers(excelFile);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Scheduled(cron = "0 0 9 * * Mon")
    public void scheduledReport() {
        generateReport();
    }
}
