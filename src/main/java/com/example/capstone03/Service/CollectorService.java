package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.Model.Collector;
import com.example.capstone03.Repository.CollectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectorService {

    private final CollectorRepository collectorRepository;

    public List<Collector> getAllCollectors(){
        return collectorRepository.findAll();
    }

    public void addCollector(Collector collector){
        collectorRepository.save(collector);
    }

    public void updateCollector(Integer id, Collector collector){
        Collector oldCollector = collectorRepository.findCollectorById(id);
        if (oldCollector== null){
            throw new ApiException("Collector not found");
        }

        oldCollector.setName(collector.getName());
        oldCollector.setEmail(collector.getEmail());
        oldCollector.setPhone_number(collector.getPhone_number());
        oldCollector.setAddress(collector.getAddress());

        collectorRepository.save(oldCollector);
    }


    public void delete(Integer id){
        Collector oldCollector = collectorRepository.findCollectorById(id);
        if (oldCollector == null){
            throw new ApiException("Collector not found");
        }
        collectorRepository.delete(oldCollector);
    }






}
