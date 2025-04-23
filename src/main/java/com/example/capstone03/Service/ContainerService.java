package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.Container;
import com.example.capstone03.Repository.ContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContainerService {

    private final ContainerRepository containerRepository;

    public List<Container> getAllContainers() {
        return containerRepository.findAll();
    }

    public void addContainer(Container container) {
        container.setAvailable(true);
        container.setStatus_condition("new");
        containerRepository.save(container);
    }

    public void updateContainer(Integer containerId, Container updatedContainer) {
        Container container = containerRepository.findContainerById(containerId);
        if (container == null) {
            throw new ApiException("Container not found");
        }

        container.setAvailable(updatedContainer.getAvailable());
        container.setLocation(updatedContainer.getLocation());
        container.setStatus_condition(updatedContainer.getStatus_condition());

        containerRepository.save(container);
    }

    public void deleteContainer(Integer containerId) {
        Container container = containerRepository.findContainerById(containerId);
        if (container == null) {
            throw new ApiException("Container not found");
        }

        containerRepository.delete(container);
    }

    public Container getContainerById(Integer containerId) {
        Container container = containerRepository.findContainerById(containerId);
        if (container == null) {
            throw new ApiException("Container not found");
        }
        return container;
    }
}
