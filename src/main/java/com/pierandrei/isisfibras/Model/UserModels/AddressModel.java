package com.pierandrei.isisfibras.Model.UserModels;

import com.pierandrei.isisfibras.Enuns.TypeLocationEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class AddressModel {

    @Id
    private UUID idUser;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String cep;

    @NotBlank
    private String road;

    @NotBlank
    private String neighborhood;

    @NotBlank
    private String complement;

    @NotBlank
    private String houseNumber;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private TypeLocationEnum  typeLocationEnum;
}
