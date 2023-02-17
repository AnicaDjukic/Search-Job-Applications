package com.example.ELK.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AdvancedSearch {

    private List<AdvancedSearchParam> params;
}
