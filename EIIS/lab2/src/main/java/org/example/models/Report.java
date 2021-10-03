package org.example.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class Report {
    private String name;
    private Set<String> keyWords;
    private Set<String> classicReport;
}
