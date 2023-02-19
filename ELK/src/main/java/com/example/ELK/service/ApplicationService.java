package com.example.ELK.service;

import com.example.ELK.controller.ELKController;
import com.example.ELK.dto.*;
import com.example.ELK.exception.NotFoundException;
import com.example.ELK.model.Application;
import com.example.ELK.repository.ApplicationRepository;
import com.example.ELK.util.PdfHandler;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.elasticsearch.common.geo.GeoPoint;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ApplicationService {

    private static final String CV = "cv";

    private static final String COVER_LETTER = "coverLetter";

    private static final String EXTENSION = ".pdf";

    private static final String LOCAL_PATH = "src/main/resources/files/";

    private static final String GEO_API_PATH = "http://api.positionstack.com/v1/forward?access_key=c8f03ed78aec4f35190a78ea6e029b1b&query=";

    private static final String IP_GEO_API_PATH = "https://api.ipgeolocation.io/ipgeo?apiKey=9893ea335f724992b56c50e3c6d8efe8&ip=";

    private static final Logger log = LoggerFactory.getLogger(ELKController.class);

    private final ApplicationRepository repository;

    private final PdfHandler fileHandler;

    private final RestTemplate restTemplate;

    private final ResultRetriever resultRetriever;

    public ApplicationService(ApplicationRepository repository, PdfHandler fileHandler, RestTemplate restTemplate, ResultRetriever resultRetriever) {
        this.repository = repository;
        this.fileHandler = fileHandler;
        this.restTemplate = restTemplate;
        this.resultRetriever = resultRetriever;
    }

    public Application create(ApplicationDto applicant, MultipartFile cv, MultipartFile coverLetter) {
        String cvPath = CV + UUID.randomUUID() + EXTENSION;
        String coverLetterPath = COVER_LETTER + UUID.randomUUID() + EXTENSION;
        File cvFile = fileHandler.saveFile(cv, LOCAL_PATH + cvPath);
        File coverLetterFile = fileHandler.saveFile(coverLetter, LOCAL_PATH + coverLetterPath);
        GeoPoint geoPoint = getGeoLocation(applicant.getAddress());
        Application application = Application.builder()
                .firstName(applicant.getFirstName())
                .lastName(applicant.getLastName())
                .education(applicant.getEducation())
                .cvText(fileHandler.parseFile(cvFile))
                .coverLetterText(fileHandler.parseFile(coverLetterFile))
                .cvPath(cvPath)
                .coverLetterPath(coverLetterPath)
                .address(applicant.getAddress())
                .geoLocation(geoPoint)
                .build();
        return repository.save(application);
    }

    private GeoPoint getGeoLocation(String address) {
        GeoResponse response = restTemplate.getForEntity(GEO_API_PATH + address, GeoResponse.class).getBody();
        return new GeoPoint(response.getData().get(0).getLatitude(), response.getData().get(0).getLongitude());
    }

    public List<ResultData> search(String field, String text) {
        QueryBuilder query = getQuery(field, text);
        return resultRetriever.getResults(query, List.of(field));
    }

    public QueryBuilder getQuery(String field, String text) {
        if(isPhrase(text))
            return new MatchPhraseQueryBuilder(field, text);
        return QueryBuilders.matchQuery(field, text);
    }

    public List<ResultData> booleanSearch(BooleanBetweenFields booleanInfo) {
        org.elasticsearch.index.query.QueryBuilder query1 = getQuery(booleanInfo.getField1(), booleanInfo.getValue1());
        org.elasticsearch.index.query.QueryBuilder query2 = getQuery(booleanInfo.getField2(), booleanInfo.getValue2());

        BoolQueryBuilder boolQuery = getBoolQuery(booleanInfo, query1, query2);

        List<String> fieldsForHighlight = List.of(booleanInfo.getField1(), booleanInfo.getField2());
        return resultRetriever.getResults(boolQuery, fieldsForHighlight);
    }

    private BoolQueryBuilder getBoolQuery(BooleanBetweenFields booleanInfo, QueryBuilder query1, QueryBuilder query2) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (booleanInfo.getOperation().equalsIgnoreCase(BooleanOperation.AND.toString())) {
            boolQuery.must(query1);
            boolQuery.must(query2);
        } else if (booleanInfo.getOperation().equalsIgnoreCase(BooleanOperation.OR.toString())) {
            boolQuery.should(query1);
            boolQuery.should(query2);
        } else if (booleanInfo.getOperation().equalsIgnoreCase(BooleanOperation.NOT.toString())) {
            boolQuery.must(query1);
            boolQuery.mustNot(query2);
        }
        return boolQuery;
    }

    public List<ResultData> advancedSearch(AdvancedSearch advancedSearchInfo) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        List<String> fieldsForHighlight = new ArrayList<>();
        for(AdvancedSearchParam param : advancedSearchInfo.getParams()) {
            if(param.getOperation().equals(Operations.MUST.toString())) {
                query.must(getQuery(param.getField(), param.getValue()));
            } else {
                query.should(getQuery(param.getField(), param.getValue()));
            }
            fieldsForHighlight.add(param.getField());
        }
        return resultRetriever.getResults(query, fieldsForHighlight);
    }

    private boolean isPhrase(String text) {
        return text.charAt(0) == '\"' && text.charAt(text.length() - 1) == '\"';
    }

    public List<ResultData> geoLocationSearch(GeoLocationSearch geoLocationInfo) {
        GeoPoint cityGeoPoint = getGeoLocation(geoLocationInfo.getCity());
        GeoDistanceQueryBuilder query = QueryBuilders
                .geoDistanceQuery("geoLocation")
                .point(cityGeoPoint.getLat(), cityGeoPoint.getLon())
                .distance(geoLocationInfo.getRadius(), DistanceUnit.KILOMETERS);
        return resultRetriever.getResultsForGeoSearch(query);
    }

    public Application deleteById(String id) {
        Application application = repository.findById(id).orElseThrow(NotFoundException::new);
        repository.deleteById(id);
        return application;
    }

    public void receivePremiumRequest(PremiumRequest premiumRequest) {
        String city = findCityByIpAddress(premiumRequest.getIpAddress());
        log.info("Premium request from city: {}", city);
    }

    private String findCityByIpAddress(String ipAddress) {
        return restTemplate.getForEntity(IP_GEO_API_PATH + ipAddress, IpGeoResponse.class).getBody().getCity();
    }

    public void receiveSuccessfulEmployment(EmploymentDto employmentDto) {
        log.info("Employment from employee {}", employmentDto.getEmployee());
        log.info("New employment in company {}", employmentDto.getCompany());
    }
}
