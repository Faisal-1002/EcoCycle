package com.example.capstone03.Controller;

import com.example.capstone03.Api.ApiResponse;
import com.example.capstone03.Model.RecyclingCompany;
import com.example.capstone03.Service.RecyclingCompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recycling-company")
@RequiredArgsConstructor
public class RecyclingCompanyController {

    private final RecyclingCompanyService recyclingCompanyService;

    @GetMapping("/get")
    public ResponseEntity getAllRecyclingCompany(){
        return ResponseEntity.status(200).body(recyclingCompanyService.getAllRecyclingCompany());

    }
    @PostMapping("/add")
    public ResponseEntity addRecyclingCompany(@RequestBody @Valid RecyclingCompany recyclingCompany){
        recyclingCompanyService.addRecyclingCompany(recyclingCompany);
        return ResponseEntity.status(200).body(new ApiResponse("added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateCoffee(@PathVariable Integer id, @RequestBody @Valid RecyclingCompany recyclingCompany){
        recyclingCompanyService.updateRecyclingCompany(id, recyclingCompany);
        return ResponseEntity.status(200).body(new ApiResponse("updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCoffee(@PathVariable Integer id){
        recyclingCompanyService.deleteRecyclingCompany(id);
        return ResponseEntity.status(200).body(new ApiResponse("deleted successfully"));
    }
}
