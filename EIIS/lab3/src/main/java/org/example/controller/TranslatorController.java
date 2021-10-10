package org.example.controller;

import org.example.service.Translator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranslatorController {

    private final Translator translator;

    public TranslatorController(Translator translator) {
        this.translator = translator;
    }

    @PostMapping("translator")
    public String translate(@RequestBody final String text){
        return translator.translate(text);
    }
}
