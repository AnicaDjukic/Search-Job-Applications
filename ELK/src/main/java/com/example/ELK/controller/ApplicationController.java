package com.example.ELK.controller;

import com.example.ELK.dto.*;
import com.example.ELK.model.Application;
import com.example.ELK.service.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("search")
    public List<ResultData> search(@RequestParam String field, @RequestParam String text) {
        return service.search(field, text);
    }

    @PostMapping("search/boolean")
    public List<ResultData> search(@RequestBody BooleanBetweenFields booleanInfo) {
        return service.booleanSearch(booleanInfo);
    }

    @PostMapping("search/advanced")
    public List<ResultData> advancedSearch(@RequestBody AdvancedSearch advancedSearchInfo) {
        return service.advancedSearch(advancedSearchInfo);
    }

    @PostMapping("search/geo")
    public List<ResultData> geoLocationSearch(@RequestBody GeoLocationSearch geoLocationInfo) {
        return service.geoLocationSearch(geoLocationInfo);
    }

    @DeleteMapping("{id}")
    public ResponseDto delete(@PathVariable String id) {
        return  modelMapper.map(service.deleteById(id), ResponseDto.class);
    }

}
