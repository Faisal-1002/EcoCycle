package com.example.capstone03.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContainerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "date not null")
    @NotNull(message = "Request date must not be null")
    private LocalDateTime request_date;

    @Column(columnDefinition = "date")
    private LocalDateTime delivery_date;

    @Column(columnDefinition = "varchar(20) not null")
    @NotNull(message = "Status must not be null")
    private String status;

    @Column(columnDefinition = "varchar(255)")
    private String issue_notes;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull(message = "User must not be null")
    @JsonIgnore
    private User user;

//    @ManyToOne
//    @JoinColumn(name = "collector_id", referencedColumnName = "id")
//    private Recycler collector;
//
//    @ManyToOne
//    @JoinColumn(name = "container_id", referencedColumnName = "id")
//    private Container container;
}
