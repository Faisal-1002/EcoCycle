package com.example.capstone03.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RecyclingCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(20) not null")
    @NotNull(message = "name must be not empty")
    private String name;

    @Column(columnDefinition = "varchar(20) not null")
    @NotEmpty(message = "phone number must be not empty")
    private String phone_number;

    @Column(columnDefinition = "varchar(20) not null")
    @NotEmpty(message = "Basic_activity must be not empty")
    private String basic_activity;

    @Column(columnDefinition = "varchar(20) not null")
    @NotEmpty(message = "zone must be not empty")
    private String zone;

    @Column(columnDefinition = "varchar(20) not null")
    @NotEmpty(message = "email must be not empty")
    private String email;

    @Column(columnDefinition = "varchar(20) not null")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Password must be at least 8 characters and include uppercase, lowercase, and a number")
    @NotEmpty(message = "password cannot be empty")
    private String password;

    @Column(columnDefinition = "varchar(20) not null")
    @NotEmpty(message = "Certification must be not empty")
    private String certification;

    @Column(columnDefinition = "date")
    private LocalDate permit_expiration;

    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "recycling_company")
    private Set<CompanyRequest>company_request;
}
