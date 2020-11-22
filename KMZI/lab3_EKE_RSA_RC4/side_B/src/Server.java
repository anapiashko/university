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


                        BigInteger P = BigInteger.valueOf(4567);

                        String response = in.readLine(); // ждём пока клиент что-нибудь нам напишет

                        BigInteger encrOpenKey = new BigInteger(response);
                        RC4 rc4P = new RC4(P.toByteArray());

                        byte[] decrOpenKey = rc4P.decrypt(encrOpenKey.toByteArray());

                        System.out.println("encrypted open key = " + encrOpenKey);
                        System.out.println("decrypted open key = " + new BigInteger(decrOpenKey));

//                        out.write("Привет, это Сервер! Подтверждаю, расшифрованный открытый ключ : " + new BigInteger(decrOpenKey) + "\n");

                        BigInteger Ks = BigInteger.valueOf(5);

                        RC4 rc4OK = new RC4(decrOpenKey);

                        byte[] encr_Ks1 = rc4OK.encrypt(Ks.toByteArray());

                        byte[] encr_Ks2 = rc4P.encrypt(encr_Ks1);

                        out.write(new BigInteger(encr_Ks2) + "\n");
                        out.flush(); // выталкиваем все из буфера

                        // (4)
                        String encr_Ra = in.readLine(); // ждём пока клиент напишет encr_Ra
                        RC4 rc4_Ks = new RC4(Ks.toByteArray());
                        byte[] decr_Ra = rc4_Ks.decrypt(new BigInteger(encr_Ra).toByteArray());
                        System.out.println("encr_Ra = " + encr_Ra);
                        System.out.println("decr_Ra = " + new BigInteger(decr_Ra));

                        int max = 654352354, min = 15433;
                        int r = (int) (Math.random() * (max - min + 1) + min);
                        BigInteger Rb = new BigInteger((Integer.toString(r)).substring(0, 5));
                        System.out.println("случайная строка Rb : " + Rb);

                        String RaRb =  new BigInteger(decr_Ra).toString() + Rb.toString();

                        byte[] encr_RaRb = rc4_Ks.encrypt(new BigInteger(RaRb).toByteArray());

                        System.out.println("RaRb = " + RaRb);
                        System.out.println("encr_RaRb = " + new BigInteger(encr_RaRb));

                        out.write(new BigInteger(encr_RaRb) + "\n");

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