package com.example.capstone03.Controller;


import com.example.capstone03.Api.ApiResponse;
import com.example.capstone03.DTO.ContainerDTO;
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
    public ResponseEntity addContainer(@RequestBody @Valid ContainerDTO containerDTO){
        containerService.addContainer(containerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Container Added"));
    }



    @PutMapping("/update/{containerDTO}")
    public ResponseEntity updateContainerDTO(@PathVariable ContainerDTO containerDTO ){
        containerService.updateContainer(containerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Container updated"));

    }

    @DeleteMapping("/delete/{containerDTO}")
    public ResponseEntity deleteContainerDTO(@PathVariable ContainerDTO containerDTO ){
        containerService.deleteContainer(containerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Container deleted"));
    }


}
