package com.example.capstone03.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Recycler_Id number must be not empty")
    private Integer recycler_id;


    @Column(columnDefinition = "int not null")
    @NotNull(message = "quantity must be not empty")
    private Integer quantity;

    @ManyToOne
    @JsonIgnore
    private RecyclingCompany recycling_company;

    @ManyToOne
    @JoinColumn(name = "collector_id", referencedColumnName = "id")
    @JsonIgnore
    private Collector collector;
}
