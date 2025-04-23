package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.CompanyRequest;
import com.example.capstone03.Model.PickupRequest;
import com.example.capstone03.Model.RecyclingCompany;
import com.example.capstone03.Model.User;
import com.example.capstone03.Model.RecyclingCompany;
import com.example.capstone03.Repository.CompanyRequestRepository;
import com.example.capstone03.Repository.RecyclingCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CompanyRequestService {

    private final CompanyRequestRepository companyRequestRepository;
    private final RecyclingCompanyRepository recyclingCompanyRepository;

    public List<CompanyRequest> getAllCompanyRequest(){
        return companyRequestRepository.findAll();
    }

    public void addCompanyRequest(CompanyRequest companyRequest){
        RecyclingCompany recyclingCompany = recyclingCompanyRepository.findRecyclingCompanyById(companyRequest.getRecycling_company().getId());

        if (recyclingCompany == null){
            throw new ApiException("recycling company is not found");
        }
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
        companyRequestRepository.delete(companyRequest);
    }
}
