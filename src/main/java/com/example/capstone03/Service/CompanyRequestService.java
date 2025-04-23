package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.*;
import com.example.capstone03.Model.RecyclingCompany;
import com.example.capstone03.Repository.CollectorRepository;
import com.example.capstone03.Repository.CompanyRequestRepository;
import com.example.capstone03.Repository.RecyclingCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CompanyRequestService {

    private final CompanyRequestRepository companyRequestRepository;
    private final RecyclingCompanyRepository recyclingCompanyRepository;
    private final CollectorRepository collectorRepository;

    public List<CompanyRequest> getAllCompanyRequest(){
        return companyRequestRepository.findAll();
    }

    public void addCompanyRequest(Integer recyclingCompanyId,CompanyRequest companyRequest){
        RecyclingCompany recyclingCompany = recyclingCompanyRepository.findRecyclingCompanyById(recyclingCompanyId);

        if (recyclingCompany == null){
            throw new ApiException("recycling company is not found");
        }

        companyRequest.setRecycling_company(recyclingCompany);
        companyRequest.setStatus("Requested");
        companyRequestRepository.save(companyRequest);

    }

    //no Duplication

//    public void addCompanyRequests(Integer recyclingCompanyId ,CompanyRequest companyRequest){
//        RecyclingCompany recyclingCompany = recyclingCompanyRepository.findRecyclingCompanyById(recyclingCompanyId);
//
//        if (recyclingCompany == null){
//            throw new ApiException("Recycling Company not found");
//        }
//
//        List<CompanyRequest> requests = companyRequestRepository.findAll();
//        for (CompanyRequest request : requests){
//            if (request.getRecycling_company().getId().equals(recyclingCompanyId)  && request.getStatus().equals("Requested")){
//                throw new ApiException("Recycling Company have already requested ");
//            }
//        }
//
//        companyRequest.setStatus("Requested");
//        companyRequest.setRecycling_company(recyclingCompany);
//
//        companyRequestRepository.save(companyRequest);
//
//    }

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
    }

    //=================================================
    //20  View pending company requests

    public List<CompanyRequest> pendingRequests(){
        List<CompanyRequest> requests = companyRequestRepository.findAll();
        List<CompanyRequest> pendingRequest = new ArrayList<>();
        for (CompanyRequest request : requests){
            if (request.getStatus().equals("Requested")){
                pendingRequest.add(request);
            }
        }

        return pendingRequest;
    }

    //21  Accept company delivery request

    public void acceptCompanyRequest(Integer companyRequestId, Integer collectorId){
        CompanyRequest companyRequest = companyRequestRepository.findCompanyRequestById(companyRequestId);
        Collector collector = collectorRepository.findCollectorById(collectorId);

        if (companyRequest == null) {
            throw new ApiException("Company Request not found");
        }
        if (collector == null){
            throw new ApiException("collector not found");
        }

        if (companyRequest.getStatus().equals("Processing") ||
                companyRequest.getStatus().equals("PickedUp") ||
                companyRequest.getStatus().equals("Delivered")) {
            throw new ApiException("Company request has already been accepted or is in progress by " +companyRequest.getCollector().getName());
        }

        companyRequest.setCollector(collector);
        companyRequest.setStatus("Processing");
        companyRequestRepository.save(companyRequest);
    }


    //22 View completed delivery history

    public List<CompanyRequest> getDeliveredRequest(){
        List<CompanyRequest> requests = companyRequestRepository.findAll();
        List<CompanyRequest> deliveredRequest = new ArrayList<>();

        for (CompanyRequest request : requests){
            if (request.getStatus().equals("Delivered")){
                deliveredRequest.add(request);
            }
        }

        return deliveredRequest;
    }
}
