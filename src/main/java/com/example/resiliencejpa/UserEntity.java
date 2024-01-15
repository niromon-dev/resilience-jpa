package com.example.resiliencejpa;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String username;
    String lastname;
    String forname;

}
