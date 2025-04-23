package com.example.capstone03.Controller;

import com.example.capstone03.Api.ApiResponse;
import com.example.capstone03.Model.ContainerRequest;
import com.example.capstone03.Service.ContainerRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/deliver/collector-id/{collectorId}/container-request-id/{containerRequestId}")
    public ResponseEntity<?> deliverContainer(@PathVariable Integer collectorId, @PathVariable Integer containerRequestId) {
        containerRequestService.deliverContainer(collectorId, containerRequestId);
        return ResponseEntity.status(200).body(new ApiResponse("Container delivered and assigned successfully"));
    }

    @GetMapping("/get-status")
    public ResponseEntity getAllPendingRequests() {
        return ResponseEntity.ok(containerRequestService.getPendingRequests());
    }

    @PutMapping("/accept/container-request-id/{containerRequestId}/collector-id/{collectorId}")
    public ResponseEntity acceptContainerRequest(@PathVariable Integer containerRequestId, @PathVariable Integer collectorId) {
        containerRequestService.acceptContainerRequest(containerRequestId, collectorId);
        return ResponseEntity.ok("Container request accepted successfully");
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getRequestsByStatus(@PathVariable String status) {
        return ResponseEntity.status(200).body(containerRequestService.getRequestsByStatus(status));
    }

    @GetMapping("/collector/{collectorId}")
    public ResponseEntity<?> getRequestsByCollectorId(@PathVariable Integer collectorId) {
        return ResponseEntity.status(200).body(containerRequestService.getRequestsByCollectorId(collectorId));
    }

}
