package com.example.capstone03.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^(plastic|paper|metal|All)$", message = "Type must be one of: plastic, paper, metal, All")
    private String type;

    @Column(columnDefinition = "double not null")
    @NotNull(message = "Weight must not be null")
    private Double weight_kg;

    @Column(columnDefinition = "varchar(30) not null")
    @Pattern(regexp = "^(processed|delivered)$", message = "Type must be one of: processed, delivered")
    private String status;

    @ManyToOne
    @JoinColumn(name = "pickup_request_id", referencedColumnName = "id")
    @JsonIgnore
    private PickupRequest pickup_request;
}
