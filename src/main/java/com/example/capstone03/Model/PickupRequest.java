package com.example.capstone03.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PickupRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime request_date;

    @Column(columnDefinition = "date")
    private LocalDate pickup_date;

    @Column(columnDefinition = "varchar(20) not null")
    @Pattern(regexp = "requested|auto_requested|processing|pickedup",
            message = "Status must be one of: requested, auto_requested, pickedup, processing")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "pickup_request", cascade = CascadeType.ALL)
    private List<RecycleItem> recycle_items;

    @ManyToOne
    @JoinColumn(name = "collector_id", referencedColumnName = "id")
    private Collector collector;
}
