package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.models.Report;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl {

    private final ClassicReportServiceImpl classicReportService;
    private final KeyWordReportServiceImpl keyWordReportService;

    public Report getReport(String reportName) throws IOException {

        File file = Files.walk(Paths.get("src/main/resources/"))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .filter(e -> e.getName().contains(reportName))
                .findFirst().get();

        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder wholeDataFromFile = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            wholeDataFromFile.append(line).append(" ");
        }

        String finalDataFromFile = wholeDataFromFile.toString()
                .trim()
                .replaceAll("\\s+", " ");

        System.out.println("wholeDataFromFile ---->" + finalDataFromFile + "----->");

        Set<String> keyWords = keyWordReportService.getKeyWords(finalDataFromFile);
        Map<String, Integer> map = keyWordReportService.getCountWordsInFile();
        Set<String> par = classicReportService.getParagraphs(file,finalDataFromFile,map);

        return Report.builder()
                .name(file.getName())
                .keyWords(keyWords)
                .classicReport(par)
                .build();
    }

    public static boolean isNeeded(String word){
        return !word.equalsIgnoreCase("I")
                && !word.equalsIgnoreCase("me")
                && !word.equalsIgnoreCase("you")
                && !word.equalsIgnoreCase("your")
                && !word.equalsIgnoreCase("he")
                && !word.equalsIgnoreCase("his")
                && !word.equalsIgnoreCase("she")
                && !word.equalsIgnoreCase("her")
                && !word.equalsIgnoreCase("it")
                && !word.equalsIgnoreCase("its")
                && !word.equalsIgnoreCase("they")
                && !word.equalsIgnoreCase("them")
                && !word.equalsIgnoreCase("those")
                && !word.equalsIgnoreCase("these")
                && !word.equalsIgnoreCase("that")
                && !word.equalsIgnoreCase("which")
                && !word.equalsIgnoreCase("this")
                && !word.equalsIgnoreCase("before")
                && !word.equalsIgnoreCase("after")
                && !word.equalsIgnoreCase("around")
                && !word.equalsIgnoreCase("above")
                && !word.equalsIgnoreCase("from")
                && !word.equalsIgnoreCase("why")
                && !word.equalsIgnoreCase("what")
                && !word.equalsIgnoreCase("how")
                && !word.equalsIgnoreCase("who")
                && !word.equalsIgnoreCase("whom")
                && !word.equalsIgnoreCase("whose")
                && !word.equalsIgnoreCase("wow")
                && !word.equalsIgnoreCase("no")
                && !word.equalsIgnoreCase("yes")
                && !word.equalsIgnoreCase("for")
                && !word.equalsIgnoreCase("but")
                && !word.equalsIgnoreCase("or")
                && !word.equalsIgnoreCase("and")
                && !word.equalsIgnoreCase("all")
                && !word.equalsIgnoreCase("on")
                && !word.equalsIgnoreCase("in")
                && !word.equalsIgnoreCase("near")
                && !word.equalsIgnoreCase("into")
                && !word.equalsIgnoreCase("onto")
                && !word.equalsIgnoreCase("the")
                && !word.equalsIgnoreCase("a")
                && !word.equalsIgnoreCase("at")
                && !word.equalsIgnoreCase("to")
                && !word.equalsIgnoreCase("by")
                && !word.equalsIgnoreCase("of")
                && !word.equalsIgnoreCase("are")
                && !word.equalsIgnoreCase("is")
                && !word.equalsIgnoreCase("as")
                && !word.equalsIgnoreCase("because")
                && !word.equalsIgnoreCase("be")
                && !word.equalsIgnoreCase("was")
                && !word.equalsIgnoreCase("were")
                && !word.equalsIgnoreCase("when")
                && !word.equalsIgnoreCase("where")
                && !word.equalsIgnoreCase("if")
                && !word.equalsIgnoreCase("one")
                && !word.equalsIgnoreCase("many")
                && !word.equalsIgnoreCase("almost")
                && !word.equalsIgnoreCase("only")
                && !word.equalsIgnoreCase("most")
                && !word.equalsIgnoreCase("now");
    }
}
