package com.example.StockStick.Controller;

import com.example.StockStick.AllException.CustomException;
import com.example.StockStick.Model.Contact;
import com.example.StockStick.Service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/app")
public class ContactUsController {
    public static final  Logger log= LoggerFactory.getLogger(ContactUsController.class);
    @Autowired
    private ContactService contactService;
    @GetMapping("/ContactUs")
    public String ContactUs()
    {
        return "ContactUs";
    }
    @PostMapping("/ContactUs")
    public String ContactUs( @ModelAttribute  Contact contact , Model model)
    {
        try {
            contactService.saveAndSendEmail(contact);
        }
        catch (CustomException e)
        {
            log.error("Error In Contact us Controller:");
            throw new CustomException("Please check your internet connectivity. If the connection is correct, the issue might be on our side. Please wait for 10 minutes or try again later!!!");
        }
        model.addAttribute("message","Thank you For Contacting Us We Received Your Request!!! ");
        return "ContactUs";
    }
}
