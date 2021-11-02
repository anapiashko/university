package org.example.services;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LanguageService {

    private Map<String, Integer> rusTrainingSet;
    private Map<String, Integer> engTrainingSet;

    @PostConstruct
    private void fillResources() throws Exception {
        Map<String, Integer> rusMap = countWordRepetitions("src/main/resources/source/rus_source.txt");
        Map<String, Integer> engMap = countWordRepetitions("src/main/resources/source/eng_source.txt");

        List<String> rusSortedWords = sortWords(rusMap);
        List<String> engSortedWords = sortWords(engMap);
        int minSize = Math.min(rusSortedWords.size(), engSortedWords.size());

        rusTrainingSet = new HashMap<>();
        for (int i = 0; i < minSize; i++) {
            rusTrainingSet.put(rusSortedWords.get(i), i);
        }

        engTrainingSet = new HashMap<>();
        for (int i = 0; i < minSize; i++) {
            engTrainingSet.put(engSortedWords.get(i), i);
        }
    }

    public String recognizeLanguage(final String fileName) throws Exception {
        Map<String, Integer> requestMap = countWordRepetitions("src/main/resources/recognize/" + fileName + ".txt");
        List<String> requestSortedWords = sortWords(requestMap);

        int rusCount = 0;
        int engCount = 0;
        for (int i = 0; i < requestSortedWords.size(); i++) {
            String requestWord = requestSortedWords.get(i);

            if (!rusTrainingSet.containsKey(requestWord)) {
                rusCount += rusTrainingSet.size();
            } else {
                rusCount += Math.abs(rusTrainingSet.get(requestWord) - i);
            }

            if (!engTrainingSet.containsKey(requestWord)) {
                engCount += engTrainingSet.size();
            } else {
                engCount += Math.abs(engTrainingSet.get(requestWord) - i);
            }
        }
        return rusCount > engCount ? "English" : "Русский";
    }

    private Map<String, Integer> countWordRepetitions(final String fileName) throws Exception {

        String fileString = readFromFile(fileName);

        Map<String, Integer> wordsWithNumberOfRepetitions = new HashMap<>();
        List<String> words = Arrays.stream(fileString.split("[ ,.;:0-9\"'»«()—!\\[\\]\n]+"))
                .filter(word -> !word.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        for (String word : words) {
            wordsWithNumberOfRepetitions.computeIfPresent(word, (key, value) -> value + 1);
            wordsWithNumberOfRepetitions.putIfAbsent(word, 1);
        }
        return wordsWithNumberOfRepetitions;
    }

    private List<String> sortWords(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private String readFromFile(final String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuilder resultStringBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            resultStringBuilder.append(line).append(" ");
        }
        return resultStringBuilder.toString();
    }

}