package com.pierandrei.isisfibras.Model.EmployeeModels;

import com.pierandrei.isisfibras.Enuns.RolesEmployee;
import com.pierandrei.isisfibras.Enuns.StatusEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class ShippingRecordModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // Identificador único do registro de envio

    private String nfeCode; // Código da Nota Fiscal Eletrônica

    private String orderCode; // Código do pedido

    private LocalDateTime sentAt; // Data e hora de envio

    @Enumerated(EnumType.STRING)
    private StatusEnum status; // Status do envio (PENDING, PREPARING, SENT, DELIVERED, CANCELED)

    private String registrationCodeEmployee; // Código de registro do funcionário responsável pelo envio

    @Enumerated(EnumType.STRING)
    private RolesEmployee rolesEmployee; // Cargo específico

    // Campos adicionais
    private LocalDateTime deliveredAt; // Data e hora em que o pedido foi entregue

    private String trackingNumber; // Número de rastreamento do envio

    private String carrier; // Transportadora responsável pela entrega

    private String notes; // Notas adicionais sobre o envio
}
