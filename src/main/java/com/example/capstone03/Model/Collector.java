package com.example.capstone03.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Collector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(50) not null")
    @NotEmpty(message = "Name must not be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Column(columnDefinition = "varchar(100) not null unique")
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;

    @Column(columnDefinition = "varchar(255) not null")
    @NotEmpty(message = "Address must not be empty")
    private String address;

    @Column(columnDefinition = "varchar(20) not null unique")
    @NotEmpty(message = "Phone number must not be empty")
    private String phone_number;


    @OneToMany(cascade =CascadeType.ALL ,mappedBy ="collector" )
    private Set<ContainerRequest> container_Requests;

    @OneToMany(cascade =CascadeType.ALL ,mappedBy ="collector" )
    private Set<PickupRequest> Pickup_Request;

    @OneToMany(cascade =CascadeType.ALL ,mappedBy ="collector" )
    private Set<CompanyRequest> Company_Request;




}
