package org.example.services;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeyWordReportServiceImpl {

    Map<String, Integer> countWordsInFile = new HashMap<>();

    public Set<String> getKeyWords(String finalDataFromFile) {

        List<String> fileWithWords = Arrays.asList(finalDataFromFile
                .split("[ ,.;:\"'»\\-«()—!\\[\\]\n]+"));

        fileWithWords = fileWithWords.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        for (String word : fileWithWords) {
            int count = 1;
            if (countWordsInFile.containsKey(word)) {
                count++;
            }
            countWordsInFile.put(word, count);
        }

        Set<String> sortedKeySet = countWordsInFile.keySet()
                .stream()
                .filter(word -> !word.isEmpty() && ReportServiceImpl.isNeeded(word))
                .sorted((e1, e2) -> countWordsInFile.get(e2).compareTo(countWordsInFile.get(e1)))
                .limit(10)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return sortedKeySet;
    }

    public Map<String, Integer> getCountWordsInFile () {
        return this.countWordsInFile;
    }
}
