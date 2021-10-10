package org.example.util;

public class ConstantUtils {

    public static final String TRANSLATION_ERROR = "Translation error";
    public static final String WORD_NOT_FOUND_REGEX = "Word %s not found in the dictionary";

    public static String toRegex(final String text){
        return text
                .replaceAll("x1", ".*")
                .replaceAll("x2", ".*")
                .replaceAll("x3", ".*")
                .replaceAll("y1", ".*")
                .replaceAll("y2", ".*")
                .replaceAll("y3", ".*");
    }

    public static String removePunctuation(final String text){
        return text
                .replaceAll("[.,/;!?]", "");
    }

    public static boolean isStringMock(final String word) {
        return word.equals("x1")
                || word.equals("x2")
                || word.equals("x3")
                || word.equals("y1")
                || word.equals("y2")
                || word.equals("y3");
    }
}
