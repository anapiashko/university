package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.models.Report;
import org.example.services.ReportServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportServiceImpl reportService;

    @GetMapping("/report")
    public Report getReport(@RequestParam(name = "name") final String reportName) throws Exception {
        return reportService.getReport(reportName);
    }
}
