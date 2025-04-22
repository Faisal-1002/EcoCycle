package com.example.capstone03.Controller;

import com.example.capstone03.Api.ApiResponse;
import com.example.capstone03.Model.CompanyRequest;
import com.example.capstone03.Service.CompanyRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/companyRequest")
@RequiredArgsConstructor
public class CompanyRequestController {

    private final CompanyRequestService companyRequestService;

    @GetMapping("/get")
    public ResponseEntity getAllCompanyRequest(){
        return ResponseEntity.ok(companyRequestService.getAllCompanyRequest());
    }
    @PostMapping("/add")
    public ResponseEntity addCompanyRequest(@RequestBody @Valid CompanyRequest companyRequest){
        companyRequestService.addCompanyRequest(companyRequest);
        return ResponseEntity.ok(new ApiResponse("added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity update(@PathVariable Integer id, @RequestBody @Valid CompanyRequest companyRequest){
        companyRequestService.updateCompanyRequest(id, companyRequest);
        return ResponseEntity.ok(new ApiResponse("update successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        companyRequestService.deleteCompanyRequest(id);
        return ResponseEntity.ok(new ApiResponse("delete successfully"));

    }
}
