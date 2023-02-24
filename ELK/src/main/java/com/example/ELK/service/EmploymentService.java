package com.example.ELK.service;

import com.example.ELK.dto.EmploymentDto;
import com.example.ELK.dto.IpGeoResponse;
import com.example.ELK.dto.PremiumRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmploymentService {

    private static final String IP_GEO_API_PATH = "http://ip-api.com/json/";

    private static final Logger log = LoggerFactory.getLogger(EmploymentService.class);

    private final RestTemplate restTemplate;

    public EmploymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
