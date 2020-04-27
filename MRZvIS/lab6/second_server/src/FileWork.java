import java.io.*;

public class FileWork {

    private String filename;

    FileWork(String filename) {
        this.filename = filename;
    }

    public String searchFile() {

        String data = "";
        try {
            File f = new File(filename);

            BufferedReader bfr = new BufferedReader(new FileReader(String.valueOf(f)));

            char[] charData = new char[(int)f.length()];

            bfr.read(charData);

            data = new String(charData);

        } catch (FileNotFoundException fe) {
            data = "FILE NOT FOUND";

        } catch (IOException e) {
            data = "IOException";
        }
        return data;
    }
}
