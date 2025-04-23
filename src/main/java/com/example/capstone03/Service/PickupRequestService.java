package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.Collector;
import com.example.capstone03.Model.ContainerRequest;
import com.example.capstone03.Model.PickupRequest;
import com.example.capstone03.Model.User;
import com.example.capstone03.Repository.CollectorRepository;
import com.example.capstone03.Repository.PickupRequestRepository;
import com.example.capstone03.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PickupRequestService {

    private final PickupRequestRepository pickupRequestRepository;
    private final CollectorRepository collectorRepository;
    private final UserRepository userRepository;

    public List<PickupRequest> getAllPickupRequests() {
        return pickupRequestRepository.findAll();
    }

    public void addPickupRequest(Integer userId,PickupRequest pickupRequest) {
        User user = userRepository.findUserById(userId);

        if (user == null){
            throw new ApiException("user not found");
        }

        List<PickupRequest> requests = pickupRequestRepository.findAll();
        for (PickupRequest request : requests){
            if (request.getUser().getId().equals(userId)  && request.getStatus().equals("Requested")){
                throw new ApiException("user have already requested a pick up");
            }
        }

        pickupRequest.setUser(user);
        pickupRequest.setRequest_date(LocalDate.now());
        pickupRequest.setPickup_date(LocalDate.now().plusDays(1));
        pickupRequest.setStatus("Requested");
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



    //==============================

    //17 Accept a pickup

    public void acceptPickup(Integer pickupId, Integer collectorId){
        PickupRequest pickupRequest = pickupRequestRepository.findPickupRequestById(pickupId);
        Collector collector = collectorRepository.findCollectorById(collectorId);

        if (pickupRequest == null) {
            throw new ApiException("Pickup request not found");
        }
        if (collector == null){
            throw new ApiException("collector not found");
        }

        List<PickupRequest> requests = pickupRequestRepository.findAll();
        for (PickupRequest request : requests){
            if (request.getStatus().equals("Processing")){
                throw new ApiException("Pickup Request been accepted already By "+ request.getCollector().getName());
            }
        }

        pickupRequest.setCollector(collector);
        pickupRequest.setStatus("Processing");
        pickupRequestRepository.save(pickupRequest);
    }
}
