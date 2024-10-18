package com.pierandrei.isisfibras.Model.UserModels;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @Column(unique = true)
    @NotBlank
    private String phone;

    @Column(unique = true)
    @NotBlank
    private String cpf;

    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;

    boolean receivePromotions;


}
