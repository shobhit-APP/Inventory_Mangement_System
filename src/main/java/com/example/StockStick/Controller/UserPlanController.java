package com.example.StockStick.Controller;
import com.example.StockStick.Model.UserDetails;
import com.example.StockStick.Model.UserPlanData;
import com.example.StockStick.Repositery.UserRepo;
import com.example.StockStick.Service.UserPlanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class UserPlanController {
    @Autowired
    private UserPlanService userPlanService;
    @Autowired
    private  UserRepo userRepo;
    @GetMapping("/UserPlan")
    public String UserPlan(Model model , HttpSession session) {
        // Retrieve UserId from the session
        Long userId=(Long) session.getAttribute("userId");
        if(userId!=null)
        {
            model.addAttribute("userId",userId);
        }
        return "UserPlan";
    }
    @PostMapping("/UserPlan")
    public String UserPlan(@ModelAttribute UserPlanData userPlanData, Model model ,HttpSession session)
        {
            // Retrieve UserDetails from the database
            Long userId=(Long) session.getAttribute("userId");
            if(userId!=null)
            {
                UserDetails userDetails=userRepo.findById(userId).orElseThrow(()->new RuntimeException("User Not Found"));
                userPlanData.setUserDetails(userDetails);
                System.out.println(userPlanData.getPlanType());
                System.out.println(userPlanData.getPlanId());
                userPlanService.UserPlans(userPlanData);
                model.addAttribute("message", "Subscription Confirmed!");
                model.addAttribute("message1", "Thank you for your subscription");
                model.addAttribute("message2","Selected Plan Details");
                model.addAttribute("planId", userPlanData.getPlanId());
                model.addAttribute("planType", userPlanData.getPlanType());
                model.addAttribute("expiryDate", userPlanData.getExpiryDate());
            }
            return "SelectedPlan";
        }

        @GetMapping("/SelectedPlan")
        public String SelectedPlan ()
        {
            return "SelectedPlan";
        }
    }