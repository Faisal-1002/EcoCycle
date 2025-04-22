package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.Container;
import com.example.capstone03.Model.ContainerRequest;
import com.example.capstone03.Model.User;
import com.example.capstone03.Repository.ContainerRepository;
import com.example.capstone03.Repository.ContainerRequestRepository;
import com.example.capstone03.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContainerRequestService {

    private final ContainerRequestRepository containerRequestRepository;
    private final ContainerRepository containerRepository;
    private final UserRepository userRepository;

    public List<ContainerRequest> getAllContainerRequests() {
        return containerRequestRepository.findAll();
    }

    public void addContainerRequest(Integer userId, ContainerRequest containerRequest) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }
        containerRequest.setUser(user);
        containerRequest.setRequest_date(LocalDate.now());
        containerRequest.setStatus("pending");
        containerRequest.setDelivery_date(LocalDate.now().plusWeeks(1));
        containerRequestRepository.save(containerRequest);
    }

    public void updateContainerRequest(Integer containerRequestId, ContainerRequest updatedContainerRequest) {
        ContainerRequest containerRequest = containerRequestRepository.findContainerRequestById(containerRequestId);
        if (containerRequest == null) {
            throw new ApiException("Container request not found");
        }
        containerRequest.setIssue_notes(updatedContainerRequest.getIssue_notes());
        containerRequestRepository.save(containerRequest);
    }

    public void deleteContainerRequest(Integer containerRequestId) {
        ContainerRequest containerRequest = containerRequestRepository.findContainerRequestById(containerRequestId);
        if (containerRequest == null) {
            throw new ApiException("Container request not found");
        }
        containerRequestRepository.delete(containerRequest);
    }

    public ContainerRequest getContainerRequestById(Integer containerRequestId) {
        ContainerRequest containerRequest = containerRequestRepository.findContainerRequestById(containerRequestId);
        if (containerRequest == null) {
            throw new ApiException("Container request not found");
        }
        return containerRequest;
    }

    public void deliverContainer(Integer containerRequestId) {
        ContainerRequest containerRequest = containerRequestRepository.findContainerRequestById(containerRequestId);
        if (containerRequest == null) {
            throw new ApiException("Container request not found");
        }

        if (!"pending".equals(containerRequest.getStatus())) {
            throw new ApiException("Only pending requests can be delivered");
        }

        Container container = containerRepository.findFirstAvailableContainer();
        if (container == null) {
            throw new ApiException("No available container found");
        }

        containerRequest.setContainer(container);
        containerRequest.setDelivery_date(LocalDate.now());
        containerRequest.setStatus("delivered");
        containerRequestRepository.save(containerRequest);

        container.setIs_available(false);
        containerRepository.save(container);
  }

    //endpoint 13 - View all container requests
  public List<ContainerRequest> getPendingRequests() {
      return containerRequestRepository.findContainerRequestByStatus("Pending");
  }
}
