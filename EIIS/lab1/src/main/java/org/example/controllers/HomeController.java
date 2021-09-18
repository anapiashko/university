package org.example.controllers;

import org.example.models.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {
    private final SearchService searchService;

    @GetMapping({"/search"})
    public List<String> get(@RequestParam("input") String text) {
        return this.searchService.findFilesByInput(text);
    }

    public HomeController(final SearchService searchService) {
        this.searchService = searchService;
    }
}