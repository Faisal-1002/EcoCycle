package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.Container;
import com.example.capstone03.Model.User;
import com.example.capstone03.Repository.ContainerRepository;
import com.example.capstone03.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContainerService {

    private final ContainerRepository containerRepository;
    private final UserRepository userRepository;

    public List<Container> getAllContainers() {
        return containerRepository.findAll();
    }

    public void addContainer(Container container) {
        if (container.getStatus_condition().equalsIgnoreCase("damaged")) {
            throw new ApiException("Invalid status condition. Must be 'new'");
        }
        container.setAvailable(true);
        containerRepository.save(container);
    }

    public void updateContainer(Integer id, Container updatedContainer) {
        Container container = containerRepository.findContainerById(id);
        if (container == null) {
            throw new ApiException("Container not found");
        }

        container.setAvailable(updatedContainer.getAvailable());
        container.setLocation(updatedContainer.getLocation());
        container.setStatus_condition(updatedContainer.getStatus_condition());

        containerRepository.save(container);
    }

    public void deleteContainer(Integer id) {
        Container container = containerRepository.findContainerById(id);
        if (container == null) {
            throw new ApiException("Container not found");
        }

        containerRepository.delete(container);
    }

    // 3. Get container by ID
    public Container getContainerById(Integer id) {
        Container container = containerRepository.findContainerById(id);
        if (container == null) {
            throw new ApiException("Container not found");
        }
        return container;
    }

    // 4. Get all available containers
    public List<Container> getAvailableContainers() {
        return containerRepository.findAllByAvailableTrue();
    }
}
