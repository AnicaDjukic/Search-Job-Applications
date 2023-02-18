package com.example.ELK.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BooleanBetweenFields {

    private String field1;
    private String value1;
    private String field2;
    private String value2;
    private String operation;
}
