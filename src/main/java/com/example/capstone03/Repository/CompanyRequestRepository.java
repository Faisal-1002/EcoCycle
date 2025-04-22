package com.example.capstone03.Repository;

import com.example.capstone03.Model.CompanyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRequestRepository extends JpaRepository<CompanyRequest , Integer> {

    CompanyRequest findCompanyRequestById (Integer id);

}
