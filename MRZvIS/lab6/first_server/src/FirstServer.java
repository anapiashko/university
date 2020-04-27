import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FirstServer {

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

                        String filename = in.readLine(); // ждём пока клиент что-нибудь нам напишет
                        System.out.println(filename);
                        // не долго думая отвечает клиенту

                        FileWork fileWork = new FileWork(filename); //ищем файл и при успехе возвращаем содержимое
                        String content = fileWork.searchFile();

                        //тут еще надо если что сходить к другому серверу
                        if(content.equals("FILE NOT FOUND")){
                            ServerAsClient serverAsClient = new ServerAsClient();
                            content = serverAsClient.connectToSecondServer(filename);
                        }
                        //

                        out.write("Привет, это Сервер! Подтверждаю, вы написали : " + filename + "\n");
                        out.write("Содержимое вашего файла: " + content);
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