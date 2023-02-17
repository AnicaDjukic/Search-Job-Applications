package com.example.ELK.repository;

import com.example.ELK.model.Application;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ApplicationRepository extends ElasticsearchRepository<Application, String> {
}
