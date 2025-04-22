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
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Recycler_Id number must be not empty")
    private Integer recycler_Id;
  
    @Column(columnDefinition = "int not null")
    @NotNull(message = "quantity must be not empty")
    private Integer quantity;

    @Column(columnDefinition = "varchar(20) not null")
    @Pattern(regexp = "Requested|Auto-Requested|PickedUp|Delivered",
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
