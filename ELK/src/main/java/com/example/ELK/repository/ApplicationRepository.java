package com.example.ELK.repository;

import com.example.ELK.model.Application;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ApplicationRepository extends ElasticsearchRepository<Application, String> {

    List<Application> findAllByFirstNameAndLastName(String firstName, String lastName);

    List<Application> findByEducation(String education);

    List<Application> findByCvText(String cvText);

    List<Application> findByCoverLetterText(String coverLetterText);

    @Query("""
            {
              "multi_match": {
                "query": "?0",
                "fields": [
                  "firstName"
                ],
                "analyzer": "standard"
              }
            }""")
    @Highlight(
            fields = {
                    @HighlightField(name = "education")
            }
    )
    List<SearchHit<Application>> findByFirstName(String firstName);
}
