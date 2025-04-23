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
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${twilio.account_sid}")
    private String twilioSid;
    @Value("${twilio.auth_token}")
    private String twilioToken;
    @Value("${twilio.phone_number}")
    private String twilioFrom;


    public List<ContainerRequest> getAllContainerRequests() {
        return containerRequestRepository.findAll();
    }

    // 7. Request container
    public void addContainerRequest(Integer userId, ContainerRequest containerRequest) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }
        if (!user.getContainerRequests().isEmpty())
            throw new ApiException("User has requested a container");

        containerRequest.setUser(user);
        containerRequest.setRequest_date(LocalDate.now());
        containerRequest.setStatus("pending");
        containerRequest.setDelivery_date(LocalDate.now().plusWeeks(1));
        containerRequestRepository.save(containerRequest);

        String subject = "Container Request Received";
        String body = "\"Dear \" + user.getName() + \",\\n\\nWe have received your container request. Our team will process it shortly.\\n\\nThank you for using our service!\"";
        String from = "faisal.a.m.2012@gmail.com";
        sendEmailToUser(user.getId(), subject, body, from);
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

    // 8. Get container request by ID
    public ContainerRequest getContainerRequestById(Integer id) {
        ContainerRequest request = containerRequestRepository.findContainerRequestById(id);
        if (request == null) {
            throw new ApiException("Container request not found");
        }
        return request;
    }

    // 9. View all pending container requests
    public List<ContainerRequest> getPendingRequests() {
        return containerRequestRepository.findContainerRequestByStatus("Pending");
    }

    // 10. Accept container request
    public void acceptContainerRequest(Integer containerRequestId, Integer collectorId) {
        ContainerRequest containerRequest = containerRequestRepository.findContainerRequestById(containerRequestId);
        if (containerRequest == null)
            throw new ApiException("Container request not found");

        Collector collector = collectorRepository.findCollectorById(collectorId);
        if (collector == null)
            throw new ApiException("Collector is not found");

        User user = containerRequest.getUser();
        if (user == null)
            throw new ApiException("User not found");

        Container container = containerRepository.findTop1ByAvailableTrueOrderByIdAsc();
        if (container == null)
            throw new ApiException("No available container found");

        if (!containerRequest.getStatus().equalsIgnoreCase("pending"))
            throw new RuntimeException("Only pending requests can be accepted");

        containerRequest.setContainer(container);
        containerRequest.setCollector(collector);
        containerRequest.setStatus("processing");
        containerRequestRepository.save(containerRequest);

        container.setAvailable(false);
        containerRepository.save(container);

        String message = "Hi, " + user.getName() + " your request #" + containerRequest.getId() + " has been accepted by " + collector.getName() + " collector.";
        sendWhatsAppMessage(user.getPhone_number(), message);
    }

    // 11. Deliver Container
    public void deliverContainer(Integer collectorId, Integer containerRequestId) {
        ContainerRequest containerRequest = containerRequestRepository.findContainerRequestById(containerRequestId);
        if (containerRequest == null)
            throw new ApiException("Container request not found");

        Collector collector = collectorRepository.findCollectorById(collectorId);
        if (collector == null)
            throw new ApiException("Collector is not found");

        User user = containerRequest.getUser();
        if (user == null)
            throw new ApiException("User not found");

        if (collector != containerRequest.getCollector())
            throw new ApiException("The requested container is not the same as the provided collector");

        if (!containerRequest.getStatus().equalsIgnoreCase("processing")) {
            throw new ApiException("Only processing requests can be delivered");
        }

        containerRequest.setCollector(collector);
        containerRequest.setDelivery_date(LocalDate.now());
        containerRequest.setStatus("delivered");
        containerRequestRepository.save(containerRequest);

        String subject = "Container Delivered";
        String body = "Dear " + user.getName() + ",\n\nWe are pleased to inform you that your requested container has been delivered.\n\nThank you for choosing our recycling service!";
        String from = "faisal.a.m.2012@gmail.com";
        sendEmailToUser(user.getId(), subject, body, from);
    }

    // 12. Get requests by status
    public List<ContainerRequest> getRequestsByStatus(String status) {
        List<String> validStatuses = List.of("pending", "processing", "delivered");
        if (!validStatuses.contains(status.toLowerCase())) {
            throw new ApiException("Invalid status value");
        }
        return containerRequestRepository.findAllByStatusIgnoreCase(status);
    }

    // 13. Get requests by collector ID
    public List<ContainerRequest> getRequestsByCollectorId(Integer collectorId) {
        Collector collector = collectorRepository.findCollectorById(collectorId);
        if (collector == null)
            throw new ApiException("Collector not found");
        return containerRequestRepository.findAllByCollector(collector);
    }

    // 14. Notify user about the container request by email
    public void sendEmailToUser(Integer userId, String subject, String body, String from) {
        User user = userRepository.findUserById(userId);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(from);
        mailSender.send(message);
    }

    // 15. Send accept notification through Whatsapp
    public void sendWhatsAppMessage(String phoneNumber, String messageBody) {
        Twilio.init(twilioSid, twilioToken);
        phoneNumber = "+966" + phoneNumber.substring(1);
        Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:" + phoneNumber),
                new com.twilio.type.PhoneNumber(twilioFrom),
                messageBody
        ).create();
    }
}
