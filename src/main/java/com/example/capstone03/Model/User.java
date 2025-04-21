package com.example.capstone03.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

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

    @Column(columnDefinition = "varchar(100) not null")
    @NotEmpty(message = "Password must not be empty")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @Column(columnDefinition = "varchar(255) not null")
    @NotEmpty(message = "Address must not be empty")
    private String address;

    @Column(columnDefinition = "varchar(20) not null unique")
    @NotEmpty(message = "Phone number must not be empty")
    private String phone_number;

    @Column(columnDefinition = "int not null default 0")
    @NotNull(message = "Points must not be null")
    private Integer points;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    private Container container;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ContainerRequest> containerRequests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PickupRequest> pickupRequests;
}
