package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.CompanyRequest;
import com.example.capstone03.Repository.CompanyRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CompanyRequestService {

    private final CompanyRequestRepository companyRequestRepository;

    public List<CompanyRequest> getAllCompanyRequest(){
        return companyRequestRepository.findAll();
    }
    public void addCompanyRequest(CompanyRequest companyRequest){
        companyRequest.setId(companyRequest.getId());
        companyRequestRepository.save(companyRequest);

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
    }
}
