package com.example.StockStick.Controller;

import com.example.StockStick.AllException.CustomException;
import com.example.StockStick.Model.*;
import com.example.StockStick.Service.ItemsService;
import com.example.StockStick.Service.UserPlanService;
import com.example.StockStick.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/app")
public class UserController {
    @Autowired
    private UserPlanService userPlanService;
    @Autowired
    private ItemsService ItemService;
    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender javaMailSender;
    public static final Logger log = LoggerFactory.getLogger(UserController.class);
    @GetMapping("/StockStick.com")
    public String HomePage() {
        return "StockStick";
    }

    @GetMapping("/StockStick")
    public String HomePage1() {
        return "StockStick";
    }

    @GetMapping("Setting")
    // This The UserDetails Which Can Be Display On The Setting Page and Also  Modified Using
    // Edit Profile Button
    private String ShowingUserDetails(Model model, @AuthenticationPrincipal UserPrinciple userPrinciple) {
        Long userId;
        UserDetails userDetails;
        try {
            userId = userPrinciple.getUserId();
            if (userId < 0) {
                log.error("user Id is empty for user principle: {}", userPrinciple);
                throw new CustomException("An unexpected error occurred. Please try again later or contact us for assistance.");
            }
            userDetails = userService.FindById(userPrinciple.getUserId());
            if (userDetails == null) {
                log.error("No user found with email address: {}", userId);
                throw new CustomException("An unexpected error occurred. Please try again later or contact us for assistance.");
            }
        } catch (CustomException e) {
            log.error("Error in Fetching user details: {}", e.getMessage());
            throw new CustomException("Please check your internet connectivity. If the connection is correct, the issue might be on our side. Please wait for 10 minutes or try again later!!!");
        } catch (Exception e) {
            log.error("An Unexpected Error Occurred: {}", e.getMessage());
            throw new CustomException("An unexpected error occurred. Please try again later or contact us for assistance.");

        }
        model.addAttribute("emailAddress", userDetails.getEmailAddress());
        model.addAttribute("firstNameLastName", userDetails.getFirstName().substring(0, 1).toUpperCase() + userDetails.getFirstName().substring(1) + " " + userDetails.getLastName().substring(0, 1).toUpperCase() + userDetails.getLastName().substring(1));
        model.addAttribute("ContactNo", userDetails.getContactNo());
        model.addAttribute("company", userDetails.getCompany());
        model.addAttribute("Bio", userDetails.getBio());
        model.addAttribute("country", userDetails.getCountry());
        model.addAttribute("state", userDetails.getState());
        model.addAttribute("UserPlans", userDetails.getUserPlanData());
        model.addAttribute("UserType", userDetails.getUsertype());
        model.addAttribute("UserId", userDetails.getId());
        return "Setting";
    }

    @GetMapping("/AboutUs")
    public String AboutUs() {
        return "AboutUs";
    }

    @GetMapping("/terms_Policy")
    public String TermsAndPrivacy() {
        return "terms_Policy";
    }

    @GetMapping("/_csrf_Token")
    public CsrfToken GetCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @GetMapping("/Register")
    public String UserRegistration() {
        return "Register";
    }

    @GetMapping("/Pricing")
    private String ViewPricing() {
        return "Pricing";
    }

