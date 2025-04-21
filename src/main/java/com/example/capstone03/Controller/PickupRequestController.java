package com.example.capstone03.Controller;

import com.example.capstone03.Api.ApiResponse;
import com.example.capstone03.Model.PickupRequest;
import com.example.capstone03.Service.PickupRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pickup-request")
@RequiredArgsConstructor
public class PickupRequestController {

    private final PickupRequestService pickupRequestService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllPickupRequests() {
        return ResponseEntity.status(200).body(pickupRequestService.getAllPickupRequests());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPickupRequest(@RequestBody @Valid PickupRequest pickupRequest) {
        pickupRequestService.addPickupRequest(pickupRequest);
        return ResponseEntity.status(200).body(new ApiResponse("Pickup request added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePickupRequest(@PathVariable Integer id, @RequestBody @Valid PickupRequest pickupRequest) {
        pickupRequestService.updatePickupRequest(id, pickupRequest);
        return ResponseEntity.status(200).body(new ApiResponse("Pickup request updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePickupRequest(@PathVariable Integer id) {
        pickupRequestService.deletePickupRequest(id);
        return ResponseEntity.status(200).body(new ApiResponse("Pickup request deleted successfully"));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getPickupRequestById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(pickupRequestService.getPickupRequestById(id));
    }
}
