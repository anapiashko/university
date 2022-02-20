import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class A {

    private final static String IDa = "7245190517861531";
    private final static String IDb = "4545427636290023";
    private final static String Ka = "70123022901292Ka";

    private static String T;
    private static String Ks;

    static String one() {
        return IDa + IDb;
    }

    static String parseReplyFromKDC(String replyFromKDC) {
        replyFromKDC = decrypt(Ka, replyFromKDC);

        System.out.println("Decrypt only part of message from KDC " + replyFromKDC);

        Ks = replyFromKDC.substring(0, KDC.KEY_SIZE);
        String IDbFromKDC = replyFromKDC.substring(KDC.KEY_SIZE, 2 * KDC.KEY_SIZE);
        System.out.println("Compare IDb = " + IDb.equals(IDbFromKDC));
        T = replyFromKDC.substring(2 * KDC.KEY_SIZE, 2 * KDC.KEY_SIZE + KDC.TIME_SIZE);

        if (ChronoUnit.SECONDS.between(LocalDateTime.parse(T, KDC.dtf), LocalDateTime.now()) > 5) {
            System.out.println("Timestamp is NOT relevant!");
        }
        return replyFromKDC.substring(2 * KDC.KEY_SIZE + KDC.TIME_SIZE);
    }

//    static String three () {
//
//    }

    static String decryptAndModifyN1(String N1) {
        N1 = decrypt(Ks, N1);
        System.out.println("N1 after decrypt = " + N1);
        return String.valueOf(new BigInteger(N1).multiply(new BigInteger("2")));
    }

    public static String encrypt(String key, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(KDC.initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String decrypt(String key, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(KDC.initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) throws InterruptedException {

        try (Socket socket = new Socket("localhost", 3345);
             DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
             DataInputStream ois = new DataInputStream(socket.getInputStream())) {

            System.out.println("Client connected to socket.");

            String theRestCode = "";
            while (!socket.isOutputShutdown()) {

                System.out.println("Client start writing in channel...");
                Thread.sleep(1000);
                String clientCommand = one();

                oos.writeUTF(clientCommand);
                oos.flush();
                System.out.println("Client sent message " + clientCommand + " to server.");
                Thread.sleep(1000);

                System.out.println("Client sent message & start waiting for data from server...");
                Thread.sleep(2000);

                System.out.println("reading...");
                String replyFromKDC = ois.readUTF();
                System.out.println("replyFromKDC = " + replyFromKDC);
                theRestCode = parseReplyFromKDC(replyFromKDC);

                break;
            }

            sendToB(theRestCode);

            System.out.println("Closing connections & channels on clientSide - DONE.");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void sendToB(String theRestCode) {

        boolean established = false;

        try (Socket socket = new Socket("localhost", 3346);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
             DataInputStream ois = new DataInputStream(socket.getInputStream())) {

            System.out.println("A connected to socket B.");

            while (!socket.isOutputShutdown()) {

                if (!established) {

                    System.out.println("A start writing in channel...");

                    oos.writeUTF(theRestCode);
                    oos.flush();
                    System.out.println("A sent message " + theRestCode + " to server.");

                    System.out.println("A sent message & start waiting for data from server...");
                    Thread.sleep(2000);

                    System.out.println("reading B...");
                    String replyFromBN1 = ois.readUTF();
                    System.out.println("Decrypted N1 = " + replyFromBN1);

                    String modifiedN1 = decryptAndModifyN1(replyFromBN1);
                    oos.writeUTF(encrypt(Ks, modifiedN1));
                    oos.flush();

                    established = true;
                } else {
                    System.out.println();
                    System.out.println("Connection established!");
                    System.out.println();

                    while (!socket.isOutputShutdown()) {

                        if (br.ready()) {

                            Thread.sleep(1000);
                            String clientCommand = br.readLine();

                            oos.writeUTF(clientCommand);
                            oos.flush();
                            System.out.println("A sent message " + clientCommand + " to B.");
                            Thread.sleep(1000);

                            if (clientCommand.equalsIgnoreCase("quit")) {

                                System.out.println("A kill connections");
                                Thread.sleep(2000);

                                System.out.println("reading...");
                                String in = ois.readUTF();
                                System.out.println(in);
                                break;
                            }

                            System.out.println("Client sent message & start waiting for data from B...");
                            Thread.sleep(2000);

                            String in = ois.readUTF();
                            System.out.println(in);
                        }
                    }
                }
            }

            System.out.println("Closing connections & channels on clientSide - DONE.");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
