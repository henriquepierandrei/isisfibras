package com.pierandrei.isisfibras.Model.EmployeeModels;

import com.pierandrei.isisfibras.Enuns.RolesUsers;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
public class EmployeeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idEmployee;

    @NotBlank
    private String username; // Códgio de acesso

    @NotBlank
    private String password;        // Senha

    @Enumerated(EnumType.STRING)
    private RolesUsers position; // Enum com as posições como SUPPORT, LOGISTICS_MANAGER, etc.

    private String registrationCode; // Código de registro do funcionário


    private boolean isActive; // Indica se o funcionário está ativo

    private LocalTime workStart; // Horário de início do expediente

    private LocalTime workEnd; // Horário de término do expediente

    // Campos adicionais
    private LocalDateTime hiredAt; // Data de contratação


    private double salary; // Salário do funcionário

    @Column(length = 500)
    private String notes; // Notas adicionais sobre o funcionário
}
