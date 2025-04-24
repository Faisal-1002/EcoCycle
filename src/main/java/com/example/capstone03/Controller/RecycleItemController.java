package com.example.capstone03.Controller;

import com.example.capstone03.Api.ApiResponse;
import com.example.capstone03.Model.RecycleItem;
import com.example.capstone03.Service.RecycleItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recycle-item")
@RequiredArgsConstructor
public class RecycleItemController {

    private final RecycleItemService recycleItemService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllRecycleItems() {
        return ResponseEntity.status(200).body(recycleItemService.getAllRecycleItems());
    }

    @PostMapping("/add/{pickupRequestId}")
    public ResponseEntity addRecycleItemToPickupRequest(@PathVariable Integer pickupRequestId,@RequestBody @Valid  RecycleItem recycleItem) {
        recycleItemService.addRecycleItemToPickupRequest(pickupRequestId,recycleItem);
        return ResponseEntity.status(200).body(new ApiResponse("Recycle item added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRecycleItem(@PathVariable Integer id, @RequestBody @Valid RecycleItem recycleItem) {
        recycleItemService.updateRecycleItem(id, recycleItem);
        return ResponseEntity.status(200).body(new ApiResponse("Recycle item updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRecycleItem(@PathVariable Integer id) {
        recycleItemService.deleteRecycleItem(id);
        return ResponseEntity.status(200).body(new ApiResponse("Recycle item deleted successfully"));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getRecycleItemById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(recycleItemService.getRecycleItemById(id));
    }

    @GetMapping("/get-point/{userId}")
    public ResponseEntity<?> getPointsHistory(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(recycleItemService.getPointsHistory(userId));
    }

}
