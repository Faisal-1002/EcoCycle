package com.example.capstone03.Controller;

import com.example.capstone03.Api.ApiResponse;
import com.example.capstone03.Model.Collector;
import com.example.capstone03.Service.CollectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api/v1/collector")
@RequiredArgsConstructor

public class CollectorController {

    private final CollectorService collectorService;


    @GetMapping("/get")
    public ResponseEntity getCollector(){
        return ResponseEntity.status(200).body(collectorService.getAllCollectors());
    }

    @PostMapping("/add")
    public ResponseEntity addCollector(@RequestBody @Valid Collector collector){
        collectorService.addCollector(collector);
        return ResponseEntity.status(200).body(new ApiResponse("Collector Added"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updateCollector(@PathVariable Integer id, @RequestBody @Valid Collector collector ){
        collectorService.updateCollector(id,collector);
        return ResponseEntity.status(200).body(new ApiResponse("Collector updated"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteContainer(@PathVariable Integer id ){
        collectorService.delete(id);
        return ResponseEntity.status(200).body(new ApiResponse("Collector deleted"));
    }


}
