package com.pierandrei.isisfibras.Model.UserModels;

import com.pierandrei.isisfibras.Enuns.RolesUsers;
import com.pierandrei.isisfibras.Model.LogisticModels.OrdersModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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


    @NotBlank
    private LocalDate dateBorn;       // Idade

    @Column(unique = true)
    @NotBlank
    private String cpf;

    @NotBlank
    private String name;  // Nome completo do usuário

    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;

    private boolean receivePromotions;

    private boolean isActive;  // Flag para indicar se a conta está ativa

    private boolean isBanned;  // Flag para indicar se o usuário está banido

    @ManyToOne
    private AddressModel addressModel;

    @OneToOne
    private CartModel cartModel;

    @OneToMany
    private List<OrdersModel> orders;  // Relacionamento com histórico de pedidos

    @Enumerated(EnumType.STRING)
    private RolesUsers rolesUsers;

}
