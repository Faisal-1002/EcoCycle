package com.example.capstone03.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PointsDTO {
    private double totalWeight;
    private int totalPoints;
    private LocalDate lastPickupDate;

}
