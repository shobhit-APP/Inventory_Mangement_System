package com.example.StockStick.Repositery;

import com.example.StockStick.Model.Custom_or_UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CustomizationRepo extends JpaRepository<Custom_or_UserPreferences,Integer> {
    List<Custom_or_UserPreferences> findByUserDetails_EmailAddress(String emailAddress);
    @Modifying
    @Transactional
    void deleteByUserDetails_EmailAddress(String emailAddress);
}
