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

    public void addRecycleItem(Integer pickupRequestId, RecycleItem recycleItem) {
        PickupRequest pickupRequest = pickupRequestRepository.findPickupRequestById(pickupRequestId);
        if (pickupRequest == null) {
            throw new ApiException("Pickup request not found");
        }

        recycleItem.setStatus("processed");
        recycleItem.setPickup_request(pickupRequest);
        recycleItemRepository.save(recycleItem);
    }

    // 26. System awards points (1kg = 1 point) - automatic
    public void addRecycleItemToPickupRequest(Integer pickupRequestId, RecycleItem recycleItem) {
        PickupRequest pickupRequest = pickupRequestRepository.findPickupRequestById(pickupRequestId);
        if (pickupRequest == null){
                throw new ApiException("Pickup request not found");
        }
        recycleItem.setPickup_request(pickupRequest);
        recycleItemRepository.save(recycleItem);

        // update points
        User user = pickupRequest.getUser();
        int points = recycleItem.getWeight_kg().intValue();
        user.setPoints(points);
        userRepository.save(user);
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

    // 27. get by ID
    public RecycleItem getRecycleItemById(Integer id) {
        RecycleItem recycleItem = recycleItemRepository.findRecycleItemById(id);
        if (recycleItem == null) {
            throw new ApiException("Recycle item not found");
        }
        return recycleItem;
    }
  
    //endpoint 12 - User can view points history
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

