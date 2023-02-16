package com.example.ELK.service;

import com.example.ELK.dto.ApplicationDto;
import com.example.ELK.dto.GeoResponse;
import com.example.ELK.dto.ResultData;
import com.example.ELK.model.Application;
import com.example.ELK.repository.ApplicationRepository;
import com.example.ELK.util.PdfHandler;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository repository;

    private final PdfHandler fileHandler;

    private final ElasticsearchOperations elasticsearchTemplate;

    private final RestTemplate restTemplate;

    public ApplicationService(ApplicationRepository repository, PdfHandler fileHandler, ElasticsearchOperations elasticsearchTemplate, RestTemplate restTemplate) {
        this.repository = repository;
        this.fileHandler = fileHandler;
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.restTemplate = restTemplate;
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

    public List<Application> searchByFullName(String firstName, String lastName) {
        return repository.findAllByFirstNameAndLastName(firstName, lastName);
    }

    public List<ResultData> searchByFirstName(String firstName) {
        co.elastic.clients.elasticsearch._types.query_dsl.Query query =
                QueryBuilders.termQueryAsQuery("firstName", firstName);
        HighlightField field = new HighlightField("education");
        List<HighlightField> fields = new ArrayList<>();
        fields.add(field);

        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(query)
                .withHighlightQuery(new HighlightQuery(new Highlight(fields), Application.class))
                .build();
        List<SearchHit<Application>> sampleEntities = elasticsearchTemplate.search(searchQuery, Application.class).stream().toList();
        List<ResultData> applications = new ArrayList<>();
        for (var a: sampleEntities) {
            System.out.println(a);
            applications.add(new ResultData(a.getContent().getFirstName(), a.getContent().getLastName(), a.getContent().getEducation(), a.getContent().getCvText()));
        }
        return applications;
    }

    public List<Application> searchByFirstName1(String firstName) {
        List<SearchHit<Application>> searchHits = repository.findByFirstName(firstName);
        System.out.println(searchHits);
        List<Application> applications = new ArrayList<>();
        for (SearchHit<Application> a: searchHits) {
            System.out.println(a);
            applications.add(a.getContent());
        }
        return applications;
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
