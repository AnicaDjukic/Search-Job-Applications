package com.example.ELK.repository;

import com.example.ELK.model.Application;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ApplicationRepository extends ElasticsearchRepository<Application, String> {

    List<Application> findAllByFirstNameAndLastName(String firstName, String lastName);
}
