package com.example.capstone03.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class CompanyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(10) not null")
    @NotNull(message = "Type must be not empty ,and must be one of: plastic, paper, metal, All")
    @Pattern(regexp = "^(plastic|paper|metal|All)$", message = "Type must be one of: plastic, paper, metal, All")
    private String type;


    @Column(columnDefinition = "int not null")
    @NotNull(message = "quantity must be not empty")
    private Integer quantity;

    @Column(columnDefinition = "varchar(10) not null")
    @Pattern(regexp = "Requested|Auto-Requested|Processing|PickedUp|Delivered",
            message = "Status must be one of: Requested, Auto-Requested, PickedUp, Delivered")
    private String status;

    @ManyToOne
    @JsonIgnore
    private RecyclingCompany recycling_company;

    @ManyToOne
    @JoinColumn(name = "collector_id", referencedColumnName = "id")
    @JsonIgnore
    private Collector collector;
}
