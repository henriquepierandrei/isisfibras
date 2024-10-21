package com.pierandrei.isisfibras.Model.Historic;

import com.pierandrei.isisfibras.Enuns.RolesUsers;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class RoleHistoricChange {
    @Id
    private UUID idAdminOwnerChange;      // ID do administrador

    @Enumerated(EnumType.STRING)
    private RolesUsers oldRolesUsers;       // ROLE antiga

    @Enumerated(EnumType.STRING)
    private RolesUsers newRolesUsers;       // ROLE nova

    private LocalDateTime changeAt;     // Horário da mundança

    private UUID roleUserChanged;       // ID do usuário
}
