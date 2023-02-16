package com.example.ELK.repository;

import com.example.ELK.model.Application;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ApplicationRepository extends ElasticsearchRepository<Application, String> {

    List<Application> findByFirstNameAndLastName(String firstName, String lastName);

    List<Application> findByEducation(String education);

    List<Application> findByCvText(String cvText);

    List<Application> findByCoverLetterText(String coverLetterText);
}
