package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.RecyclingCompany;
import com.example.capstone03.Repository.RecyclingCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecyclingCompanyService {

    private final RecyclingCompanyRepository recyclingCompanyRepository;

    public List<RecyclingCompany> getAllRecyclingCompany(){
        return recyclingCompanyRepository.findAll();
    }

    public void addRecyclingCompany(RecyclingCompany recyclingCompany){
        //recyclingCompany.setId(recyclingCompany.getId());
        recyclingCompanyRepository.save(recyclingCompany);
    }

    public void updateRecyclingCompany(Integer id , RecyclingCompany recyclingCompany){
        RecyclingCompany oldRecyclingCompany = recyclingCompanyRepository.findRecyclingCompanyById(id);

        if (oldRecyclingCompany == null){
            throw new ApiException("RecyclingCompanyId is not fond");
        }
        oldRecyclingCompany.setName(recyclingCompany.getName());
        oldRecyclingCompany.setEmail(recyclingCompany.getEmail());
        oldRecyclingCompany.setPassword(recyclingCompany.getPassword());
        oldRecyclingCompany.setCertification(recyclingCompany.getCertification());
        oldRecyclingCompany.setBasic_activity(recyclingCompany.getBasic_activity());
        oldRecyclingCompany.setPhone_number(recyclingCompany.getPhone_number());
        oldRecyclingCompany.setPermit_expiration(recyclingCompany.getPermit_expiration());

        recyclingCompanyRepository.save(oldRecyclingCompany);
    }

    public void deleteRecyclingCompany(Integer id){
        RecyclingCompany recyclingCompany = recyclingCompanyRepository.findRecyclingCompanyById(id);
        if (recyclingCompany == null){
            throw new ApiException("recyclingCompanyId is not fond");
        }
    }

}
