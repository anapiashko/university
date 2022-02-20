import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Random;

public class B {

    private final static String IDb = "4545427636290023";
    private final static String IDa = "7245190517861531";
    private final static String Kb = "89023828447372Kb";

    private static String Ks;
    private static String T;

    static void parseReplyFromA(String replyFromA) {
        replyFromA = decrypt(Kb, replyFromA);
        Ks = replyFromA.substring(0, KDC.KEY_SIZE);
        String IDaFromKDC = replyFromA.substring(KDC.KEY_SIZE, 2 * KDC.KEY_SIZE);
        System.out.println("Compare IDa = " + IDa.equals(IDaFromKDC));
        T = replyFromA.substring(2 * KDC.KEY_SIZE, 2 * KDC.KEY_SIZE + KDC.TIME_SIZE);
        if (ChronoUnit.SECONDS.between(LocalDateTime.parse(T, KDC.dtf), LocalDateTime.now()) > 5) {
            System.out.println("Timestamp is NOT relevant!");
        }
    }

    static void parseReplyFromAModifiedN1(String N1, String modifiedN1) {
        modifiedN1 = decrypt(Ks, modifiedN1);
        String toCheckN1 = new BigInteger(modifiedN1).divide(new BigInteger("2")).toString();
        System.out.println("Correct N1 returned from A = " + N1.equals(toCheckN1));
    }

    private static String generateN1() {
        BigInteger n = new BigInteger("4243154165794052");
        Random rand = new Random();
        BigInteger result = new BigInteger(n.bitLength(), rand);
        while (result.compareTo(n) >= 0) {
            result = new BigInteger(n.bitLength(), rand);
        }
        System.out.println("Generating N1 = " + result.toString());
        return result.toString();
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
            IvParameterSpec iv = new IvParameterSpec(KDC.initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private static void four() {

    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("B is started!");
        boolean established = false;

        try (ServerSocket server = new ServerSocket(3346)) {

            Socket client = server.accept();

            System.out.print("Connection accepted.");

            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());

            while (!client.isClosed()) {

                if (!established) {

                    System.out.println("Server reading from channel A");

                    String replyFromA = in.readUTF();

                    System.out.println("READ from A message - " + replyFromA);

                    parseReplyFromA(replyFromA);

                    System.out.println("Server B writing to channel");

                    String N1 = generateN1();

                    out.writeUTF(encrypt(Ks, N1));
                    System.out.println("Server wrote - " + N1 + " - OK");

                    out.flush();

                    String replyFromAModifiedN1 = in.readUTF();

                    parseReplyFromAModifiedN1(N1, replyFromAModifiedN1);

                    established = true;

                } else {
                    System.out.println();
                    System.out.println("Connection established!");
                    System.out.println();

                    while (!client.isClosed()) {

                        System.out.println("B reading from channel");

                        String entry = in.readUTF();

                        System.out.println("READ from A message - " + entry);

                        if (entry.equalsIgnoreCase("quit")) {
                            System.out.println("A initialize connections suicide ...");
                            out.writeUTF("B reply - " + entry + " - OK");
                            out.flush();
                            Thread.sleep(3000);
                            break;
                        }

                        out.writeUTF("B reply - " + entry + " - OK");
                        System.out.println("B Wrote '" + entry + "'to A.");
                        out.flush();
                    }
                }
            }

            System.out.println("Client A disconnected");

            in.close();
            out.close();
            client.close();

            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
