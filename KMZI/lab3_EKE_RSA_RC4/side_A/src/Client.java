import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.*;

public class Client {

    private static Socket clientSocket; //сокет для общения
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        try {
            try {
                // адрес - локальный хост, порт - 4004, такой же как у сервера
                clientSocket = new Socket("localhost", 4004); // этой строкой мы запрашиваем у сервера доступ на соединение
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // писать туда же
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                BigInteger P = BigInteger.valueOf(4567);
                RC4 rc4P = new RC4(P.toByteArray());

                // генерация открытого и закрытого ключей RSA
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(512);
                KeyPair kp = keyGen.genKeyPair();

                PublicKey publicKey = kp.getPublic();
                PrivateKey privateKey = kp.getPrivate();

                byte[] encodedPublicKey = publicKey.getEncoded();
                byte[] encryptOpenKey = rc4P.encrypt(encodedPublicKey); // шифрование открытого ключа RSA

                System.out.println("public key = " + new BigInteger(encodedPublicKey));
               // System.out.println("encrPublicKey = " + new BigInteger(encryptOpenKey));
                out.write(new BigInteger(encryptOpenKey)  + "\n"); // отправляем зашифрованный публичный ключ на сервер

                out.flush();

                String encr_Ks = in.readLine(); // ждём от сервера зашифрованный Ks
                System.out.println("encr_Ks = " + encr_Ks); // получив - выводим на экран

                // сначала, расшифровываем Ks ключом P
                byte[] decr_Ks1 = rc4P.decrypt((new BigInteger(encr_Ks)).toByteArray());

                // далее, закрытым ключом RSA расшифровавыем Ks еще раз и получаем сеансовый ключ
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                byte[] y = cipher.doFinal(decr_Ks1);
                BigInteger decr_Ks2 = new BigInteger(y);

                System.out.println("сеансовый ключ Ks =  " + decr_Ks2);

                // Генерация строки Ra
                int max = 234534546, min = 14573;
                int rand = (int) (Math.random() * (max - min + 1) + min);
                BigInteger Ra = new BigInteger((Integer.toString(rand)).substring(0, 5));
                System.out.println("случайная строка Ra: " + Ra);

                RC4 rc4_Ks = new RC4(decr_Ks2.toByteArray());
                byte[] encr_Ra = rc4_Ks.encrypt(Ra.toByteArray()); // сеансовым ключом шифруем Ra

                out.write( new BigInteger(encr_Ra) + "\n"); // отправляем зашфированное Ra на сервер
                out.flush(); // выталкиваем все из буфера

                String encr_RaRb_str = in.readLine(); // ждём, что скажет сервер
                BigInteger encr_RaRb = new BigInteger(encr_RaRb_str);
                //System.out.println("encr_RaRb = " + encr_RaRb);

                // расшифровываем сеаносвым ключом Ks строку с сервера
                byte[] decr_RaRb_arr = rc4_Ks.decrypt(encr_RaRb.toByteArray());
                BigInteger decr_RaRb = new BigInteger(decr_RaRb_arr);
                System.out.println("decr_RaRb = " + decr_RaRb);

                String Ra1S = "";
                for (int i = 0; i < (decr_RaRb.toString().length() / 2); i++) {
                    Ra1S += decr_RaRb.toString().charAt(i);
                }
                BigInteger Ra1 = new BigInteger(Ra1S);
                System.out.println("Ra1S = " + Ra1);

                if (Ra.compareTo(Ra1) == 0) {
                    System.out.println("Ra equals Ra1");
                    String Rb1S = "";
                    for (int i = decr_RaRb.toString().length() / 2; i < decr_RaRb.toString().length(); i++) {
                        Rb1S += decr_RaRb.toString().charAt(i);
                    }
                    BigInteger Rb1 = new BigInteger(Rb1S);
                    System.out.println("Rb1S = " + Rb1);

                    byte[] encr_Rb1 = rc4_Ks.encrypt(Rb1.toByteArray());
                    out.write(new BigInteger(encr_Rb1) + "\n"); // отправляем зашфированное Rb1 на сервер
                    out.flush(); // выталкиваем все из буфера
                }

            } finally {
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