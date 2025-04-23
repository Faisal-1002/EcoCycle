package com.example.capstone03.Controller;

import com.example.capstone03.Api.ApiResponse;
import com.example.capstone03.Model.Collector;
import com.example.capstone03.Service.CollectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/collector")
@RequiredArgsConstructor
public class CollectorController {

    private final CollectorService collectorService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllCollectors() {
        return ResponseEntity.status(200).body(collectorService.getAllCollectors());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCollector(@RequestBody @Valid Collector collector) {
        collectorService.addCollector(collector);
        return ResponseEntity.status(200).body(new ApiResponse("Collector added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCollector(@PathVariable Integer id, @RequestBody @Valid Collector collector) {
        collectorService.updateCollector(id, collector);
        return ResponseEntity.status(200).body(new ApiResponse("Collector updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCollector(@PathVariable Integer id) {
        collectorService.deleteCollector(id);
        return ResponseEntity.status(200).body(new ApiResponse("Collector deleted successfully"));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getCollectorById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(collectorService.getCollectorById(id));
    }

    @GetMapping("/assigned-containers/{collectorId}")
    public ResponseEntity<?> getAssignedContainerRequests(@PathVariable Integer collectorId) {
        return ResponseEntity.status(200).body(collectorService.getAssignedContainerRequests(collectorId));
    }

    @GetMapping("/assigned-pickups/{collectorId}")
    public ResponseEntity<?> getAssignedPickupRequests(@PathVariable Integer collectorId) {
        return ResponseEntity.status(200).body(collectorService.getAssignedPickupRequests(collectorId));
    }
}
