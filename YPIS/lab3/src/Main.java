import java.io.*;

public class Main {
    public static void main(String[] args) {

        String text = read();
        System.out.println("SOURCE TEXT : " + text);

        String[] words = text.split("[\\s.,(); ]+");
        String[] functions = text.split(";");
        for (String f : functions) {
            if (!correctFunction(f)) {
                System.out.println("incorrect function : " + f);
                return;
            }
        }

        boolean closeQuote = false;
        int wordCount = 0;

        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);

            // если слово начинается с буквы, то это имя процедуры, т.к. другие параметры заключены в ковычки
            if (String.valueOf(symbol).matches("[a-zA-Z0-9_]+")) {
                if (correctWord(words[wordCount])) {
                    System.out.format("%-20s%s\n", words[wordCount], "Procedure name");
                    i += words[wordCount].length() - 1;
                    wordCount++;
                } else {
                    System.out.println("word \"" + words[wordCount] + "\" is not correct");
                    return;
                }
            } else if (symbol == '(') System.out.format("%-20s%s\n", symbol, "Open parenthesis");
            else if (symbol == ')') System.out.format("%-20s%s\n", symbol, "Close parenthesis");
            else if (symbol == ';') System.out.format("%-20s%s\n", symbol, "Semicolon");
            else if (symbol == ',') System.out.format("%-20s%s\n", symbol, "Comma");
            else if (symbol == '\'') {
                System.out.format("%-20s%s\n", symbol, "Single quotes");
                if (!closeQuote) {
                    System.out.format("%-20s%s\n", text.charAt(++i), "Single character");
                    closeQuote = true;
                    wordCount++;
                } else closeQuote = false;
            } else if (symbol == '\"') {
                System.out.format("%-20s%s\n", symbol, "Double quotes");
                if (!closeQuote) {
                    if (correctWord(words[wordCount].substring(1, words[wordCount].length() - 1))) {
                        System.out.format("%-20s%s\n", words[wordCount].substring(1, words[wordCount].length() - 1), "String constant");
                        closeQuote = true;
                        i += words[wordCount].length() - 2;
                        wordCount++;
                    } else {
                        System.out.println("word \"" + words[wordCount].substring(1, words[wordCount].length() - 1) + "\" is not correct");
                        return;
                    }
                } else {
                    closeQuote = false;
                }
            }
        }
    }

    private static boolean correctWord(String word) {
        String regex = "[a-zA-Z_][a-zA-Z0-9]+";
        return word.matches(regex);
    }

    private static boolean correctFunction(String function) {
        String regex = " *([a-zA-Z_][a-zA-Z0-9]+) *\\([a-zA-Z0-9 ,\"']*\\) *";
        return function.matches(regex);
    }

    // построчное считывание файла
    public static String read() {
        String input = "";
        try {
            File file = new File("input.txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            input = reader.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }
}

