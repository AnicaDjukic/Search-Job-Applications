package com.example.ELK.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AdvancedSearchParam {

    private String field;
    private String value;
    private String operation;
}
