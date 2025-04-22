package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.ContainerRequest;
import com.example.capstone03.Repository.ContainerRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContainerRequestService {

    private final ContainerRequestRepository containerRequestRepository;

    public List<ContainerRequest> getAllContainerRequests() {
        return containerRequestRepository.findAll();
    }

    public void addContainerRequest(ContainerRequest containerRequest) {
        containerRequestRepository.save(containerRequest);
    }

    public void updateContainerRequest(Integer id, ContainerRequest updatedContainerRequest) {
        ContainerRequest containerRequest = containerRequestRepository.findContainerRequestById(id);
        if (containerRequest == null) {
            throw new ApiException("Container request not found");
        }

        containerRequest.setUser(updatedContainerRequest.getUser());
//        containerRequest.setCollector(updatedContainerRequest.getCollector());
//        containerRequest.setContainer(updatedContainerRequest.getContainer());
        containerRequest.setRequest_date(updatedContainerRequest.getRequest_date());
        containerRequest.setDelivery_date(updatedContainerRequest.getDelivery_date());
        containerRequest.setStatus(updatedContainerRequest.getStatus());
        containerRequest.setIssue_notes(updatedContainerRequest.getIssue_notes());

        containerRequestRepository.save(containerRequest);
    }

    public void deleteContainerRequest(Integer id) {
        ContainerRequest containerRequest = containerRequestRepository.findContainerRequestById(id);
        if (containerRequest == null) {
            throw new ApiException("Container request not found");
        }

        containerRequestRepository.delete(containerRequest);
    }

    public ContainerRequest getContainerRequestById(Integer id) {
        ContainerRequest containerRequest = containerRequestRepository.findContainerRequestById(id);
        if (containerRequest == null) {
            throw new ApiException("Container request not found");
        }
        return containerRequest;
    }

    //endpoint 13 - View all container requests
    public List<ContainerRequest> getPendingRequests() {
        return containerRequestRepository.findContainerRequestByStatus("Pending");
    }
}
