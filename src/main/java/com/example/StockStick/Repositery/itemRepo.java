package com.example.StockStick.Repositery;

import com.example.StockStick.Model.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface itemRepo  extends JpaRepository<Items,Integer> {
    List<Items> findByUserDetailsId(Long Id);
    @Modifying
    @Transactional
    void deleteByUserDetailsId(Long Id);
}
