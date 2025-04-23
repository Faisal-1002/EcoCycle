package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.Collector;
import com.example.capstone03.Model.ContainerRequest;
import com.example.capstone03.Model.PickupRequest;
import com.example.capstone03.Model.RecycleItem;
import com.example.capstone03.Model.User;
import com.example.capstone03.Repository.CollectorRepository;
import com.example.capstone03.Repository.ContainerRequestRepository;
import com.example.capstone03.Repository.PickupRequestRepository;
import com.example.capstone03.Repository.RecycleItemRepository;
import com.example.capstone03.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PickupRequestService {

    private final PickupRequestRepository pickupRequestRepository;
    private final CollectorRepository collectorRepository;
    private final RecycleItemRepository recycleItemRepository;
    private final UserRepository userRepository;
    private final ContainerRequestRepository containerRequestRepository;
    private final JavaMailSender mailSender;

    public List<PickupRequest> getAllPickupRequests() {
        return pickupRequestRepository.findAll();
    }

    //5. Request pickup manually
    public void addPickupRequest(Integer userId, PickupRequest pickupRequest) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }

        pickupRequest.setUser(user);
        pickupRequest.setRequest_date(LocalDateTime.now());
        pickupRequest.setStatus("requested");
        pickupRequest.setPickup_date(LocalDate.now().plusDays(1));
        pickupRequestRepository.save(pickupRequest);
        notifyPickupScheduled(userId, pickupRequest.getPickup_date());
    }


    public void updatePickupRequest(Integer pickupRequestId, PickupRequest updatedPickupRequest) {
        PickupRequest pickupRequest = pickupRequestRepository.findPickupRequestById(pickupRequestId);
        if (pickupRequest == null) {
            throw new ApiException("Pickup request not found");
        }
        pickupRequest.setPickup_date(updatedPickupRequest.getPickup_date());
        pickupRequestRepository.save(pickupRequest);
    }

    public void deletePickupRequest(Integer pickupRequestId) {
        PickupRequest pickupRequest = pickupRequestRepository.findPickupRequestById(pickupRequestId);
        if (pickupRequest == null) {
            throw new ApiException("Pickup request not found");
        }

        pickupRequestRepository.delete(pickupRequest);
    }

    public PickupRequest getPickupRequestById(Integer pickupRequestId) {
        PickupRequest pickupRequest = pickupRequestRepository.findPickupRequestById(pickupRequestId);
        if (pickupRequest == null) {
            throw new ApiException("Pickup request not found");
        }
        return pickupRequest;
    }


    //==============================



    //6. Auto pickup request if 7 days passes with no manual request
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoCreatePickupRequestsAfter7Days() {
        List<ContainerRequest> deliveredContainers = containerRequestRepository.findAllByStatus("delivered");

        for (ContainerRequest containerRequest : deliveredContainers) {
            User user = containerRequest.getUser();

            // Get all pickup requests for this user
            List<PickupRequest> pickupRequests = pickupRequestRepository.findByUserId(user.getId());

            boolean hasActivePickup = false;

            for (PickupRequest pickupRequest : pickupRequests) {
                if (!"delivered".equals(pickupRequest.getStatus())) {
                    hasActivePickup = true;
                    break;
                }
            }

            if (!hasActivePickup) {
                // Check if 7 days passed since delivery date
                if (containerRequest.getDelivery_date() != null &&
                        ChronoUnit.DAYS.between(containerRequest.getDelivery_date(), LocalDate.now()) >= 7) {

                    PickupRequest autoPickupRequest = new PickupRequest();
                    autoPickupRequest.setUser(user);
                    autoPickupRequest.setRequest_date(LocalDateTime.now());
                    autoPickupRequest.setStatus("auto_requested");
                    autoPickupRequest.setPickup_date(LocalDate.now().plusDays(1));

                    pickupRequestRepository.save(autoPickupRequest);
                    notifyPickupScheduled(user.getId(), autoPickupRequest.getPickup_date());
                }
            }
        }
    }

    //7. Notify scheduled pickup
    public void notifyPickupScheduled(Integer userId, LocalDate pickup_date) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Pickup Scheduled");
        message.setText("Dear " + user.getName() + ",\n\nWe have scheduled a pickup for your recyclable materials which is " + pickup_date + ". Please prepare them for collection.\n\nThank you for your commitment to recycling!");
        message.setFrom("faisal.a.m.2012@gmail.com");

        mailSender.send(message);
    }

    //8. Close pickup request
    //17. Accept a pickup
    public void acceptPickupRequest(Integer pickupRequestId, Integer collectorId) {
        PickupRequest pickupRequest = pickupRequestRepository.findPickupRequestById(pickupRequestId);
        if (pickupRequest == null) {
            throw new ApiException("Pickup request not found");
        }

        if (!"requested".equals(pickupRequest.getStatus()) && !"auto_requested".equals(pickupRequest.getStatus())) {
            throw new ApiException("Only requested or auto-requested pickups can be accepted");
        }

        Collector collector = collectorRepository.findCollectorById(collectorId);
        if (collector == null) {
            throw new ApiException("Collector not found");
        }

        pickupRequest.setCollector(collector);
        pickupRequest.setPickup_date(LocalDate.now().plusDays(1));
        pickupRequest.setStatus("pickedup");

        pickupRequestRepository.save(pickupRequest);
    }

    // endpoint 16 - View assigned pickup requests
    public List<PickupRequest> getAssignedPickupRequests(Integer collectorId) {
        return pickupRequestRepository.findAllByCollectorId(collectorId);
    }

    //endpoint 11- Notify user: Pickup completed + Points updated
//    public void completePickupAndNotify(Integer userId) {
//        User user = userRepository.findUserById(userId);
//        if (user == null) {
//            throw new ApiException("User not found");
//        }
//        List<RecycleItem> items = recycleItemRepository.findRecycleItemsByUserId(userId).stream().filter(item -> item.getPickup_request() != null
//                        && item.getPickup_request().getStatus().equalsIgnoreCase("PickedUp")).toList();
//
//        double totalWeight = items.stream()
//                .mapToDouble(RecycleItem::getWeight_kg).sum();
//
//        int earnedPoints = (int) totalWeight;
//
//        if (earnedPoints == 0) {
//            return "No picked-up items found. No points added.";
//        }
//
//        user.setPoints(user.getPoints() + earnedPoints);
//        userRepository.save(user);
//
//        String message = "Pickup completed!\nYou earned " + earnedPoints + " points.\n\nThank you for recycling with us!";
//        sendEmail(user.getEmail(), " Pickup Completed & Points Earned", message);
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(user.getEmail());
//        message.setSubject("Pickup Scheduled");
//        message.setText("Pickup completed!\nYou earned " + earnedPoints + " points.\n\nThank you for recycling with us!");
//        message.setFrom("faisal.a.m.2012@gmail.com");
//
//        mailSender.send(message);
//    }
}
