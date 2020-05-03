import java.io.*;
import java.net.Socket;

public class Client {

    private static Socket clientSocket; //сокет для общения
    private static BufferedReader reader; // нам нужен ридер читающий с консоли, иначе как
    // мы узнаем что хочет сказать клиент?
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static void main(String[] args) throws IOException {
        String exit;
        //  у сервера доступ на соединение
        reader = new BufferedReader(new InputStreamReader(System.in));
        do {
            try {
                try {
                    // адрес - локальный хост, порт - 4004, такой же как у сервера
                    clientSocket = new Socket("localhost", 4004); // этой строкой мы запрашиваем
                    // читать соообщения с сервера
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    // писать туда же
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    // если соединение произошло и потоки успешно созданы - мы можем
                    //  работать дальше и предложить клиенту что то ввести
                    // если нет - вылетит исключение

                    // ждём пока клиент что-нибудь не напишет в консоль
                    System.out.print("Encryption or Decryption (1/0) ? ");
                    String stingMark = reader.readLine();
                    int mark = Integer.parseInt(stingMark);

                    System.out.print("key : ");
                    String key = reader.readLine();

                    System.out.print("Enter message : ");
                    String message = reader.readLine();

                    // отправляем сообщение на сервер
                    out.write(mark + "\n");
                    out.write(key + "\n");
                    out.write(message + "\n");
                    out.flush();

                    // String serverWord = in.readLine(); // ждём, что скажет сервер
                    String serverWord = "";
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
            System.out.print("To quit write exit : ");
        } while (!(exit = reader.readLine()).equals("exit"));
    }
}
