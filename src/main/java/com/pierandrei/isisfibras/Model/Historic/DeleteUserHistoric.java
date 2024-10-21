package com.pierandrei.isisfibras.Model.Historic;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class DeleteUserHistoric {
    @Id
    private UUID idAdminOwner;    // ID do administrador

    private UUID idUserDeleted;     // ID do user deletado

    private LocalDateTime deletedAt;    // Horário da remoção


}
