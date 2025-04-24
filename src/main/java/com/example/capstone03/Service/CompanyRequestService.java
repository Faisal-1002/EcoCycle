package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.*;
import com.example.capstone03.Model.RecyclingCompany;
import com.example.capstone03.Repository.CollectorRepository;
import com.example.capstone03.Repository.CompanyRequestRepository;
import com.example.capstone03.Repository.RecycleItemRepository;
import com.example.capstone03.Repository.RecyclingCompanyRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompanyRequestService {

    private final CompanyRequestRepository companyRequestRepository;
    private final RecyclingCompanyRepository recyclingCompanyRepository;
    private final CollectorRepository collectorRepository;
    private final RecycleItemRepository recycleItemRepository;
    private final MailSender mailSender;

    @Value("${twilio.account_sid}")
    private String twilioSid;
    @Value("${twilio.auth_token}")
    private String twilioToken;
    @Value("${twilio.phone_number}")
    private String twilioFrom;


    public List<CompanyRequest> getAllCompanyRequest(){
        return companyRequestRepository.findAll();
    }

    // 29. Add company request
    public void addCompanyRequest(Integer recyclingCompanyId,CompanyRequest companyRequest){
        RecyclingCompany recyclingCompany = recyclingCompanyRepository.findRecyclingCompanyById(recyclingCompanyId);

        if (recyclingCompany == null){
            throw new ApiException("recycling company is not found");
        }

        companyRequest.setRecycling_company(recyclingCompany);
        companyRequest.setStatus("requested");
        companyRequest.setRequest_date(LocalDate.now());
        companyRequestRepository.save(companyRequest);

        String subject = "Company Request Received";
        String body = "Dear " + recyclingCompany.getName() + ",\n\nWe have received your request. Our collectors will process it shortly.\n\nThank you for using our service!";
        String from = "faisal.a.m.2012@gmail.com";
        sendEmailToUser(recyclingCompany.getId(), subject, body, from);

    }

    public void updateCompanyRequest(Integer id , CompanyRequest companyRequest){
        CompanyRequest oldCompanyRequest = companyRequestRepository.findCompanyRequestById(id);

        if (oldCompanyRequest == null){
            throw new ApiException("companyRequestId is not fond");
        }

        oldCompanyRequest.setQuantity(companyRequest.getQuantity());

        companyRequestRepository.save(oldCompanyRequest);
    }

    public void deleteCompanyRequest(Integer id){
        CompanyRequest companyRequest = companyRequestRepository.findCompanyRequestById(id);
        if (companyRequest == null){
            throw new ApiException("companyRequestId is not fond");
        }
        companyRequestRepository.delete(companyRequest);
    }

    // 30. View pending company requests
    public List<CompanyRequest> pendingRequests(){
        List<CompanyRequest> requests = companyRequestRepository.findAll();
        List<CompanyRequest> pendingRequest = new ArrayList<>();
        for (CompanyRequest request : requests){
            if (request.getStatus().equals("requested") || request.getStatus().equals("auto-requested")){
                pendingRequest.add(request);
            }
        }

        return pendingRequest;
    }

    // 31. Accept company delivery request
    public void acceptCompanyRequest(Integer companyRequestId, Integer collectorId){
        CompanyRequest companyRequest = companyRequestRepository.findCompanyRequestById(companyRequestId);
        Collector collector = collectorRepository.findCollectorById(collectorId);

        if (companyRequest == null) {
            throw new ApiException("Company Request not found");
        }
        if (collector == null){
            throw new ApiException("collector not found");
        }

        if (companyRequest.getStatus().equals("processing") ||
                companyRequest.getStatus().equals("Delivered")) {
            throw new ApiException("Company request has already been accepted or is in progress by " +companyRequest.getCollector().getName());
        }

        companyRequest.setCollector(collector);
        companyRequest.setStatus("processing");
        companyRequest.setDelivery_date(LocalDate.now().plusWeeks(1));
        companyRequestRepository.save(companyRequest);

        String message = "Hi, " + companyRequest.getRecycling_company().getName() + " your request #" + companyRequest.getId() + " has been accepted by " + collector.getName() + " collector.";
        sendWhatsAppMessage(companyRequest.getRecycling_company().getPhone_number(), message);
    }

    // 32. Delivery
    public void deliverRequest(Integer companyRequestId, Integer collectorId){
        CompanyRequest companyRequest = companyRequestRepository.findCompanyRequestById(companyRequestId);
        Collector collector = collectorRepository.findCollectorById(collectorId);
        Set<PickupRequest> pickupRequests = collector.getPickup_request();

        int totalPickedUpQuantity = 0;
        for(PickupRequest pickupRequest : pickupRequests){
            List<RecycleItem> recycleItems = pickupRequest.getRecycle_items();

            for (RecycleItem recycleItem : recycleItems) {
                if (recycleItem.getStatus().equals("delivered")){
                    continue;
                }
                totalPickedUpQuantity += recycleItem.getWeight_kg();

                recycleItem.setStatus("delivered");
                recycleItemRepository.save(recycleItem);
            }

        }

        if (companyRequest == null) {
            throw new ApiException("Company Request not found");
        }
        if (collector == null){
            throw new ApiException("Collector not found");
        }
        if (!collector.getId().equals(companyRequest.getCollector().getId())){
            throw new ApiException("Collector is not allowed to deliver");
        }

        if (companyRequest.getStatus().equals("requested") || companyRequest.getStatus().equals("auto-requested") || companyRequest.getStatus().equals("delivered")) {
            throw new ApiException( companyRequest.getRecycling_company().getName() + " Company request is not accepted yet");
        }

        if (totalPickedUpQuantity < companyRequest.getQuantity()) {
            throw new ApiException("Pickup quantity is less than the requested quantity.collector needs to pick up more");
        }

        companyRequest.setStatus("delivered");
        companyRequest.setDelivery_date(LocalDate.now());
        companyRequestRepository.save(companyRequest);

        String subject = "Company Request Delivered";
        String body = "Dear " + companyRequest.getRecycling_company().getName() + ",\n\n" +
                "We are pleased to inform you that your request has been successfully delivered by our collector.\n\n" +
                "Thank you for using our service!\n\n" +
                "Best regards,\nYour Service Team";
        String from = "faisal.a.m.2012@gmail.com";
        sendEmailToUser(companyRequest.getRecycling_company().getId(), subject, body, from);

    }

    // 33. View completed delivery history
    public List<CompanyRequest> getDeliveredRequest(){
        List<CompanyRequest> requests = companyRequestRepository.findAll();
        List<CompanyRequest> deliveredRequest = new ArrayList<>();

        for (CompanyRequest request : requests){
            if (request.getStatus().equals("delivered")){
                deliveredRequest.add(request);
            }
        }

        return deliveredRequest;
    }

    // 34. Send email
    public void sendEmailToUser(Integer companyId, String subject, String body, String from) {
        RecyclingCompany recyclingCompany = recyclingCompanyRepository.findRecyclingCompanyById(companyId);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recyclingCompany.getEmail());
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(from);
        mailSender.send(message);
    }

    // 35. send whatsapp
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
