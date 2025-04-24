package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.Collector;
import com.example.capstone03.Model.ContainerRequest;
import com.example.capstone03.Model.PickupRequest;
import com.example.capstone03.Repository.CollectorRepository;
import com.example.capstone03.Repository.ContainerRequestRepository;
import com.example.capstone03.Repository.PickupRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectorService {

    private final CollectorRepository collectorRepository;
    private final ContainerRequestRepository containerRequestRepository;
    private final PickupRequestRepository pickupRequestRepository;

    public List<Collector> getAllCollectors() {
        return collectorRepository.findAll();
    }

    public void addCollector(Collector collector) {
        collectorRepository.save(collector);
    }

    public void updateCollector(Integer id, Collector updatedCollector) {
        Collector collector = collectorRepository.findCollectorById(id);
        if (collector == null) {
            throw new ApiException("Collector not found");
        }

        collector.setName(updatedCollector.getName());
        collector.setEmail(updatedCollector.getEmail());
        collector.setPassword(updatedCollector.getPassword());
        collector.setAddress(updatedCollector.getAddress());
        collector.setPhone_number(updatedCollector.getPhone_number());

        collectorRepository.save(collector);
    }

    public void deleteCollector(Integer id) {
        Collector collector = collectorRepository.findCollectorById(id);
        if (collector == null) {
            throw new ApiException("Collector not found");
        }

        collectorRepository.delete(collector);
    }

    // 5. Get by ID
    public Collector getCollectorById(Integer id) {
        if (collectorRepository.findCollectorById(id) == null) {
            throw new ApiException("Collector not found");
        }
        return collectorRepository.findCollectorById(id);
    }

    // 6. Get assigned container requests for a collector
    public List<ContainerRequest> getAssignedContainerRequests(Integer collectorId) {
        Collector collector = collectorRepository.findCollectorById(collectorId);
        if (collector == null) {
            throw new ApiException("Collector not found");
        }

        return containerRequestRepository.findAllByCollector(collector);
    }

    // 7. Get assigned pickup requests for a collector
    public List<PickupRequest> getAssignedPickupRequests(Integer collectorId) {
        Collector collector = collectorRepository.findCollectorById(collectorId);
        if (collector == null) {
            throw new ApiException("Collector not found");
        }

        return pickupRequestRepository.findAllByCollector(collector);
    }

}
