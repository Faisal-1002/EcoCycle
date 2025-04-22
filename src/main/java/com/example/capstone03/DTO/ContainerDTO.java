package com.example.capstone03.DTO;


import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContainerDTO {

    @Id
    private Integer user_id;

    @NotNull
    private Boolean is_available;

}
