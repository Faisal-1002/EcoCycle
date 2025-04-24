package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.DTO.PointsDTO;
import com.example.capstone03.Model.PickupRequest;
import com.example.capstone03.Model.RecycleItem;
import com.example.capstone03.Model.User;
import com.example.capstone03.Repository.PickupRequestRepository;
import com.example.capstone03.Repository.RecycleItemRepository;
import com.example.capstone03.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecycleItemService {

    private final RecycleItemRepository recycleItemRepository;
    private final PickupRequestRepository pickupRequestRepository;
    private final UserRepository userRepository;

    public List<RecycleItem> getAllRecycleItems() {
        return recycleItemRepository.findAll();
    }

    // 26. Add items to pick up request - Abeer
    public void addRecycleItemToPickupRequest(Integer pickupRequestId, RecycleItem recycleItem) {
        PickupRequest pickupRequest = pickupRequestRepository.findPickupRequestById(pickupRequestId);
        if (pickupRequest == null){
                throw new ApiException("Pickup request not found");
        }
        if (!pickupRequest.getStatus().equalsIgnoreCase("processing")){
            throw new ApiException("Pickup request is not processing");
        }
        recycleItem.setStatus("picked");
        recycleItem.setPickup_request(pickupRequest);
        recycleItemRepository.save(recycleItem);
    }

    public void updateRecycleItem(Integer id, RecycleItem updatedRecycleItem) {
        RecycleItem recycleItem = recycleItemRepository.findRecycleItemById(id);
        if (recycleItem == null) {
            throw new ApiException("Recycle item not found");
        }

        recycleItem.setPickup_request(updatedRecycleItem.getPickup_request());
        recycleItem.setType(updatedRecycleItem.getType());
        recycleItem.setWeight_kg(updatedRecycleItem.getWeight_kg());

        recycleItemRepository.save(recycleItem);
    }

    public void deleteRecycleItem(Integer id) {
        RecycleItem recycleItem = recycleItemRepository.findRecycleItemById(id);
        if (recycleItem == null) {
            throw new ApiException("Recycle item not found");
        }

        recycleItemRepository.delete(recycleItem);
    }

    // 27. get by ID - Abeer
    public RecycleItem getRecycleItemById(Integer id) {
        RecycleItem recycleItem = recycleItemRepository.findRecycleItemById(id);
        if (recycleItem == null) {
            throw new ApiException("Recycle item not found");
        }
        return recycleItem;
    }
  
    // 28. User can view points history - Abeer
    public PointsDTO getPointsHistory(Integer userId) {
        List<RecycleItem> items = recycleItemRepository.findRecycleItemsByUserId(userId);

        double totalWeight = 0;
        LocalDate lastPickupDate = null;

        for (RecycleItem item : items) {
            if (item.getPickup_request() != null) {
                String status = item.getPickup_request().getStatus();
                System.out.println("Item status: " + status);

                if ("PickedUp".equalsIgnoreCase(status)) {
                    totalWeight += item.getWeight_kg();


                    LocalDate pickupDate = item.getPickup_request().getPickup_date();
                    System.out.println("Pickup date found: " + pickupDate);

                    if (lastPickupDate == null || pickupDate.isAfter(lastPickupDate)) {
                        lastPickupDate = pickupDate;
                    }
                }
            }
        }

            int points = (int) totalWeight;
            return new PointsDTO(totalWeight, points, lastPickupDate);

    }

}

