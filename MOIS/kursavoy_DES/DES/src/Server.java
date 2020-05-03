import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static Socket clientSocket; //сокет для общения
    private static ServerSocket server; // серверсокет
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static void main(String[] args) {
        try {
            try {
                server = new ServerSocket(4004); // серверсокет прослушивает порт 4004

                System.out.println("Сервер запущен!"); // хорошо бы серверу объявить о своем запуске

                while (true) {

                    clientSocket = server.accept(); // accept() будет ждать пока кто-нибудь не захочет подключиться

                    try { // установив связь и воссоздав сокет для общения с клиентом можно перейти
                        // к созданию потоков ввода/вывода.
                        // теперь мы можем принимать сообщения
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        // и отправлять
                        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                        // ждём пока клиент что-нибудь нам напишет
                        String[] clientWord = new String[3];

                        String line = in.readLine();
                        int i = 0;
                        while (line != null) {
                            clientWord[i] = line;
                            i++;
                            if (i == 3) {
                                break;
                            }
                            line = in.readLine();
                        }

                        Integer mark = Integer.parseInt(clientWord[0]);
                        String key = clientWord[1];
                        String message = clientWord[2];

                        DES des = new DES();
                        String result = des.encryption(mark, key, message);

//                        StringBuilder clientWord = new StringBuilder();
//
//                        String line = in.readLine();
//                        while (line != null) {
//                            clientWord.append(line).append("\r\n");
//                            line = in.readLine();
//                        }

                        System.out.println(clientWord[0] + "\t" + clientWord[1] + "\t" + clientWord[2]);
                        // не долго думая отвечает клиенту
                        out.write("Привет, это Сервер! Подтверждаю, вы написали : "+"\r\n" + clientWord[0] +"\r\n"+ clientWord[1] + "\r\n" + clientWord[2] + "\r\n"
                                +" and response from server : "+ result + "\n");
                        out.flush(); // выталкиваем все из буфера

                    } finally { // в любом случае сокет будет закрыт
                        clientSocket.close();
                        // потоки тоже хорошо бы закрыть
                        in.close();
                        out.close();
                    }
                }
            } finally {
                System.out.println("Сервер закрыт!");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}