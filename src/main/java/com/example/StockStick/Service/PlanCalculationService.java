package com.example.StockStick.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class PlanCalculationService {
    //Method To Calculate A Plan expiry Data
    // Using Switch Method
    @Transactional
    public LocalDate calculateExpiryDate(String PlanType)
    {
        LocalDate now=LocalDate.now();
        return switch (PlanType) {
            case "BasicPlan" -> now.plusMonths(1);
            case "AdvancePlan" -> now.plusMonths(6);
            case "EnterPrisePlan" -> now.plusYears(1);
            default -> throw new IllegalArgumentException("Invalid plan Id");
        };
    }

}
