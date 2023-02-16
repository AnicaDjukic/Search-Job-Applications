package com.example.ELK.service;

import com.example.ELK.dto.ApplicationDto;
import com.example.ELK.model.Application;
import com.example.ELK.repository.ApplicationRepository;
import com.example.ELK.util.PdfHandler;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository repository;

    private final PdfHandler fileHandler;

    public ApplicationService(ApplicationRepository repository, PdfHandler fileHandler) {
        this.repository = repository;
        this.fileHandler = fileHandler;
    }

    public Application create(ApplicationDto dto) {
        Application application = Application.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .education(dto.getEducation())
//                .cvText(fileHandler.parseFile(new File(cv.getName())))
//                .coverLetterText(fileHandler.parseFile(new File(coverLetter.getName())))
                .cvText("cvText")
                .coverLetterText("CoverLetter")
                .geoLocation(new GeoPoint(12.2, 13.3))
                .build();
        return repository.save(application);
    }

    public List<Application> searchByFullName(String firstName, String lastName) {
        return repository.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<Application> searchByEducation(String education) {
        return repository.findByEducation(education);
    }

    public List<Application> searchByCvText(String cvText) {
        return repository.findByCvText(cvText);
    }

    public List<Application> searchByCoverLetterText(String coverLetterText) {
        return repository.findByCoverLetterText(coverLetterText);
    }
}
