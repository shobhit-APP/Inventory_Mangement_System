package com.example.StockStick.Service;

import com.example.StockStick.AllException.CustomException;
import com.example.StockStick.Model.Items;
import com.example.StockStick.Model.UserDetails;
import com.example.StockStick.Model.UserRegistrationDTO;
import com.example.StockStick.Repositery.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private ItemsService ItemService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Transactional
    public UserDetails register(UserRegistrationDTO userRegistrationDTO) {
        UserDetails User=new UserDetails();
        User.setFirstName(userRegistrationDTO.getFirstName());
        User.setLastName(userRegistrationDTO.getLastName());
        User.setEmailAddress(userRegistrationDTO.getEmailAddress());
        User.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        User.setConfirmPassword(passwordEncoder.encode(userRegistrationDTO.getConfirmPassword()));
        User.setCompany(userRegistrationDTO.getCompany());
        User.setCountry(userRegistrationDTO.getCountry());
        User.setState(userRegistrationDTO.getState());
        User.setContactNo(userRegistrationDTO.getContactNo());
        User.setBio(userRegistrationDTO.getBio());
        User.setUsertype(userRegistrationDTO.getUsertype());
        User.setRDate(LocalDate.now());
        userRepo.save(User);
        return User;
    }
    @Transactional
    public UserDetails FindByEmailAddress(String emailAddress) {
        return  userRepo.findByEmailAddress(emailAddress);
    }
    @Transactional
    public void deleteUserByEmailAddress(String emailAddress) {
        userRepo.deleteUserByEmailAddress(emailAddress);
    }
    @Transactional
    public void UpdateUser(UserDetails userDetails, UserRegistrationDTO userRegistrationDTO) {
        // Update UserDetails fields
        userDetails.setFirstName(userRegistrationDTO.getFirstName());
        userDetails.setLastName(userRegistrationDTO.getLastName());
        userDetails.setEmailAddress(userRegistrationDTO.getEmailAddress());
        userDetails.setCountry(userRegistrationDTO.getCountry());
        userDetails.setContactNo(userRegistrationDTO.getContactNo());
        userDetails.setBio(userRegistrationDTO.getBio());
        userDetails.setState(userRegistrationDTO.getState());
        userDetails.setCompany(userRegistrationDTO.getCompany());
        userDetails.setUsertype(userRegistrationDTO.getUsertype());
        userDetails.setUDate(LocalDate.now());
        userDetails.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        userDetails.setConfirmPassword(passwordEncoder.encode(userRegistrationDTO.getConfirmPassword()));

        // Save UserDetails first to ensure it is persisted
        userRepo.save(userDetails);
    }

    @Transactional
    public void UpdateUserWithoutChangingEmailAndId(UserDetails userDetails, UserRegistrationDTO userRegistrationDTO) {
        userDetails.setFirstName(userRegistrationDTO.getFirstName());
        userDetails.setLastName(userRegistrationDTO.getLastName());
        userDetails.setEmailAddress(userRegistrationDTO.getEmailAddress());
        userDetails.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        userDetails.setConfirmPassword(passwordEncoder.encode(userRegistrationDTO.getConfirmPassword()));
        userDetails.setCountry(userRegistrationDTO.getCountry());
        userDetails.setContactNo(userRegistrationDTO.getContactNo());
        userDetails.setState(userRegistrationDTO.getState());
        userDetails.setCompany(userRegistrationDTO.getCompany());
        userDetails.setBio(userRegistrationDTO.getBio());
        userDetails.setUDate(LocalDate.now());
        userDetails.setUsertype(userRegistrationDTO.getUsertype());
        userRepo.save(userDetails);
    }

    public void UpdateUser1(UserDetails userDetails, UserRegistrationDTO userRegistrationDTO) {
        userDetails.setFirstName(userRegistrationDTO.getFirstName());
        userDetails.setLastName(userRegistrationDTO.getLastName());
        userDetails.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        userDetails.setConfirmPassword(passwordEncoder.encode(userRegistrationDTO.getConfirmPassword()));
        userDetails.setCountry(userRegistrationDTO.getCountry());
        userDetails.setContactNo(userRegistrationDTO.getContactNo());
        userDetails.setState(userRegistrationDTO.getState());
        userDetails.setCompany(userRegistrationDTO.getCompany());
        userDetails.setBio(userRegistrationDTO.getBio());
        userDetails.setUsertype(userRegistrationDTO.getUsertype());
        userDetails.setUDate(LocalDate.now());
        userRepo.save(userDetails);
    }

    public UserDetails FindById(Long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new CustomException("Sorry , User There is No User With this User Id Please Try Again"));
    }
}
