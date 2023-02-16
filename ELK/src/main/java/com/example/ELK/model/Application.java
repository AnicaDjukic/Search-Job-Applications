package com.example.ELK.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "applications")
public class Application {

    @Id
    @Field(type = FieldType.Text, store = true)
    private String filename;

    @Field(type = FieldType.Text, store = true)
    private String firstName;

    @Field(type = FieldType.Text, store = true)
    private String lastName;

    @Field(type = FieldType.Text, store = true)
    private String education;

    @Field(type = FieldType.Text, store = true)
    private String cvText;

    @Field(type = FieldType.Text, store = true)
    private String coverLetterText;

    @GeoPointField
    private GeoPoint geoLocation;
}