    @PostMapping("/Register")
    public String userRegister(@ModelAttribute UserRegistrationDTO userRegistrationDTO, Model model, HttpSession session) {
        // Log user registration details for debugging
        try {

            if (userRegistrationDTO.NullChecker(userRegistrationDTO)) {
                log.error("User registration data is missing or invalid. Please try again!!");
                throw new CustomException("User registration data is missing or invalid. Please try again!!");
            }
            // Register the user with the provided details and userId from session
            UserDetails userDetails = userService.register(userRegistrationDTO);
            // Set  the userId in session and Send To Frontend to compare with user entered details during plan selection
            if (userDetails.getId() == null) {
                log.error("User registration failed: User ID is missing.");
                throw new CustomException("User registration failed: User ID is missing.");
            }
            session.setAttribute("userId", userDetails.getId());
            session.setAttribute("registered", true);
            // Retrieve userId from session after setting it
            Long UserId = (Long) session.getAttribute("userId");
            if (UserId == null) {
                log.error("User registration failed: User ID is missing.");
                throw new CustomException("User registration failed: User ID is missing.");
            }
            // Send Confirmation Mail To User To Give Registration Details
            SimpleMailMessage userPlanConfirmationMessage = new SimpleMailMessage();
            userPlanConfirmationMessage.setTo(userRegistrationDTO.getEmailAddress());
            String message = "Dear " + userRegistrationDTO.getFirstName() + " " + userRegistrationDTO.getLastName() + ",\n\n" +
                    "Thank you for registering with StockStick! We're excited to have you on board.\n\n" +
                    "Below are your registration details:\n\n" +
                    "Name: " + userRegistrationDTO.getFirstName() + " " + userRegistrationDTO.getLastName() + "\n" +
                    "Company: " + userRegistrationDTO.getCompany() + "\n" +
                    "Country: " + userRegistrationDTO.getCountry() + "\n" +
                    "User ID: " + UserId + "\n" +
                    "Email Address: " + userRegistrationDTO.getEmailAddress() + "\n" +
                    "Password: " + userRegistrationDTO.getPassword() + "\n\n" +
                    "Please keep this information secure and do not share it with anyone.\n\n" +
                    "If you have any questions or need assistance, feel free to reach out to our support team.\n\n" +
                    "Best regards,\n" +
                    "The StockStick Team";
            userPlanConfirmationMessage.setText(message);

            userPlanConfirmationMessage.setSubject("Registration");

            javaMailSender.send(userPlanConfirmationMessage);
        } catch (MailException e) {
            log.error("Error in Sending Confirmation Mail : {}", e.getMessage());
            throw new CustomException("Failed to send confirmation email. Please try again later or contact us for assistance.!!!");
        } catch (CustomException e) {
            log.error("Error in fetching User details : {}", e.getMessage());
            throw new CustomException("Please check your internet connectivity. If the connection is correct, the issue might be on our side. Please wait for 10 minutes or try again later!!!");
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            throw new CustomException("An internal error occurred. Please try again later or contact us for assistance.!!!");
        }
        // Redirect to UserPlan after successful registration
        return "redirect:/UserPlan";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/logout-success")
    public String LogoutPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        model.addAttribute("secondsLeft", 20);
        request.getCookies();
        return "logout";
    }

