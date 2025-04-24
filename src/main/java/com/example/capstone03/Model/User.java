package com.example.capstone03.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
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

    @Column(columnDefinition = "varchar(20) not null")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Password must be at least 8 characters and include uppercase, lowercase, and a number")
    @NotEmpty(message = "password cannot be empty")
    private String password;

    @Column(columnDefinition = "varchar(255) not null")
    @NotEmpty(message = "Address must not be empty")
    private String address;

    @Column(columnDefinition = "varchar(20) not null unique")
    @NotEmpty(message = "Phone number must not be empty")
    private String phone_number;

    @Column(columnDefinition = "int")
    private Double points;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ContainerRequest> containerRequests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PickupRequest> pickupRequests;

}
