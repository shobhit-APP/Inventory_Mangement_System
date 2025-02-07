package com.example.StockStick.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "Custom_or_UserPreferences")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Custom_or_UserPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customizationId")
    private int id;
    @Column(name="highStockThreshold")
    private int highStockThreshold;
    @Column(name="mediumStockThreshold")
    private int  mediumStockThreshold;
    @Column(name="lowStockThreshold")
    private int lowStockThreshold;
    @Column(name="highStockColor")
    private String highStockColor;
    @Column(name="lowStockColor")
    private String lowStockColor;
    @Column(name="mediumStockColor")
    private String mediumStockColor;
    @Column(name="emptyStockColor")
    private String emptyStockColor;
    @ManyToOne
    @JoinColumn(name = "userId",referencedColumnName = "Id")
    @ToString.Exclude
    private UserDetails userDetails;


}
