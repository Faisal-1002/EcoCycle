package com.example.capstone03.Repository;

import com.example.capstone03.Model.PickupRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickupRequestRepository extends JpaRepository<PickupRequest, Integer> {
    PickupRequest findPickupRequestById(Integer id);
}
