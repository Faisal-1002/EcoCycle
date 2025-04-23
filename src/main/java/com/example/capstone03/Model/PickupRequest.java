package com.example.capstone03.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickupRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "date")
    private LocalDate request_date;

    @Column(columnDefinition = "date")
    private LocalDate pickup_date;

    @Column(columnDefinition = "varchar(20) not null")
    @Pattern(regexp = "Requested|Auto-Requested|Processing|PickedUp",
            message = "Status must be one of: Requested, Auto-Requested, PickedUp, Delivered")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "pickup_request", cascade = CascadeType.ALL)
    private List<RecycleItem> recycleItems;

    @ManyToOne
    @JoinColumn(name = "collector_id", referencedColumnName = "id")
    @JsonIgnore
    private Collector collector;
}
