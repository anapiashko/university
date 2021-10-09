package org.example.services;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeyWordReportServiceImpl {

    Map<String, Integer> countWordsInFile = new HashMap<>();

    public Set<String> getKeyWords(String finalDataFromFile, List<String> paragraphs) {

        List<String> fileWithWords = Arrays.asList(finalDataFromFile
                .split(ReportServiceImpl.REGEX));

        fileWithWords = fileWithWords.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        int count;
        for (String word : fileWithWords) {
            if (countWordsInFile.containsKey(word)) {
                count = countWordsInFile.get(word) + 1;
            } else {
                count = 1;
            }
            countWordsInFile.put(word, count);
        }

        Set<String> sortedKeySet = countWordsInFile.keySet()
                .stream()
                .filter(word -> !word.isEmpty() && ReportServiceImpl.isSensibleWord(word))
                .sorted((e1, e2) -> countWordsInFile.get(e2).compareTo(countWordsInFile.get(e1)))
                .limit(paragraphs.size())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return sortedKeySet;
    }

    public Map<String, Integer> getCountWordsInFile() {
        return this.countWordsInFile;
    }
}
