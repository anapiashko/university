package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.models.Report;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl {

    public static final String REGEX = "[ ,.;:\"'»\\-«()—!\\[\\]\n]+";

    private final ClassicReportServiceImpl classicReportService;
    private final KeyWordReportServiceImpl keyWordReportService;

    public Report getReport(String reportName) throws IOException {

        FileReaderServiceImpl fileReaderService = new FileReaderServiceImpl(reportName);

        String dataFromFile = fileReaderService.readDataFromFile();
        List<String> paragraphs = fileReaderService.readFileByParagraphs();

        Set<String> keyWords = keyWordReportService.getKeyWords(dataFromFile, paragraphs);
        Map<String, Integer> map = keyWordReportService.getCountWordsInFile();
        Set<String> par = classicReportService.getParagraphs(paragraphs, dataFromFile, map);

        return Report.builder()
                .name(reportName)
                .keyWords(keyWords)
                .classicReport(par)
                .build();
    }

    public static boolean isSensibleWord(String word) {
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
                && !word.equalsIgnoreCase("not")
                && !word.equalsIgnoreCase("yes")
                && !word.equalsIgnoreCase("for")
                && !word.equalsIgnoreCase("but")
                && !word.equalsIgnoreCase("or")
                && !word.equalsIgnoreCase("and")
                && !word.equalsIgnoreCase("all")
                && !word.equalsIgnoreCase("on")
                && !word.equalsIgnoreCase("in")
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
