package org.example.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Output {
    private String fileName;
    private Double weight;
}