package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.PickupRequest;
import com.example.capstone03.Model.RecycleItem;
import com.example.capstone03.Model.User;
import com.example.capstone03.Repository.PickupRequestRepository;
import com.example.capstone03.Repository.RecycleItemRepository;
import com.example.capstone03.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void addRecycleItem(RecycleItem recycleItem) {

        recycleItemRepository.save(recycleItem);
    }

    // 10. System awards points (1kg = 1 point) - automatic
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
        user.setPoints(user.getPoints() + points);
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

    public RecycleItem getRecycleItemById(Integer id) {
        RecycleItem recycleItem = recycleItemRepository.findRecycleItemById(id);
        if (recycleItem == null) {
            throw new ApiException("Recycle item not found");
        }
        return recycleItem;
    }
    //endpoint 12 - User can view points history
    public List<RecycleItem> getPointsHistory(Integer userId) {
        return recycleItemRepository.findByPickup_requestUserId(userId);
    }


}
