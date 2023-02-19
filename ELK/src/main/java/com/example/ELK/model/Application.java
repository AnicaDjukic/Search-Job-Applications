package com.example.ELK.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "applications")
public class Application {

    @Id
    @Field(type = FieldType.Text)
    private String id;

    @Field(type = FieldType.Text)
    private String firstName;

    @Field(type = FieldType.Text)
    private String lastName;

    @Field(type = FieldType.Text)
    private String education;

    @Field(type = FieldType.Text)
    private String cvText;

    @Field(type = FieldType.Text)
    private String coverLetterText;

    @GeoPointField
    private GeoPoint geoLocation;

    @Field(type = FieldType.Text, index = false)
    private String cvPath;

    @Field(type = FieldType.Text, index = false)
    private String coverLetterPath;

    @Field(type = FieldType.Text, index = false)
    private String address;
}
