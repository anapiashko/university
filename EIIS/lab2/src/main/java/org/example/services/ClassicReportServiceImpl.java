package org.example.services;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassicReportServiceImpl {

    public Set<String> getParagraphs(File file, String finalDataFromFile, Map<String, Integer> allWordsWithCount) throws IOException {

        List<String> paragraphs = readFileByParagraphs(file);

        Map<String, Integer> sentenceList = new HashMap<>();
        List<String> sentences = Arrays.stream(finalDataFromFile.split("[.]"))
                .filter(sentence -> !sentence.isEmpty())
                .map(sentence -> sentence = sentence.trim())
                .collect(Collectors.toList());

        for (String s : sentences) {
            int score = 0;
            List<String> wordsInSentence = Arrays.stream(s.split("[ ,.;:\"'»«\\-()—!\\[\\]\n]+"))
                    .filter(word -> !word.isEmpty() && ReportServiceImpl.isNeeded(word))
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

        Set<String> limitedSentenceSet = finalSen
                .stream()
                .limit(10)
                .collect(Collectors.toSet());

        return limitedSentenceSet;
    }

    private List<String> readFileByParagraphs(File file) throws IOException {
        List<String> paragraphs = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder dataInsideParagraph = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            dataInsideParagraph.append(line).append(" ");
            if (line.startsWith("    ")) {
                paragraphs.add(dataInsideParagraph.toString());
                dataInsideParagraph = new StringBuilder();
            }
        }
        return paragraphs;
    }
}
