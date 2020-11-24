import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Server {

    private static Socket clientSocket; // сокет для общения
    private static ServerSocket server; // сервер-сокет
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        try {
            try {
                server = new ServerSocket(4004); // серверсокет прослушивает порт 4004

                System.out.println("Сервер запущен!");

                while (true) {

                    clientSocket = server.accept(); // accept() будет ждать пока кто-нибудь не захочет подключиться

                    try {
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                        BigInteger P = BigInteger.valueOf(4567);

                        String encrPublicKey = in.readLine(); // ждём пока клиент напишет зашифрованный открытый ключ

                        BigInteger encrOpenKey = new BigInteger(encrPublicKey);
                        RC4 rc4P = new RC4(P.toByteArray());

                        byte[] decrOpenKey = rc4P.decrypt(encrOpenKey.toByteArray()); // расшифровываем открытый ключ

                        //System.out.println("encrypted open key = " + encrOpenKey);
                        System.out.println("decrypted open key = " + new BigInteger(decrOpenKey));

                        // Генерируем сеансовый ключ Ks
                        int max = 974293293, min = 11728;
                        int r = (int) (Math.random() * (max - min + 1) + min);
                        BigInteger Ks = BigInteger.valueOf(r);
                        System.out.println("Ks = " + Ks);

                        KeyFactory kf = KeyFactory.getInstance("RSA");
                        X509EncodedKeySpec spec = new X509EncodedKeySpec(decrOpenKey);
                        PublicKey publicKey = kf.generatePublic(spec);

                        Cipher cipher = Cipher.getInstance("RSA");
                        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                        byte[] x = cipher.doFinal(Ks.toByteArray()); // шифрование открытым ключот от клиента

                        byte[] encr_Ks2 = rc4P.encrypt(x); // шифрование на ключе P

                        System.out.println("encrypted Ks = " + new BigInteger(encr_Ks2));
                        out.write(new BigInteger(encr_Ks2) + "\n"); // отправляем клиенту зашифрованный Ks
                        out.flush(); // выталкиваем все из буфера


                        String encr_Ra = in.readLine(); // ждём пока клиент напишет зашифрованную строку Ra

                        //  расшифрование Ra с помошью сеансового ключа Ks
                        RC4 rc4_Ks = new RC4(Ks.toByteArray());
                        byte[] decr_Ra = rc4_Ks.decrypt(new BigInteger(encr_Ra).toByteArray());
                       // System.out.println("encr_Ra = " + encr_Ra);
                        System.out.println("decr_Ra = " + new BigInteger(decr_Ra));

                        // Генерация строки Rb
                        r = (int) (Math.random() * (max - min + 1) + min);
                        BigInteger Rb = new BigInteger((Integer.toString(r)).substring(0, 5));
                        System.out.println("случайная строка Rb : " + Rb);

                        String RaRb =  new BigInteger(decr_Ra).toString() + Rb.toString(); // объединение Ra и Rb

                        byte[] encr_RaRb = rc4_Ks.encrypt(new BigInteger(RaRb).toByteArray()); // шифрование (Ra || Rb)

//                        System.out.println("RaRb = " + RaRb);
//                        System.out.println("encr_RaRb = " + new BigInteger(encr_RaRb));

                        out.write(new BigInteger(encr_RaRb) + "\n"); // Отправляем клиенту зашифрованное (Ra || Rb)

                        out.flush(); // выталкиваем все из буфера

                        String encr_Rb1 = in.readLine(); // ждём пока клиент напишет зашифрованную строку Rb1

                        byte[] decr_Rb1 = rc4_Ks.decrypt((new BigInteger(encr_Rb1)).toByteArray());
                        BigInteger Rb1 = new BigInteger(decr_Rb1);
                        System.out.println("Rb1 = " + Rb1);

                        if(Rb.compareTo(Rb1) == 0){
                            System.out.println("Rb equals Rb1");
                            System.out.println("Ks = " + Ks + " - accepted");
                        }
                    } finally {
                        clientSocket.close();
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