package org.example.controllers;

import org.example.services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @GetMapping(value = "recognize-language")
    public String getLanguage(@RequestParam(name = "filename") final String filename) throws Exception{
        return languageService.recognizeLanguage(filename);
    }

}