package com.example.capstone03.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecycleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(30) not null")
    @NotEmpty(message = "Type must not be empty")
    private String type;

    @Column(columnDefinition = "double not null")
    @NotNull(message = "Weight must not be null")
    private Double weight_kg;

    @ManyToOne
    @JoinColumn(name = "pickup_request_id", referencedColumnName = "id")
    @JsonIgnore
    private PickupRequest pickup_request;
}
