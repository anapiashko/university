import java.io.*;
import java.math.BigInteger;
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

                        String[] parameters = new String[7];
                        for (int i = 0; i < 7; i++) {
                            String response = in.readLine(); // ждём пока клиент что-нибудь нам напишет
                            parameters[i] = response;
                            out.flush(); // выталкиваем все из буфера
                        }

                        String message = parameters[0];
                        BigInteger[] rs = {new BigInteger(parameters[1]), new BigInteger(parameters[2])};
                        BigInteger P = new BigInteger(parameters[3]);
                        BigInteger q = new BigInteger(parameters[4]);
                        BigInteger Y = new BigInteger(parameters[5]);
                        BigInteger G = new BigInteger(parameters[6]);

                        System.out.println("Server: ");
                        System.out.println("\t" +message);
                        System.out.println("\tr = " + rs[0]);
                        System.out.println("\ts = " + rs[1]);
                        System.out.println("\tY = " + Y);
                        System.out.println("\tG = " + G);
                        System.out.println("\tP = " + P);
                        System.out.println("\tq = " + q);

                        // выталкиваем все из буфера
                        if (DSA.checkSignature(message,P,q,G,Y,rs)){
                            out.write("Привет, это Сервер! Подтверждаю, вы написали : " + message + "\n");
                            System.out.println("Подпись принята");
                        } else{
                            out.write("Подпись не корректна!\n");
                            System.out.println("Подпись отклонена");
                        }
                        out.flush(); // выталкиваем все из буфера

//                        String word = in.readLine(); // ждём пока клиент что-нибудь нам напишет
//                        System.out.println("Word from client : " + word);
//
//                        // не долго думая отвечает клиенту
//                        out.write("Привет, это Сервер! Подтверждаю, вы написали : " + word + "\r\n и ответ : "  + "\n");
//                        out.flush(); // выталкиваем все из буфера

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