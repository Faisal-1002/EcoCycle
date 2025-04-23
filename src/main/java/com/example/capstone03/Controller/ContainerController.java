package com.example.capstone03.Controller;

import com.example.capstone03.Api.ApiResponse;
import com.example.capstone03.Model.Container;
import com.example.capstone03.Service.ContainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/container")
@RequiredArgsConstructor
public class ContainerController {

    private final ContainerService containerService;

    @GetMapping("/get")
    public ResponseEntity getContainer(){
        return ResponseEntity.status(200).body(containerService.getAllContainers());
    }

    @PostMapping("/add")
    public ResponseEntity addContainer(@RequestBody @Valid Container container){
        containerService.addContainer(container);
        return ResponseEntity.status(200).body(new ApiResponse("Container Added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateContainerDTO(@PathVariable Integer id, @RequestBody @Valid Container container){
        containerService.updateContainer(id, container);
        return ResponseEntity.status(200).body(new ApiResponse("Container updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteContainerId(@PathVariable Integer id){
        containerService.deleteContainer(id);
        return ResponseEntity.status(200).body(new ApiResponse("Container deleted"));
    }

    @GetMapping("/get/{containerId}")
    public ResponseEntity<?> getContainerById(@PathVariable Integer containerId) {
        return ResponseEntity.status(200).body(containerService.getContainerById(containerId));
    }


}
