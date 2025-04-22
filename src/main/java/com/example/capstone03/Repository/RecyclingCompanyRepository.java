package com.example.capstone03.Repository;

import com.example.capstone03.Model.RecyclingCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecyclingCompanyRepository extends JpaRepository<RecyclingCompany , Integer> {
    RecyclingCompany findRecyclingCompanyById(Integer id);
}
