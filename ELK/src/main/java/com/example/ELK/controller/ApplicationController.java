package com.example.ELK.controller;

import com.example.ELK.dto.ApplicationDto;
import com.example.ELK.dto.ResponseDto;
import com.example.ELK.model.Application;
import com.example.ELK.service.ApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/application")
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
        Application newApplication = service.create(dto);
        return modelMapper.map(newApplication, ResponseDto.class);
    }


}
