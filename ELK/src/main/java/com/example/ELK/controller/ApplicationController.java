package com.example.ELK.controller;

import com.example.ELK.dto.ApplicationDto;
import com.example.ELK.dto.ResponseDto;
import com.example.ELK.model.Application;
import com.example.ELK.service.ApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.*;

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

//    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public ResponseDto add(@RequestPart("application") ApplicationDto dto,
//                           @RequestPart("cv") MultipartFile cv,
//                           @RequestPart("coverLetter") MultipartFile coverLetter,
//                           HttpServletRequest request) {
//        Application newApplication = service.create(dto, cv, coverLetter);
//        return modelMapper.map(newApplication, ResponseDto.class);
//    }

    @PostMapping
    public ResponseDto add(@RequestBody ApplicationDto dto,
                           HttpServletRequest request) {
        // TODO: add cv and cover letter
        // TODO: ip location
        Application newApplication = service.create(dto);
        return modelMapper.map(newApplication, ResponseDto.class);
    }

    @GetMapping("full-name")
    public List<ResponseDto> searchByFullName(@RequestParam String firstName, @RequestParam String lastName) {
        return modelMapper.map(service.searchByFullName(firstName, lastName), new TypeToken<ArrayList<ResponseDto>>() {}.getType() );
    }

    @GetMapping("education")
    public List<ResponseDto> searchByEducation(@RequestParam String education) {
        return modelMapper.map(service.searchByEducation(education), new TypeToken<ArrayList<ResponseDto>>() {}.getType() );
    }

    @GetMapping("cv")
    public List<ResponseDto> searchByCvText(@RequestParam String cv) {
        return modelMapper.map(service.searchByCvText(cv), new TypeToken<ArrayList<ResponseDto>>() {}.getType() );
    }

    @GetMapping("cover-letter")
    public List<ResponseDto> searchByCoverLetterText(@RequestParam String coverLetter) {
        return modelMapper.map(service.searchByCoverLetterText(coverLetter), new TypeToken<ArrayList<ResponseDto>>() {}.getType() );
    }






}
