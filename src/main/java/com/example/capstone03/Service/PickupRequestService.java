package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.PickupRequest;
import com.example.capstone03.Repository.PickupRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PickupRequestService {

    private final PickupRequestRepository pickupRequestRepository;

    public List<PickupRequest> getAllPickupRequests() {
        return pickupRequestRepository.findAll();
    }

    public void addPickupRequest(PickupRequest pickupRequest) {
        pickupRequestRepository.save(pickupRequest);
    }

    public void updatePickupRequest(Integer id, PickupRequest updatedPickupRequest) {
        PickupRequest pickupRequest = pickupRequestRepository.findPickupRequestById(id);
        if (pickupRequest == null) {
            throw new ApiException("Pickup request not found");
        }

        pickupRequest.setUser(updatedPickupRequest.getUser());
//        pickupRequest.setCollector(updatedPickupRequest.getCollector_id());
        pickupRequest.setRequest_date(updatedPickupRequest.getRequest_date());
        pickupRequest.setPickup_date(updatedPickupRequest.getPickup_date());
        pickupRequest.setStatus(updatedPickupRequest.getStatus());

        pickupRequestRepository.save(pickupRequest);
    }

    public void deletePickupRequest(Integer id) {
        PickupRequest pickupRequest = pickupRequestRepository.findPickupRequestById(id);
        if (pickupRequest == null) {
            throw new ApiException("Pickup request not found");
        }

        pickupRequestRepository.delete(pickupRequest);
    }

    public PickupRequest getPickupRequestById(Integer id) {
        PickupRequest pickupRequest = pickupRequestRepository.findPickupRequestById(id);
        if (pickupRequest == null) {
            throw new ApiException("Pickup request not found");
        }
        return pickupRequest;
    }
}
