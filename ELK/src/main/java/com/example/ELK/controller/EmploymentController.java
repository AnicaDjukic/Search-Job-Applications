package com.example.ELK.controller;

import com.example.ELK.dto.EmploymentDto;
import com.example.ELK.dto.PremiumRequest;
import com.example.ELK.service.EmploymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/employment")
public class EmploymentController {

    private final EmploymentService service;

    public EmploymentController(EmploymentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public void receiveSuccessfulEmployment(@RequestBody EmploymentDto employmentDto) {
        service.receiveSuccessfulEmployment(employmentDto);
    }

    @PostMapping("premium/request")
    @ResponseStatus(value = HttpStatus.OK)
    public void receivePremiumRequest(@RequestBody PremiumRequest premiumRequest) {
        service.receivePremiumRequest(premiumRequest);
    }

}
