package com.example.capstone03.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContainerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "date")
    private LocalDate request_date;

    @Column(columnDefinition = "date")
    private LocalDate delivery_date;

    @Column(columnDefinition = "varchar(20)")
    @Pattern(regexp = "^(pending|delivered|rejected|cancelled)")
    private String status;

    @Column(columnDefinition = "varchar(255)")
    private String issue_notes;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "collector_id", referencedColumnName = "id")
    @JsonIgnore
    private Collector collector;

    @ManyToOne
    @JoinColumn(name = "container_id", referencedColumnName = "id")
    private Container container;
}
