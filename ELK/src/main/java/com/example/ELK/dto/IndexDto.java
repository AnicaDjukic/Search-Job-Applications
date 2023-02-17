package com.example.ELK.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IndexDto {

    private String _class;

    private String id;

    private String firstName;

    private String lastName;

    private String education;

    private String cvText;

    private String coverLetterText;

    private GeoPoint geoLocation;
}
