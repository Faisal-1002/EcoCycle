package com.example.capstone03.Service;

import com.example.capstone03.Api.ApiException;
import com.example.capstone03.DTO.ContainerDTO;
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

    public List<Container> getAllContainers(){
        return containerRepository.findAll();
    }

    public void addContainer(ContainerDTO containerDTO){
        User user= userRepository.findUserById(containerDTO.getUser_id());

        if (user == null){
            throw new ApiException("user not found");
        }

        Container container = new Container(null, containerDTO.getIs_available(), user);
        containerRepository.save(container);
    }


    public void updateContainer(ContainerDTO containerDTO){
        Container container = containerRepository.findContainerById(containerDTO.getUser_id());

        if(container == null){
            throw new ApiException("Container not found");
        }

        container.setIs_available(containerDTO.getIs_available());

        containerRepository.save(container);
    }


    public void deleteContainer(ContainerDTO containerDTO){
        Container container = containerRepository.findContainerById(containerDTO.getUser_id());
        if(container == null){
            throw new ApiException("Container not found");
        }

        containerRepository.save(container);
    }




}
