package com.example.ELK.service;

import com.example.ELK.dto.IndexDto;
import com.example.ELK.dto.ResultData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResultRetriever {

    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http")));

    public List<ResultData> getResults(QueryBuilder query) {
        SearchRequest searchRequest = new SearchRequest("applications");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query);
        searchRequest.source(searchSourceBuilder);
        List<IndexDto> applications = new ArrayList<>();
        List<ResultData> result = new ArrayList<>();
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                org.elasticsearch.search.SearchHit[] searchHit = searchResponse.getHits().getHits();
                for (org.elasticsearch.search.SearchHit hit : searchHit) {
                    applications.add(new ObjectMapper().convertValue(hit.getSourceAsMap(), IndexDto.class));
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (var a: applications) {
            System.out.println(a);
            result.add(new ResultData(a.getFirstName(), a.getLastName(), a.getEducation(), a.getCvText()));
        }
        return result;
    }
}
