package com.example.ELK.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.elasticsearch.common.geo.GeoPoint;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseDto {

    private String id;

    private String firstName;

    private String lastName;

    private String education;

    private String cvText;

    private String coverLetterText;

    private String address;

    private GeoPoint geoLocation;
}
