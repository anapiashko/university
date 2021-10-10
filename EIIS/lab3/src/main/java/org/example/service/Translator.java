package org.example.service;

import org.example.models.Dictionary;
import org.example.models.Function;
import org.example.models.Position;
import org.example.repository.DictionaryRepository;
import org.example.repository.FunctionRepository;
import org.example.repository.FunctionWordRepository;
import org.example.repository.PositionRepository;
import org.example.util.ConstantUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.example.util.ConstantUtils.TRANSLATION_ERROR;
import static org.example.util.ConstantUtils.WORD_NOT_FOUND_REGEX;

@Service
public class Translator {

    private final DictionaryRepository dictionaryRepository;
    private final FunctionRepository functionRepository;
    private final FunctionWordRepository functionWordRepository;
    private final PositionRepository positionRepository;

    public Translator(DictionaryRepository dictionaryRepository, FunctionRepository functionRepository, FunctionWordRepository functionWordRepository, PositionRepository positionRepository) {
        this.dictionaryRepository = dictionaryRepository;
        this.functionRepository = functionRepository;
        this.functionWordRepository = functionWordRepository;
        this.positionRepository = positionRepository;
    }

    public String translate(final String text){
        List<Function> allFunctions = functionRepository.findAll();
        String textWithoutPunctuation = ConstantUtils.removePunctuation(text);
        Optional<Function> matchedFunctionOptional = allFunctions.stream()
                .filter(function -> {
                    String russianTextRegex = ConstantUtils.toRegex(function.getRussianText());
                    return textWithoutPunctuation.matches(russianTextRegex);
                })
                .findFirst();
        if(!matchedFunctionOptional.isPresent()){
            return TRANSLATION_ERROR;
        }
        Function function = matchedFunctionOptional.get();
        String russianText = function.getRussianText();
        String[] russianTextWords = russianText.split("([.,;/?! ])+");
        String[] textWords = text.split("([.,;/?! ])+");
        if(textWords.length != russianTextWords.length){
            return TRANSLATION_ERROR;
        }
        String result = function.getEnglishText();
        for (int i = 0; i < textWords.length; i++) {
            String russianWord = russianTextWords[i];
            String textWord = textWords[i];
            if(ConstantUtils.isStringMock(russianWord)){
                Position position = positionRepository.findByFunctionIdAndRussianWordPosition(function.getId(), russianWord);
                Optional<Dictionary> dictionaryOptional = dictionaryRepository.findByRussianWord(textWord);
                if(!dictionaryOptional.isPresent()){
                    return String.format(WORD_NOT_FOUND_REGEX, textWord);
                }
                result = result.replaceAll(position.getEnglishWordPosition(), dictionaryOptional.get().getEnglishWord());
            }
        }

        return result;
    }


}
