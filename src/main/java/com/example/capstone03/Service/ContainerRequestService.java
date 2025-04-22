package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.Container;
import com.example.capstone03.Model.ContainerRequest;
import com.example.capstone03.Model.User;
import com.example.capstone03.Repository.ContainerRequestRepository;
import com.example.capstone03.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContainerRequestService {

    private final ContainerRequestRepository containerRequestRepository;
    private final UserRepository userRepository;

    public List<ContainerRequest> getAllContainerRequests() {
        return containerRequestRepository.findAll();
    }


    public void assignContainerRequest(Integer userId,ContainerRequest containerRequest) {
        User user = userRepository.findUserById(userId);
        if (user == null){
            throw new ApiException("user not found");
        }

        containerRequest.setRequest_date(LocalDate.now());
        containerRequest.setStatus("Pending");
        containerRequest.setDelivery_date(LocalDate.now().plusWeeks(1));
        containerRequest.setUser(user);
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
}
