package com.abhishekkchoudharyy.tradesync.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false,unique = true,length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,length = 50)
    private String role;
}
