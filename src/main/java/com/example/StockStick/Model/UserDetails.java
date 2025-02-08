package com.example.StockStick.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity(name="UserDetails")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long Id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "contactNo")
    private Long contactNo;

    @Column(name = "emailAddress" ,unique = true ,nullable = false)
    private String emailAddress;

    @Column(name = "password")
    private String password;

    @Column(name = "confirmPassword")
    private String confirmPassword;

    @Column(name = "company")
    private String company;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "Usertype")
    private String Usertype;

    @Column (name ="RDate")
    private LocalDate RDate;
    @Column (name="UDate")
    private LocalDate UDate;
    @Column(name = "Bio", columnDefinition = "TEXT")
    private String Bio;
    @OneToMany(mappedBy = "userDetails", cascade =CascadeType.ALL)
    private List<Items> items;
    @OneToMany(mappedBy = "userDetails" ,cascade = CascadeType.ALL)
    //one user can have many plans
    @ToString.Exclude
    private List<UserPlanData> userPlanData;
  


}
