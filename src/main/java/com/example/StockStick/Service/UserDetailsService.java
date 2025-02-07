package com.example.StockStick.Service;

import com.example.StockStick.Model.UserPrinciple;
import com.example.StockStick.Repositery.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        System.out.println("Searching for user with email Address: " + emailAddress);
        com.example.StockStick.Model.UserDetails user = userRepo.findByEmailAddress(emailAddress);
        if (user == null) {
            System.out.println("User not found for email Address: " + emailAddress);
            throw new UsernameNotFoundException("User Not found");
        } else {
            System.out.println("User found: " + user.getEmailAddress());
            return new UserPrinciple(user);
        }
    }
}
