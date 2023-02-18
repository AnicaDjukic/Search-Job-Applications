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
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResultRetriever {

    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http")));

    public List<ResultData> getResults(QueryBuilder query, List<String> highlightedFields) {
        SearchRequest searchRequest = new SearchRequest("applications");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightAllQueryField =
                new HighlightBuilder.Field("*");
        highlightAllQueryField.highlighterType("unified");
        highlightBuilder.field(highlightAllQueryField);
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.query(query);
        searchRequest.source(searchSourceBuilder);
        List<IndexDto> applications = new ArrayList<>();
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                org.elasticsearch.search.SearchHit[] searchHit = searchResponse.getHits().getHits();
                for (org.elasticsearch.search.SearchHit hit : searchHit) {
                    IndexDto index = new ObjectMapper().convertValue(hit.getSourceAsMap(), IndexDto.class);
                    StringBuilder highlights = new StringBuilder();
                    for(String field : highlightedFields) {
                        HighlightField hf = hit.getHighlightFields().get(field);
                        if(hf != null)
                            highlights.append(hf.fragments()[0].toString()).append(" ");
                    }
                    index.setHighlight(highlights.toString());
                    applications.add(index);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<ResultData> result = new ArrayList<>();
        for (var a: applications)
            result.add(new ResultData(a.getFirstName(), a.getLastName(), a.getEducation(), a.getHighlight()));

        return result;
    }

    public List<ResultData> getResultsForGeoSearch(QueryBuilder query) {
        SearchRequest searchRequest = new SearchRequest("applications");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query);
        searchRequest.source(searchSourceBuilder);
        List<IndexDto> applications = new ArrayList<>();
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                org.elasticsearch.search.SearchHit[] searchHit = searchResponse.getHits().getHits();
                for (org.elasticsearch.search.SearchHit hit : searchHit) {
                    IndexDto index = new ObjectMapper().convertValue(hit.getSourceAsMap(), IndexDto.class);
                    applications.add(index);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<ResultData> result = new ArrayList<>();
        for (var a: applications)
            result.add(new ResultData(a.getFirstName(), a.getLastName(), a.getEducation(), a.getHighlight()));

        return result;
    }
}
