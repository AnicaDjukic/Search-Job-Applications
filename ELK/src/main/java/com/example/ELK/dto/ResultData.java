package com.example.ELK.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResultData {

    private String firstName;

    private String lastName;

    private String education;

    private String highlight;

    private String cvPath;

    private String coverLetterPath;
}
