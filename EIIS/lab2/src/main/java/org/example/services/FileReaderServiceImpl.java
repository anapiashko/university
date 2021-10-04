package org.example.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileReaderServiceImpl {

    private File file;

    FileReaderServiceImpl(String reportName) throws IOException {
        this.file = Files.walk(Paths.get("src/main/resources/"))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .filter(e -> e.getName().contains(reportName))
                .findFirst().get();
    }

    public String readDataFromFile() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder wholeDataFromFile = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            wholeDataFromFile.append(line).append(" ");
        }

        String finalDataFromFile = wholeDataFromFile.toString()
                .trim()
                .replaceAll("\\s+", " ");
        return finalDataFromFile;
    }

    public List<String> readFileByParagraphs() throws IOException {
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
