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
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Random;

public class KDC {

    public final static Integer KEY_SIZE = 16;
    public final static Integer TIME_SIZE = 19;
    public final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private final static String Ka = "70123022901292Ka";
    private final static String Kb = "89023828447372Kb";

    private static String IDa;
    private static String IDb;

    public static final String initVector = "encryptionIntVec";

    public static String generateKs() {
        System.out.println("Generating Ks");
        BigInteger n = new BigInteger("8842138746812764");
        Random rand = new Random();
        BigInteger result = new BigInteger(n.bitLength(), rand);
        while (result.compareTo(n) >= 0) {
            result = new BigInteger(n.bitLength(), rand);
        }
        return result.toString();
    }

    private static void getIDs(String IDaAndIDb) {
        IDa = IDaAndIDb.substring(0, KEY_SIZE);
        IDb = IDaAndIDb.substring(KEY_SIZE);
    }

    private static String two(String Ks) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("String before encryption = " + Ks + IDb + dtf.format(now) + Ks + IDa + dtf.format(now));
        return encrypt(Ka, Ks + IDb + dtf.format(now) + encrypt(Kb, Ks + IDa + dtf.format(now)));
    }

    public static String encrypt(String key, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
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


    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(3345)) {
            Socket client = server.accept();

            System.out.print("Connection accepted.");

            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            System.out.println("DataOutputStream  created");

            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println("DataInputStream created");

            while (!client.isClosed()) {

                System.out.println("Server reading from channel");

                String IDaAndIDb = in.readUTF();

                System.out.println("READ from client message - " + IDaAndIDb);

                getIDs(IDaAndIDb);

                String Ks = generateKs();

                System.out.println("Server try writing to channel");

                String two = two(Ks);
//
//                Thread.sleep(2000);

                out.writeUTF(two);

                System.out.println("KDC -> A - " + two + " - OK");
                System.out.println("Server Wrote message to client.");

                out.flush();

                break;
            }

            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");

            in.close();
            out.close();
            client.close();

            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