    @GetMapping("/UpdateUser")
    public String UpdateUser(@AuthenticationPrincipal UserPrinciple userPrinciple, Model model) {
        String emailAddress;
        UserDetails userDetails;
        emailAddress = userPrinciple.getUsername();
        if (emailAddress.isEmpty()) {
            log.error("There Is some Error While Fetching Your Email address");
            throw new CustomException("Unable to fetch your email address at the moment. Please try again later Or contact Us.");
        }
        try {
            userDetails = userService.FindByEmailAddress(emailAddress);
            if (userDetails == null) {
                log.error("There Is some Error While Fetching Your Details With Email address");
                throw new CustomException("Unable to fetch you email address at the moment. Please try again later Or contact us.");
            }
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw new CustomException(e.getMessage());
        } catch (Exception e) {
            log.error("An unexpected Error occurred: {}", e.getMessage());
            throw new CustomException("An internal error occurred. Please try again later or contact us for assistance.!!!");

        }
        model.addAttribute("FirstName", userDetails.getFirstName());
        model.addAttribute("Country", userDetails.getCountry());
        model.addAttribute("state", userDetails.getState());
        model.addAttribute("ContactNo", userDetails.getContactNo());
        model.addAttribute("Bio", userDetails.getBio());
        model.addAttribute("EmailAddress", userDetails.getEmailAddress());
        model.addAttribute("LastName", userDetails.getLastName());
        model.addAttribute("company", userDetails.getCompany());
        model.addAttribute("UserType", userDetails.getUsertype());
        return "UpdateUser";
    }


@PutMapping("/UpdateUser")
public ResponseEntity<Map<String, String>> updateUserAndItems(@AuthenticationPrincipal UserPrinciple userPrinciple,
                                                              @RequestBody UserRegistrationDTO userRegistrationDTO) {
    String currentEmail = userPrinciple.getUsername();
    Map<String, String> response = new HashMap<>();

    try {
        // Fetch UserDetails
        UserDetails userDetails = userService.FindByEmailAddress(currentEmail);

        if (userDetails != null) {
            if (currentEmail.equals(userRegistrationDTO.getEmailAddress())) {
                // Update UserDetails without changing email address
                userService.UpdateUserWithoutChangingEmailAndId(userDetails, userRegistrationDTO);
                response.put("message", "User updated successfully.");
            } else if (userDetails.getItems().isEmpty()) {
                // Update UserDetails directly if no associated items
                userService.UpdateUser1(userDetails, userRegistrationDTO);
                response.put("message", "User updated successfully.");
            } else if (!userDetails.getEmailAddress().equals(userRegistrationDTO.getEmailAddress())) {
                // Check for duplicate email
                UserDetails existingUser = userService.FindByEmailAddress(userRegistrationDTO.getEmailAddress());
                if (existingUser != null && !existingUser.getId().equals(userDetails.getId())) {
                    response.put("message", "Email address already in use.");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                // Update UserDetails
                userService.UpdateUser(userDetails, userRegistrationDTO);
                response.put("message", "User updated successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } else {
            response.put("message", "User not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    } catch (CustomException e) {
        log.error("Custom exception occurred: ", e);
        response.put("message", "Sorry, there was a specific issue updating your account. Please try again later.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
        log.error("An unexpected error occurred: ", e);
        response.put("message", "Sorry, we could not update your account. Please try again later.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(response, HttpStatus.OK);
}
    // Delete The User And Other Details Related To The User
    @DeleteMapping("/DeleteUser/{Id}")
    public ResponseEntity<Map<String,String>> DeleteUserDetails(@PathVariable("Id") Long Id ,@AuthenticationPrincipal UserPrinciple userPrinciple , UserDetails userDetails) {
        // Using these UserId to first delete Other user's details existing in other Entity
// because we cannot directly delete the user account due to dependencies in Item, UserPlanData, and CustomUserPreferences.
// In CustomUserPreferences, UserId is used as a foreign key, whereas it is a primary key in user details.
// The same applies to UserPlanData.
// In the Item entity, instead of UserId, an email address is used as a reference.
        String emailAddress;
        Map<String,String> response= new HashMap<>();
        try {
            emailAddress = userPrinciple.getUsername();
            Long userId = userDetails.getId();
            if (emailAddress.isEmpty()) {
                log.error("Email Address Is empty:{}", emailAddress);
                throw new CustomException("An unexpected error occurred. Please try again later or contact us for assistance.");
            }
            List<Items> items = ItemService.getSaveProduct(userId);
            if (items.isEmpty()) {
                log.error("Items are empty");
                throw new CustomException("Sorry, we could not find your items in the database. Without items, deletion is not possible. Please verify your account.");
            }
            ItemService.deleteAllItemById(userId);

            List<UserPlanData> userPlanData = userPlanService.GetAllUserPlanDataByUserId(Id);
            if (userPlanData.isEmpty()) {
                log.error("User plan data is empty");
                throw new CustomException("Sorry, we could not find your user plan data in the database. Without user plans, deletion is not possible. Please verify your account.");
            }
            userPlanService.deleteAllUserPlanBySameUserId(Id);
            userDetails = userService.FindByEmailAddress(emailAddress);
            if (userDetails == null) {
                log.error("User details are empty");
                response.put("message", "Sorry, we could not find your user details in the database. Without user details, deletion is not possible. Please verify your account.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            else {
                userService.deleteUserByEmailAddress(emailAddress);
                response.put("message","Your Account Is Permanently deleted.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (CustomException e) {
            log.error("Error in deleting user details: {}", e.getMessage());
            response.put("message",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            response.put("message","An unexpected error occurred. Please try again later or contact us for assistance.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

