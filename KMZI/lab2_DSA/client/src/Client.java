import sun.nio.cs.Surrogate;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;

public class Client {

    private static Socket clientSocket; //сокет для общения
    private static BufferedReader reader; // ридер читающий с консоли
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static void main(String[] args) {
        try {
            try {
                // адрес - локальный хост, порт - 4004, такой же как у сервера
                clientSocket = new Socket("localhost", 4004); // этой строкой мы запрашиваем у сервера доступ на соединение
                reader = new BufferedReader(new InputStreamReader(System.in)); // читать соообщения с сервера
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // писать туда же
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                System.out.println("Enter word : ");
                // если соединение произошло и потоки успешно созданы - мы можем
                //  работать дальше и предложить клиенту что то ввести
                // если нет - вылетит исключение

                String message = reader.readLine(); // ждём пока клиент что-нибудь не напишет в консоль
                //String message = "abc";

                BigInteger[] rs = DSA.subscribe(message);

                out.write(message + "\n"); // отправляем сообщение на сервер
                out.write(rs[0] + "\n"); // отправляем r на сервер
                out.write(rs[1] + "\n"); // отправляем s на сервер
                out.write(DSA.P + "\n"); // отправляем P на сервер
                out.write(DSA.q + "\n"); // отправляем q на сервер
                out.write(DSA.Y + "\n"); // отправляем Y на сервер
                out.write(DSA.G + "\n"); // отправляем G на сервер


//                out.write(message + "\n"); // отправляем сообщение на сервер
                out.flush();
                String serverWord = in.readLine(); // ждём, что скажет сервер
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

    }
}