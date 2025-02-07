package com.example.StockStick.Service;

import com.example.StockStick.Model.Custom_or_UserPreferences;
import com.example.StockStick.Repositery.CustomizationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomizationService {
    @Autowired
    private CustomizationRepo customization;
    @Transactional
    public void SaveUserPreferences(Custom_or_UserPreferences customOrUserPreferences) {
        System.out.println(customOrUserPreferences.getHighStockThreshold());
        System.out.println(customOrUserPreferences.getMediumStockThreshold());
        System.out.println(customOrUserPreferences.getLowStockThreshold());
        customization.save(customOrUserPreferences);
    }
    @Transactional
    public List<Custom_or_UserPreferences> getSaveUserPreferences(String emailAddress) {
        return  customization.findByUserDetails_EmailAddress(emailAddress);
    }
    @Transactional
    public void deleteByUserDetails_EmailAddress(String emailAddress) {
        customization.deleteByUserDetails_EmailAddress(emailAddress);
    }
}
