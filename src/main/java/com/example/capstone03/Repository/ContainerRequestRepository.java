package com.example.capstone03.Repository;

import com.example.capstone03.Model.ContainerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContainerRequestRepository extends JpaRepository<ContainerRequest, Integer> {
    ContainerRequest findContainerRequestById(Integer id);

    List<ContainerRequest> findContainerRequestByStatus(String status);

}
