package com.example.ELK.service;

import com.example.ELK.dto.ApplicationDto;
import com.example.ELK.model.Application;
import com.example.ELK.repository.ApplicationRepository;
import com.example.ELK.util.PdfHandler;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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
}
