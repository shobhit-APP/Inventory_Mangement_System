package com.example.StockStick.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="Contact")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contactId")
    private int contactId;
    @Column(name="messages")
    private String messages;
    @Column(name="username")
    private String username;
    @Column (name="emailAddress")
    private String emailAddress;

}
