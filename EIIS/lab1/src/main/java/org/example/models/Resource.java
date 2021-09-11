package org.example.models;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Resource {

    private String filePath;
    private Map<String, Double> words;
}
