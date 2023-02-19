package com.example.ELK.controller;

import com.example.ELK.dto.*;
import com.example.ELK.model.Application;
import com.example.ELK.service.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLConnection;
import java.util.List;

@RestController
@RequestMapping("api/v1/applications")
public class ApplicationController {

    private final ApplicationService service;

    private final ModelMapper modelMapper = new ModelMapper();

    private static final String LOCAL_PATH = "src/main/resources/files/";

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
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

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("search")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ResultData> search(@RequestParam String field, @RequestParam String text) {
        return service.search(field, text);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("search/boolean")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ResultData> search(@RequestBody BooleanBetweenFields booleanInfo) {
        return service.booleanSearch(booleanInfo);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("search/advanced")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ResultData> advancedSearch(@RequestBody AdvancedSearch advancedSearchInfo) {
        return service.advancedSearch(advancedSearchInfo);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("search/geo")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ResultData> geoLocationSearch(@RequestBody GeoLocationSearch geoLocationInfo) {
        return service.geoLocationSearch(geoLocationInfo);
    }

    @PostMapping("premium/request")
    @ResponseStatus(value = HttpStatus.OK)
    public void receivePremiumRequest(@RequestBody PremiumRequest premiumRequest) {
        service.receivePremiumRequest(premiumRequest);
    }

    @PostMapping("employment")
    @ResponseStatus(value = HttpStatus.OK)
    public void receiveSuccessfulEmployment(@RequestBody EmploymentDto employmentDto) {
        service.receiveSuccessfulEmployment(employmentDto);
    }

    @GetMapping("file/download/{fileName}")
    public void downloadPDFResource(HttpServletResponse response, @PathVariable String fileName) throws IOException {
        File file = new File(LOCAL_PATH + fileName);
        if (file.exists()) {

            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
            response.setContentLength((int) file.length());
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());

        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseDto delete(@PathVariable String id) {
        return  modelMapper.map(service.deleteById(id), ResponseDto.class);
    }

}
