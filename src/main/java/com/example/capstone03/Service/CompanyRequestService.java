package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.*;
import com.example.capstone03.Model.RecyclingCompany;
import com.example.capstone03.Repository.CollectorRepository;
import com.example.capstone03.Repository.CompanyRequestRepository;
import com.example.capstone03.Repository.RecyclingCompanyRepository;
import lombok.RequiredArgsConstructor;
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
    private final MailSender mailSender;


    public List<CompanyRequest> getAllCompanyRequest(){
        return companyRequestRepository.findAll();
    }

    public void addCompanyRequest(Integer recyclingCompanyId,CompanyRequest companyRequest){
        RecyclingCompany recyclingCompany = recyclingCompanyRepository.findRecyclingCompanyById(recyclingCompanyId);

        if (recyclingCompany == null){
            throw new ApiException("recycling company is not found");
        }

        companyRequest.setRecycling_company(recyclingCompany);
        companyRequest.setStatus("requested");
        companyRequest.setRequest_date(LocalDate.now());
        companyRequestRepository.save(companyRequest);
        sendCompanyRequestReceivedEmail(recyclingCompanyId);

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

    // 29. View pending company requests
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

    // 30. Accept company delivery request
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
    }

    // 31. Delivery
    public void deliverRequest(Integer companyRequestId, Integer collectorId){
        CompanyRequest companyRequest = companyRequestRepository.findCompanyRequestById(companyRequestId);
        Collector collector = collectorRepository.findCollectorById(collectorId);
        Set<PickupRequest> pickupRequests = collector.getPickup_request();
        for (PickupRequest pickupRequest : pickupRequests){
            if (pickupRequest.getStatus().equals("delivered")){
                List<RecycleItem> recycleItem = pickupRequest.getRecycle_items();
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


        companyRequest.setStatus("delivered");
        companyRequest.setDelivery_date(LocalDate.now());
        companyRequestRepository.save(companyRequest);
        sendCompanyRequestDeliveredEmail(companyRequest.getRecycling_company().getId());

    }

    // 32. View completed delivery history
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

    // 33. Send email
    public void sendCompanyRequestReceivedEmail(Integer recycleCompanyId) {
        RecyclingCompany recyclingCompany = recyclingCompanyRepository.findRecyclingCompanyById(recycleCompanyId);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recyclingCompany.getEmail());
        message.setSubject("Company Request Received");
        message.setText("Dear " + recyclingCompany.getName() + ",\n\nWe have received your request. Our collectors will process it shortly.\n\nThank you for using our service!");
        message.setFrom("faisal.a.m.2012@gmail.com");
        mailSender.send(message);
    }

    // 34. Send email
    public void sendCompanyRequestDeliveredEmail(Integer recycleCompanyId) {
        RecyclingCompany recyclingCompany = recyclingCompanyRepository.findRecyclingCompanyById(recycleCompanyId);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recyclingCompany.getEmail());
        message.setSubject("Company Request Delivered");
        message.setText("Dear " + recyclingCompany.getName() + ",\n\n" +
                "We are pleased to inform you that your request has been successfully delivered by our collector.\n\n" +
                "Thank you for using our service!\n\n" +
                "Best regards,\nYour Service Team");
        message.setFrom("faisal.a.m.2012@gmail.com");
        mailSender.send(message);
    }

}
