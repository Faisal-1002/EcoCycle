package com.example.capstone03.Controller;

import com.example.capstone03.Api.ApiResponse;
import com.example.capstone03.Model.Container;
import com.example.capstone03.Service.ContainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/container")
@RequiredArgsConstructor
public class ContainerController {

    private final ContainerService containerService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllContainers() {
        return ResponseEntity.status(200).body(containerService.getAllContainers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addContainer(@RequestBody @Valid Container container) {
        containerService.addContainer(container);
        return ResponseEntity.status(200).body(new ApiResponse("Container added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateContainer(@PathVariable Integer id, @RequestBody @Valid Container container) {
        containerService.updateContainer(id, container);
        return ResponseEntity.status(200).body(new ApiResponse("Container updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContainer(@PathVariable Integer id) {
        containerService.deleteContainer(id);
        return ResponseEntity.status(200).body(new ApiResponse("Container deleted successfully"));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getContainerById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(containerService.getContainerById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableContainers() {
        return ResponseEntity.status(200).body(containerService.getAvailableContainers());
    }
}
