package com.example.capstone03.Controller;

import com.example.capstone03.Api.ApiResponse;
import com.example.capstone03.Model.ContainerRequest;
import com.example.capstone03.Service.ContainerRequestService;
import com.example.capstone03.Service.ContainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/container-request")
@RequiredArgsConstructor
public class ContainerRequestController {

    private final ContainerRequestService containerRequestService;


    @GetMapping("/get")
    public ResponseEntity<?> getAllContainerRequests() {
        return ResponseEntity.status(200).body(containerRequestService.getAllContainerRequests());
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addContainerRequest(@PathVariable Integer userId ,@RequestBody @Valid ContainerRequest containerRequest) {
        containerRequestService.addContainerRequest(userId,containerRequest);
        return ResponseEntity.status(200).body(new ApiResponse("Container request added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateContainerRequest(@PathVariable Integer id, @RequestBody @Valid ContainerRequest containerRequest) {
        containerRequestService.updateContainerRequest(id, containerRequest);
        return ResponseEntity.status(200).body(new ApiResponse("Container request updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContainerRequest(@PathVariable Integer id) {
        containerRequestService.deleteContainerRequest(id);
        return ResponseEntity.status(200).body(new ApiResponse("Container request deleted successfully"));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getContainerRequestById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(containerRequestService.getContainerRequestById(id));
    }

    @PutMapping("/deliver/{containerRequestId}")
    public ResponseEntity<?> deliverContainer(@PathVariable Integer containerRequestId) {
        containerRequestService.deliverContainer(containerRequestId);
        return ResponseEntity.status(200).body(new ApiResponse("Container delivered and assigned successfully"));
    }

    //endpoint 13 - View all container requests
    @GetMapping("/get-status")
    public ResponseEntity getAllPendingRequests() {
        return ResponseEntity.ok(containerRequestService.getPendingRequests());
    }
}
