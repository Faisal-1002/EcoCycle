package com.example.capstone03.Repository;

import com.example.capstone03.Model.Collector;
import com.example.capstone03.Model.ContainerRequest;
import com.example.capstone03.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContainerRequestRepository extends JpaRepository<ContainerRequest, Integer> {
    ContainerRequest findContainerRequestById(Integer id);
    List<ContainerRequest> findContainerRequestByStatus(String status);
    List<ContainerRequest> findAllByStatus(String delivered);
    List<ContainerRequest> findAllByStatusIgnoreCase(String status);
    List<ContainerRequest> findAllByCollector(Collector collector);
    List<ContainerRequest> findAllByUser(User user);
}
