package com.example.StockStick.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
@Entity(name="UserPlanData")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPlanData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(name="planId")
    private String planId;
    @Column(name="planType")
    private String planType;
    @Column(name="selectedDate")
    private LocalDate selectedDate;
    @Column(name="expiryDate")
    private LocalDate expiryDate;
    @ManyToOne
    @JoinColumn(name = "userId",referencedColumnName = "Id")
    @ToString.Exclude
    private UserDetails userDetails;

}
