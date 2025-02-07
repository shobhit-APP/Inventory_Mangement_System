package com.example.StockStick.AllException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler  {
    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    public String HandelAnyException(CustomException e, Model model)
    {
        model.addAttribute("status","Error");
        model.addAttribute("error_message",e.getMessage());
        return "error";
    }
}
