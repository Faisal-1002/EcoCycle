package com.example.capstone03.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Container {

    @Id
    private Integer id;

    @NotNull
    private Boolean is_available;

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;
}
