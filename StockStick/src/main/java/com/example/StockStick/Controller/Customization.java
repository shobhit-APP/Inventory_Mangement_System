package com.example.StockStick.Controller;

import com.example.StockStick.Model.Custom_or_UserPreferences;
import com.example.StockStick.Model.UserDetails;
import com.example.StockStick.Model.UserPrinciple;
import com.example.StockStick.Repositery.UserRepo;
import com.example.StockStick.Service.CustomizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class Customization {
    @Autowired
    private UserRepo repo;
    @Autowired
    private CustomizationService service;
    @GetMapping("/UserPreferences")
    public String getUserPreferences()
    {
        return "UserPreferences";
    }
    @PostMapping("/UserPreferences")
    public ResponseEntity<?> setUserPreferences(@ModelAttribute Custom_or_UserPreferences customOrUserPreferences , Model model, @AuthenticationPrincipal UserPrinciple userPrinciple)
    {
       if(customOrUserPreferences==null)
       {
           return ResponseEntity.ok().body("{\"message\": \"UserPreferences Not Should be null \"}");
       }
        String emailAddress=userPrinciple.getUsername();
       UserDetails user=repo.findByEmailAddress(emailAddress);
       if(user!=null)
       {
           customOrUserPreferences.setUserDetails(user);
       }
        service.SaveUserPreferences(customOrUserPreferences);
       return ResponseEntity.ok().body("{\"message\": \"Your Preferences1 Saved Successfully\"}");
    }
}
