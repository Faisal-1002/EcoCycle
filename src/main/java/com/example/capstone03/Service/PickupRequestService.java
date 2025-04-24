package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.*;
import com.example.capstone03.Repository.CollectorRepository;
import com.example.capstone03.Repository.ContainerRequestRepository;
import com.example.capstone03.Repository.PickupRequestRepository;
import com.example.capstone03.Repository.RecycleItemRepository;
import com.example.capstone03.Repository.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${twilio.account_sid}")
    private String twilioSid;
    @Value("${twilio.auth_token}")
    private String twilioToken;
    @Value("${twilio.phone_number}")
    private String twilioFrom;

    public List<PickupRequest> getAllPickupRequests() {
        return pickupRequestRepository.findAll();
    }

    // 18. Request pickup manually - Abdulraouf
    public void addPickupRequest(Integer userId, PickupRequest pickupRequest) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }
        for (PickupRequest r : user.getPickupRequests()) {
            if (r.getStatus().equalsIgnoreCase("requested")) {
                throw new ApiException("Pickup request already made");
            }
        }
        Container container = null;
        for (ContainerRequest request : user.getContainerRequests()) {
            if (request.getStatus().equalsIgnoreCase("delivered") && request.getContainer() != null) {
                container = request.getContainer();
                break;
            }
        }

        if (container == null) {
            throw new ApiException("Container not found");
        }

        pickupRequest.setUser(user);
        pickupRequest.setRequest_date(LocalDateTime.now());
        pickupRequest.setStatus("requested");
        pickupRequest.setPickup_date(pickupRequest.getPickup_date());
        pickupRequestRepository.save(pickupRequest);

        String subject = "Pickup Request";
        String body = "Dear " + user.getName() + ",\n\nYou have requested a pickup for your recyclable materials which is in " + pickupRequest.getPickup_date() + ". Our team will process it shortly.\n\nThank you for your commitment to recycling!";
        String from = "faisal.a.m.2012@gmail.com";
        sendEmailToUser(userId, subject, body, from);
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

    // 19. Get pickup request by ID - Abdulraouf
    public PickupRequest getPickupRequestById(Integer pickupRequestId) {
        PickupRequest pickupRequest = pickupRequestRepository.findPickupRequestById(pickupRequestId);
        if (pickupRequest == null) {
            throw new ApiException("Pickup request not found");
        }
        return pickupRequest;
    }

    // 20. Auto pickup request if 7 days passes with no manual request - Faisal
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoCreatePickupRequestsAfter7Days() {
        List<ContainerRequest> deliveredContainers = containerRequestRepository.findAllByStatus("delivered");

        for (ContainerRequest containerRequest : deliveredContainers) {
            User user = containerRequest.getUser();

            // Get all pickup requests for this user
            List<PickupRequest> pickupRequests = pickupRequestRepository.findByUserId(user.getId());

            boolean hasActivePickup = false;

            for (PickupRequest pickupRequest : pickupRequests) {
                if (!pickupRequest.getStatus().equals("delivered")) {
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

                    String subject = "Pickup Auto-Requested";
                    String body = "Dear " + user.getName() + ",\n\nAuto pickup request has been made for your recyclable materials which is in " + autoPickupRequest.getPickup_date() + ". Our team will process it shortly.\n\nThank you for your commitment to recycling!";
                    String from = "faisal.a.m.2012@gmail.com";
                    sendEmailToUser(user.getId(), subject, body, from);
                }
            }
        }
    }

    // 21. Accept a pickup request - Abdulraouf
    public void acceptPickupRequest(Integer pickupRequestId, Integer collectorId) {
        PickupRequest pickupRequest = pickupRequestRepository.findPickupRequestById(pickupRequestId);
        if (pickupRequest == null) {
            throw new ApiException("Pickup request not found");
        }

        if (!pickupRequest.getStatus().equals("requested") && !pickupRequest.getStatus().equals("auto_requested")) {
            throw new ApiException("Only requested or auto-requested pickups can be accepted");
        }

        Collector collector = collectorRepository.findCollectorById(collectorId);
        if (collector == null) {
            throw new ApiException("Collector not found");
        }

        pickupRequest.setCollector(collector);
        pickupRequest.setPickup_date(pickupRequest.getPickup_date());
        pickupRequest.setStatus("processing");
        pickupRequestRepository.save(pickupRequest);

        String messageBody = "Hi " + pickupRequest.getUser().getName() + ", your pickup request accepted by " + collector.getName() + ".\nIt is scheduled in " + pickupRequest.getPickup_date() + ".";
        sendWhatsAppMessage(pickupRequest.getUser().getPhone_number(), messageBody);
    }

    // 22. Picked up request - Faisal
    public void pickedUpRequest(Integer pickupId, Integer collectorId){
        PickupRequest pickupRequest = pickupRequestRepository.findPickupRequestById(pickupId);
        Collector collector = collectorRepository.findCollectorById(collectorId);

        if (pickupRequest == null) {
            throw new ApiException("Pickup Request not found");
        }
        if (collector == null){
            throw new ApiException("Collector not found");
        }
        if (!collector.getId().equals(pickupRequest.getCollector().getId())){
            throw new ApiException("Collector is not allowed to deliver");
        }
        User user = pickupRequest.getUser();
        if (user == null) {
            throw new ApiException("User not found");
        }
        if (pickupRequest.getStatus().equals("requested") || pickupRequest.getStatus().equals("auto-requested") || pickupRequest.getStatus().equals("pickedup")) {
            throw new ApiException("The pickup request for " + pickupRequest.getUser().getName() + " has either not been accepted yet or may have already been picked up.");
        }

        for (RecycleItem recycleItem : pickupRequest.getRecycle_items()){
            user.setPoints(recycleItem.getWeight_kg());
        }
        userRepository.save(user);
        pickupRequest.setStatus("pickedup");
        pickupRequest.setPickup_date(LocalDate.now());
        pickupRequestRepository.save(pickupRequest);

        String subject = "Pickup done";
        String body = "Dear " + pickupRequest.getUser().getName() + ",\n\n" +
                "We are pleased to inform you that your pickup request has been successfully picked up by our collector.\n\n" +
                "Thank you for using our service!\n\n" +
                "Best regards,\nYour Service Team";
        String from = "faisal.a.m.2012@gmail.com";
        sendEmailToUser(pickupRequest.getUser().getId(), subject, body, from);

    }

    // 23. View assigned pickup requests - Abdulraouf
    public List<PickupRequest> getAssignedPickupRequests(Integer collectorId) {
        return pickupRequestRepository.findAllByCollectorId(collectorId);

    }

    // 24. Points updated
//    public void addPointsToUser(Integer userId) {
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
//            throw new ApiException("No picked-up items found. No points added.") ;
//        }
//
//        user.setPoints(user.getPoints() + earnedPoints);
//        userRepository.save(user);
//
//        String message = "Pickup completed!\nYou earned " + earnedPoints + " points.\n\nThank you for recycling with us!";
//        sendWhatsAppMessage(user.getPhone_number(), message);
//    }

    // 24. Send email - Abeer
    public void sendEmailToUser(Integer userId, String subject, String body, String from) {
        User user = userRepository.findUserById(userId);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(from);
        mailSender.send(message);
    }

    // 25.Send whatsapp - Abeer
    public void sendWhatsAppMessage(String phoneNumber, String messageBody) {
        Twilio.init(twilioSid, twilioToken);
        phoneNumber = "+966" + phoneNumber.substring(1);
        Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:" + phoneNumber),
                new com.twilio.type.PhoneNumber(twilioFrom),
                messageBody
        ).create();
    }
}
