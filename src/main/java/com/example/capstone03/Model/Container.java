package com.example.capstone03.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Container {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "boolean")
    private Boolean available;

    @NotEmpty
    @Column(columnDefinition = "varchar(100) not null")
    private String location;

    @NotNull(message = "Status condition must not be null")
    @Pattern(regexp = "new|damaged", message = "Status condition must be new or damaged")
    @Column(columnDefinition = "varchar(20) not null")
    private String status_condition;

    @OneToMany(mappedBy = "container")
    @JsonIgnore
    private List<ContainerRequest> containerRequests;
}
