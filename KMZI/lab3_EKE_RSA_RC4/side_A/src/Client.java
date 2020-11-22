import java.io.*;
import java.math.BigInteger;
import java.net.Socket;

public class Client {

    private static Socket clientSocket; //сокет для общения
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static void main(String[] args) {
        try {
            try {
                // адрес - локальный хост, порт - 4004, такой же как у сервера
                clientSocket = new Socket("localhost", 4004); // этой строкой мы запрашиваем у сервера доступ на соединение
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // писать туда же
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                BigInteger P = BigInteger.valueOf(4567);
                RC4 rc4P = new RC4(P.toByteArray());

                BigInteger openKey = BigInteger.valueOf(97);

                byte[] encryptOpenKey = rc4P.encrypt(openKey.toByteArray());

                out.write(new BigInteger(encryptOpenKey)  + "\n"); // отправляем сообщение на сервер

                out.flush();

//                String serverWord = in.readLine(); // ждём, что скажет сервер
//                System.out.println(serverWord); // получив - выводим на экран

                String encr_Ks = in.readLine(); // ждём, что скажет сервер
                System.out.println("encr_Ks = " + encr_Ks); // получив - выводим на экран

                // (3)
                RC4 rc4OK = new RC4(openKey.toByteArray());
                byte[] decr_Ks1 = rc4P.decrypt(new BigInteger(encr_Ks).toByteArray());
                byte[] decr_Ks2 = rc4OK.decrypt(decr_Ks1);
                System.out.println("decr_Ks = " + new BigInteger(decr_Ks2));

                int max = 234534546, min = 14573;
                int rand = (int) (Math.random() * (max - min + 1) + min);
                BigInteger Ra = new BigInteger((Integer.toString(rand)).substring(0, 5));
                System.out.println("случайная строка Ra: " + Ra);

                RC4 rc4_Ks = new RC4(decr_Ks2);
                byte[] encr_Ra = rc4_Ks.encrypt(Ra.toByteArray());

                out.write( new BigInteger(encr_Ra) + "\n"); // отправляем Ra на сервер
                out.flush(); // выталкиваем все из буфера

                String encr_RaRb_str = in.readLine(); // ждём, что скажет сервер
                BigInteger encr_RaRb = new BigInteger(encr_RaRb_str);
                System.out.println("encr_RaRb = " + encr_RaRb); // получив - выводим на экран

                byte[] decr_RaRb_arr = rc4_Ks.decrypt(encr_RaRb.toByteArray());
                BigInteger decr_RaRb = new BigInteger(decr_RaRb_arr);
                System.out.println("decr_RaRb = " + decr_RaRb); // получив - выводим на экран

                String Ra1S = "";
                for (int i = 0; i < (decr_RaRb.toString().length() / 2); i++) {
                    Ra1S += decr_RaRb.toString().charAt(i);
                }
                System.out.println("Ra1S = " + Ra1S);

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