
package com.example.StockStick.Service;
import com.example.StockStick.AllException.CustomException;
import com.example.StockStick.Model.UserDetails;
import com.example.StockStick.Model.UserPlanData;

import com.example.StockStick.Repositery.UserPlanRepo;
import com.example.StockStick.Repositery.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
public class UserPlanService {
    private static final Logger log= LoggerFactory.getLogger(UserPlanService.class);
    @Autowired
    private UserPlanRepo userPlanRepo;
    @Autowired
    private PlanCalculationService planCalculationService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserRepo userRepo;
    @Transactional
    public void UserPlans(UserPlanData userPlanData ) {
        // Save Plan Data

        userPlanData.setSelectedDate(LocalDate.now());
        LocalDate expiryDate = planCalculationService.calculateExpiryDate(userPlanData.getPlanType());
        userPlanData.setExpiryDate(expiryDate);
        try {
            userPlanRepo.save(userPlanData);
            Long userId = userPlanData.getUserDetails().getId();
            if (userId != null) {
                UserDetails userDetails = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User Not found with this userid"));
                System.out.println(userDetails.getEmailAddress());
                SimpleMailMessage userPlanConfirmationMessage = new SimpleMailMessage();
                userPlanConfirmationMessage.setTo(userDetails.getEmailAddress());
                String message = "Dear " + userDetails.getFirstName() + " " + userDetails.getLastName() + ",\n\n"
                        + "Thank you for Selecting A Plan StockStick for your business!\n\n"
                        + "This is your Plan details:\n"
                        + "Name: " + userDetails.getFirstName() + " " + userDetails.getLastName() + "\n\n"
                        + "Company: " + userDetails.getCompany() + "\n\n"
                        + "country: " + userDetails.getCountry() + "\n\n"
                        + "UserId " + userPlanData.getUserDetails().getId() + "\n\n"
                        + "Plan Type: " + userPlanData.getPlanType() + "\n\n"
                        + "SelectedDate:  " + userPlanData.getSelectedDate() + "\n\n"
                        + "Expiry Date:  " + userPlanData.getExpiryDate() + "\n\n"
                        + "Plan Id:  " + userPlanData.getPlanId() + "\n\n";

                userPlanConfirmationMessage.setText(message);
                userPlanConfirmationMessage.setSubject("Plan Confirmation Details");
                javaMailSender.send(userPlanConfirmationMessage);
            }
        } catch (MailException e) {
            log.error("Error in Sending Confirmation Mail : {}", e.getMessage());
            throw new CustomException("Failed to send confirmation email. Please try again later or contact us for assistance.!!!");
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            throw new CustomException("An internal error occurred. Please try again later or contact us for assistance.!!!");
        }
    }
    @Transactional
    public List<UserPlanData> GetAllUserPlanDataByUserId(Long Id) {
        return  userPlanRepo.findByUserDetails_Id(Id);
    }
    @Transactional
    public void deleteAllUserPlanBySameUserId(Long Id) {
        userPlanRepo.deleteByUserDetails_Id(Id);
    }
}