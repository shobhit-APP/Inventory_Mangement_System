package com.example.StockStick.Repositery;

import com.example.StockStick.Model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepo extends JpaRepository<UserDetails,Long> {
    UserDetails findByEmailAddress(String emailAddress);
    @Modifying
    @Transactional
    void deleteUserByEmailAddress(String emailAddress);

}
