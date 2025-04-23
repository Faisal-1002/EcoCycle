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

    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addPickupRequest(@PathVariable Integer userId, @RequestBody @Valid PickupRequest pickupRequest) {
        pickupRequestService.addPickupRequest(userId,pickupRequest);
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

    @PutMapping("/accept-pickup/{pickupRequestId}/{collectorId}")
    public ResponseEntity<?> acceptPickupRequest(@PathVariable Integer pickupRequestId,
                                                 @PathVariable Integer collectorId) {
        pickupRequestService.acceptPickupRequest(pickupRequestId, collectorId);
        return ResponseEntity.status(200).body(new ApiResponse("Pickup request accepted successfully"));
    }

    // endpoint 16 - View assigned pickup requests
    @GetMapping("/assigned/{collectorId}")
    public ResponseEntity getAssignedPickupRequests(@PathVariable Integer collectorId) {
        return ResponseEntity.ok(pickupRequestService.getAssignedPickupRequests(collectorId));
    }

    //endpoint 11- Notify user: Pickup completed + Points updated
    @PostMapping("/pickup-completed/{userId}")
    public ResponseEntity<String> notifyAndComplete(@PathVariable Integer userId) {
        String result = pickupRequestService.completePickupAndNotify(userId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/pickedUp-request/{pickedUpId}/collector/{collectorId}")
    public ResponseEntity<ApiResponse> deliverRecyclingCompanyRequest(@PathVariable Integer pickedUpId,@PathVariable Integer collectorId) {
        pickupRequestService.pickedUpRequest(pickedUpId,collectorId);
        return ResponseEntity.status(200).body(new ApiResponse("pickup request been picked up"));
    }

}
