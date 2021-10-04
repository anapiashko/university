package org.example.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassicReportServiceImpl {

    public Set<String> getParagraphs(List<String> paragraphs, String finalDataFromFile, Map<String, Integer> allWordsWithCount) throws IOException {

        Map<String, Integer> sentenceList = new HashMap<>();
        List<String> sentences = Arrays.stream(finalDataFromFile.split("[.!?]"))
                .filter(sentence -> !sentence.isEmpty())
                .map(sentence -> sentence = sentence.trim())
                .collect(Collectors.toList());

        for (String s : sentences) {
            int score = 0;
            List<String> wordsInSentence = Arrays.stream(s.split(ReportServiceImpl.REGEX))
                    .filter(word -> !word.isEmpty() && ReportServiceImpl.isSensibleWord(word))
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            for (String wordInSentence : wordsInSentence) {
                score += allWordsWithCount.get(wordInSentence);
            }
            sentenceList.put(s, score);
        }

        Set<String> finalSen = new LinkedHashSet<>();
        for (String par : paragraphs) {
            int maxSenScore = 0;
            String sen= null;
            for (Map.Entry<String, Integer> entry : sentenceList.entrySet()) {
                if (par.contains(entry.getKey()) &&
                        entry.getValue() > maxSenScore) {
                    maxSenScore = entry.getValue();
                    sen = entry.getKey();
                }
            }
            finalSen.add(sen);
        }

        return finalSen;
    }
}
