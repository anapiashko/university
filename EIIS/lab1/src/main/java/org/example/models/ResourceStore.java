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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ResourceStore {

    private List<Resource> resources;

    @PostConstruct
    public void fillResources() throws Exception {
        List<File> files = Files.walk(Paths.get("src/main/resources/"))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
        Map<File, List<String>> filesWithWords = new HashMap<>();
        List<String> allWords = files.stream()
                .flatMap(file -> {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        StringBuilder resultStringBuilder = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            resultStringBuilder.append(line).append(" ");
                        }
                        List<String> words = Arrays.asList(resultStringBuilder.toString().split(" \n,\\.;"));
                        filesWithWords.put(file, words);
                        return words.stream();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Stream.empty();
                })
                .collect(Collectors.toList());
        Map<String, Integer> allWordsWithCount = new HashMap<>();
        for (String newWord : allWords) {
            if (!allWordsWithCount.containsKey(newWord)) {
                int count = 1;
                for (String word : allWords) {
                    if (word.equals(newWord))
                        count++;
                }
                allWordsWithCount.put(newWord, count);
            }
        }
        for (File file : filesWithWords.keySet()) {

        }


    }
}
