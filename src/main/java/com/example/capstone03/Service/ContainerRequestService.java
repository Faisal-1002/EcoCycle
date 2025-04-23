package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.Collector;
import com.example.capstone03.Model.Container;
import com.example.capstone03.Model.ContainerRequest;
import com.example.capstone03.Model.User;
import com.example.capstone03.Repository.CollectorRepository;
import com.example.capstone03.Repository.ContainerRepository;
import com.example.capstone03.Repository.ContainerRequestRepository;
import com.example.capstone03.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContainerRequestService {

    private final ContainerRequestRepository containerRequestRepository;
    private final ContainerRepository containerRepository;
    private final UserRepository userRepository;
    private final CollectorRepository collectorRepository;
    private final JavaMailSender mailSender;

    public List<ContainerRequest> getAllContainerRequests() {
        return containerRequestRepository.findAll();
    }

    //1. Request container
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
        sendContainerRequestReceivedEmail(user.getId());
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

    //2. Notify user about the container request by email
    public void sendContainerRequestReceivedEmail(Integer userId) {
        User user = userRepository.findUserById(userId);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Container Request Received");
        message.setText("Dear " + user.getName() + ",\n\nWe have received your container request. Our team will process it shortly.\n\nThank you for using our service!");
        message.setFrom("faisal.a.m.2012@gmail.com");
        mailSender.send(message);
    }

    //3. Deliver Container
    public void deliverContainer(Integer collectorId, Integer containerRequestId) {
        ContainerRequest containerRequest = containerRequestRepository.findContainerRequestById(containerRequestId);
        if (containerRequest == null) {
            throw new ApiException("Container request not found");
        }

        if (!"processing".equals(containerRequest.getStatus())) {
            throw new ApiException("Only processing requests can be delivered");
        }

        Collector collector = collectorRepository.findCollectorById(collectorId);
        if (collector == null) {
            throw new ApiException("Collector not found");
        }

        containerRequest.setCollector(collector);
        containerRequest.setDelivery_date(LocalDate.now());
        containerRequest.setStatus("delivered");
        containerRequestRepository.save(containerRequest);

        notifyContainerDelivered(containerRequest.getUser().getId());
    }

    //4. Notify delivery
    public void notifyContainerDelivered(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Container Delivered");
        message.setText("Dear " + user.getName() + ",\n\nWe are pleased to inform you that your requested container has been delivered.\n\nThank you for choosing our recycling service!");
        message.setFrom("faisal.a.m.2012@gmail.com");

        mailSender.send(message);
    }

    //endpoint 13 - View all container requests
    public List<ContainerRequest> getPendingRequests() {
        return containerRequestRepository.findContainerRequestByStatus("Pending");
    }

    //endpoint 14 -  Accept container request
    public void acceptContainerRequest(Integer containerRequestId, Integer collectorId) {
        ContainerRequest request = containerRequestRepository.findContainerRequestById(containerRequestId);
        Collector collector = collectorRepository.findCollectorById(collectorId);

        if (collector == null)
            throw new ApiException("Collector is not found");

        if (request == null) {
            throw new ApiException("Container request not found");

        }
        if (!request.getStatus().equalsIgnoreCase("pending")) {
            throw new RuntimeException("Only pending requests can be accepted");
        }
        Container container = containerRepository.findTopByAvailableTrue(); // ماتشتغل
        if (container == null) {
            throw new ApiException("No available container found");
        }
        request.setContainer(container);
        request.setCollector(collector);
        request.setStatus("processing");
        containerRequestRepository.save(request);
        container.setAvailable(false);
        containerRepository.save(container);
    }


}
