package org.example.models;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SearchService {

    private final Map<File, Map<String, Double>> weightWordsInFile = new HashMap<>();

    @PostConstruct
    public void calculateParameters() throws Exception {

        List<File> files = Files.walk(Paths.get("src/main/resources/"))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        Map<File, List<String>> filesWithWords = new HashMap<>();
        List<String> allWords = files.stream().flatMap(file -> {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder resultStringBuilder = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append(" ");
                }

                List<String> words = Arrays.asList(resultStringBuilder.toString()
                        .trim()
                        .replaceAll("\\s+", " ")
                        .split("[ ,.;:\"'»«()—!\\[\\]\n]+"));
                filesWithWords.put(file, words);
                return words.stream();
            } catch (Exception var6) {
                var6.printStackTrace();
                return Stream.empty();
            }
        }).collect(Collectors.toList());

        Map<String, Integer> allWordsWithCount = new HashMap<>();
        for (String newWord : allWords) {
            if (!allWordsWithCount.containsKey(newWord)) {
                int count = 0;
                for (String word : allWords) {
                    if (word.equals(newWord))
                        count++;
                }
                allWordsWithCount.put(newWord, count);
            }
        }

        Map<File, Map<String, Integer>> countWordsInFile = new HashMap<>();

        for (Entry<File, List<String>> entry : filesWithWords.entrySet()) {
            List<String> listWordsInFile = entry.getValue();
            Map<String, Integer> wordsInFile = new HashMap<>();

            for (String word : listWordsInFile) {
                int count = 1;
                if (wordsInFile.containsKey(word)) {
                    count++;
                }
                wordsInFile.put(word, count);
            }
            countWordsInFile.put(entry.getKey(), wordsInFile);
        }

        Map<String, Integer> wordWithDocs = new HashMap<>();
        for (String str : allWords) {
            int count = 0;
            wordWithDocs.put(str, count);
            for (Entry<File, Map<String, Integer>> entry : countWordsInFile.entrySet()) {
                if (entry.getValue().containsKey(str)) {
                    ++count;
                    wordWithDocs.put(str, count);
                }
            }
        }

        int N = files.size();
        Map<String, Double> Bi = new HashMap<>();

        for (String word : wordWithDocs.keySet()) {
            Double b = Math.log((double) N / (double) wordWithDocs.get(word));
            Bi.put(word, b);
        }

        for (Entry<File, Map<String, Integer>> entry : countWordsInFile.entrySet()) {

            Map<String, Integer> wordWithCountInFile = entry.getValue();
            Map<String, Double> weightOfWord = new HashMap<>();

            for (Entry<String, Integer> wordCount : wordWithCountInFile.entrySet()) {
                String word = wordCount.getKey();
                Integer count = wordCount.getValue();
                Double weight = (double) count * Bi.get(word);
                weightOfWord.put(word, weight);
            }

            this.weightWordsInFile.put(entry.getKey(), weightOfWord);
        }
    }

    public List<String> findFilesByInput(String word) {
        List<Output> outputs = new ArrayList<>();

        for(Entry<File, Map<String, Double>> entry : weightWordsInFile.entrySet()) {
            Map<String, Double> wordWithWeight = entry.getValue();
            Double weight = wordWithWeight.get(word) == null ? 0.0D : wordWithWeight.get(word);
            if (weight.compareTo(0.0D) > 0) {
                outputs.add(new Output(entry.getKey().getPath(), weight));
            }
        }

        List<String> filesWithWord = outputs.stream()
                .sorted(Comparator.comparing(Output::getWeight).reversed())
                .map(Output::getFileName)
                .collect(Collectors.toList());

        return filesWithWord;
    }
}