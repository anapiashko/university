import java.io.*;
import java.net.Socket;

public class ServerAsClient {

    private Socket clientSocket; //сокет для общения
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток записи в сокет

    public String connectToSecondServer(String filename) {

        String serverWord = "";

        try {
            try {
                // адрес - локальный хост, порт - 4004, такой же как у сервера
                clientSocket = new Socket("localhost", 4005); // этой строкой мы запрашиваем
                //  у сервера доступ на соединение

                // читать соообщения с сервера
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // писать туда же
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                System.out.println("Идет обращение к второму серверу...:");
                // если соединение произошло и потоки успешно созданы - мы можем
                //  работать дальше и предложить клиенту что то ввести
                // если нет - вылетит исключение

                out.write(filename+"\n"); // отправляем сообщение на сервер
                out.flush();
                // String serverWord = in.readLine(); // ждём, что скажет сервер
                String line;
                while ((line = in.readLine()) != null) {
                    serverWord += line + "\n";
                }
                System.out.println(serverWord); // получив - выводим на экран
            } finally { // в любом случае необходимо закрыть сокет и потоки
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return serverWord;
    }
}
