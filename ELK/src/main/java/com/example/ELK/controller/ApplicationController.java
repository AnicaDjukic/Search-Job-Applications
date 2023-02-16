package com.example.ELK.controller;

import com.example.ELK.dto.ApplicationDto;
import com.example.ELK.dto.ResponseDto;
import com.example.ELK.dto.ResultData;
import com.example.ELK.model.Application;
import com.example.ELK.service.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/applications")
public class ApplicationController {

    private final ApplicationService service;

    private final ModelMapper modelMapper = new ModelMapper();

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseDto add(@RequestParam("applicant") String dto,
                            @RequestParam("cv") MultipartFile cv,
                           @RequestParam("coverLetter") MultipartFile coverLetter) throws IOException {
        ApplicationDto applicant = getJson(dto);
        Application newApplication = service.create(applicant, cv, coverLetter);
        return modelMapper.map(newApplication, ResponseDto.class);
    }

    private ApplicationDto getJson(String dto) {
        ApplicationDto applicant = new ApplicationDto();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            applicant = objectMapper.readValue(dto, ApplicationDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return applicant;
    }

    @GetMapping("full-name")
    public List<ResultData> searchByFullName(@RequestParam String firstName, @RequestParam String lastName) {
        return modelMapper.map(service.searchByFullName(firstName, lastName), new TypeToken<ArrayList<ResultData>>() {}.getType() );
    }

    @GetMapping("first-name")
    public List<ResultData> searchByFirstName(@RequestParam String firstName) {
        return service.searchByFirstName(firstName);
    }

    @GetMapping("education")
    public List<ResultData> searchByEducation(@RequestParam String education) {
        return modelMapper.map(service.searchByEducation(education), new TypeToken<ArrayList<ResultData>>() {}.getType() );
    }

    @GetMapping("cv")
    public List<ResultData> searchByCvText(@RequestParam String cv) {
        return modelMapper.map(service.searchByCvText(cv), new TypeToken<ArrayList<ResultData>>() {}.getType() );
    }

    @GetMapping("cover-letter")
    public List<ResultData> searchByCoverLetterText(@RequestParam String coverLetter) {
        return modelMapper.map(service.searchByCoverLetterText(coverLetter), new TypeToken<ArrayList<ResultData>>() {}.getType() );
    }






}
