package com.example.ELK.service;

import com.example.ELK.dto.*;
import com.example.ELK.exception.NotFoundException;
import com.example.ELK.model.Application;
import com.example.ELK.repository.ApplicationRepository;
import com.example.ELK.util.PdfHandler;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Service
public class ApplicationService {

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

    public Application create(ApplicationDto applicant, MultipartFile cv, MultipartFile coverLetter) throws FileNotFoundException {
        GeoResponse response = getGeoLocation(applicant.getAddress());
        File cvFile = fileHandler.saveFile(cv, "src/main/resources/cv.pdf");
        File coverLetterFile = fileHandler.saveFile(coverLetter, "src/main/resources/coverLetter.pdf");
        Application application = Application.builder()
                .firstName(applicant.getFirstName())
                .lastName(applicant.getLastName())
                .education(applicant.getEducation())
                .cvText(fileHandler.parseFile(cvFile))
                .coverLetterText(fileHandler.parseFile(coverLetterFile))
                .geoLocation(new GeoPoint(response.getData().get(0).getLatitude(), response.getData().get(0).getLongitude()))
                .build();
        return repository.save(application);
    }

    private GeoResponse getGeoLocation(String address) {
        return restTemplate
                .getForEntity("http://api.positionstack.com/v1/forward?access_key=c8f03ed78aec4f35190a78ea6e029b1b&query="
                                + address,
                        GeoResponse.class).getBody();
    }

    public List<ResultData> searchByFullName(String firstName, String lastName) {
        if(firstName.isBlank())
            return searchByLastName(lastName);
        if (lastName.isBlank())
            return searchByFirstName(firstName);
        else
            return searchByFirstNameAndLastName(firstName, lastName);
    }

    private List<ResultData> searchByLastName(String lastName) {
        QueryBuilder query;
        if(isPhraze(lastName))
            query = new MatchPhraseQueryBuilder("lastName", lastName);
        else
            query = QueryBuilders.matchQuery("lastName", lastName);
        return resultRetriever.getResults(query, List.of("lastName"));
    }

    private List<ResultData> searchByFirstName(String firstName) {
        QueryBuilder query;
        if(isPhraze(firstName))
            query = new MatchPhraseQueryBuilder("firstName", firstName);
        else
            query = QueryBuilders.matchQuery("firstName", firstName);
        return resultRetriever.getResults(query, List.of("firstName"));
    }

    private List<ResultData> searchByFirstNameAndLastName(String firstName, String lastName) {
        QueryBuilder firstNameQuery = QueryBuilders.matchQuery("firstName", firstName);
        QueryBuilder lastNameQuery = QueryBuilders.matchQuery("lastName", lastName);
        if (isPhraze(firstName))
            firstNameQuery = new MatchPhraseQueryBuilder("firstName", firstName);
        if (isPhraze(lastName))
            lastNameQuery = new MatchPhraseQueryBuilder("lastName", lastName);

        QueryBuilder query = QueryBuilders.boolQuery()
                .must(firstNameQuery)
                .must(lastNameQuery);
        return resultRetriever.getResults(query, List.of("firstName", "lastName"));
    }

    private boolean isPhraze(String text) {
        return text.charAt(0) == '\"' && text.charAt(text.length() - 1) == '\"';
    }

    public List<ResultData> searchByEducation(String education) {
        QueryBuilder query;
        if(isPhraze(education))
            query = new MatchPhraseQueryBuilder("education", education);
        else
            query = QueryBuilders.matchQuery("education", education);
        return resultRetriever.getResults(query, List.of("education"));
    }

    public List<ResultData> searchByCvText(String cvText) {
        QueryBuilder query;
        if(isPhraze(cvText))
            query = new MatchPhraseQueryBuilder("cvText", cvText);
        else
            query = QueryBuilders.matchQuery("cvText", cvText);
        return resultRetriever.getResults(query, List.of("cvText"));
    }

    public List<ResultData> searchByCoverLetterText(String coverLetterText) {
        QueryBuilder query;
        if(isPhraze(coverLetterText))
            query = new MatchPhraseQueryBuilder("coverLetterText", coverLetterText);
        else
            query = QueryBuilders.matchQuery("coverLetterText", coverLetterText);
        return resultRetriever.getResults(query, List.of("coverLetterText"));
    }

    public Application deleteById(String id) {
        Application application = repository.findById(id).orElseThrow(NotFoundException::new);
        repository.deleteById(id);
        return application;
    }
}
