package com.example.StockStick.Repositery;

import com.example.StockStick.Model.UserPlanData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserPlanRepo extends JpaRepository<UserPlanData,Long> {
    List<UserPlanData> findByUserDetails_Id(Long Id);
    @Modifying
    @Transactional
    void deleteByUserDetails_Id(Long Id);
}