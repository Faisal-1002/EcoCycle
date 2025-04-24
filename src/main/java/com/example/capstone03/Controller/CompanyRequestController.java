package com.example.capstone03.Controller;

import com.example.capstone03.Api.ApiResponse;
import com.example.capstone03.Model.CompanyRequest;
import com.example.capstone03.Service.CompanyRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/company-request")
@RequiredArgsConstructor
public class CompanyRequestController {

    private final CompanyRequestService companyRequestService;

    @GetMapping("/get")
    public ResponseEntity getAllCompanyRequest(){
        return ResponseEntity.status(200).body(companyRequestService.getAllCompanyRequest());
    }

    @PostMapping("/add/{recyclingCompanyId}")
    public ResponseEntity addCompanyRequest(@PathVariable Integer recyclingCompanyId , @RequestBody @Valid CompanyRequest companyRequest){
        companyRequestService.addCompanyRequest(recyclingCompanyId,companyRequest);
        return ResponseEntity.status(200).body(new ApiResponse("added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity update(@PathVariable Integer id, @RequestBody @Valid CompanyRequest companyRequest){
        companyRequestService.updateCompanyRequest(id, companyRequest);
        return ResponseEntity.status(200).body(new ApiResponse("update successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        companyRequestService.deleteCompanyRequest(id);
        return ResponseEntity.status(200).body(new ApiResponse("delete successfully"));
    }

    @GetMapping("/get-pending-requests")
    public ResponseEntity<?> getPendingRequests(){
        return ResponseEntity.status(200).body(companyRequestService.pendingRequests());
    }

    @PutMapping("/accept-company-request/{companyRequestId}/collector/{collectorId}")
    public ResponseEntity<?> acceptRecyclingCompanyRequest(@PathVariable Integer companyRequestId,@PathVariable Integer collectorId) {
        companyRequestService.acceptCompanyRequest(companyRequestId,collectorId);
        return ResponseEntity.status(200).body(new ApiResponse("recycling company request accepted"));
    }

    @GetMapping("/get-delivered-requests")
    public ResponseEntity<?> getDeliveredRequests(){
        return ResponseEntity.status(200).body(companyRequestService.getDeliveredRequest());
    }

    @PutMapping("/deliver-company-request/{companyRequestId}/collector/{collectorId}")
    public ResponseEntity<?> deliverRecyclingCompanyRequest(@PathVariable Integer companyRequestId,@PathVariable Integer collectorId) {
        companyRequestService.deliverRequest(companyRequestId,collectorId);
        return ResponseEntity.status(200).body(new ApiResponse("recycling company request delivered"));
    }

}
