import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// Центральная Избирательная Комиссия - ЦИК (Central Election Commission - CEC)
public class CEC {

    public static List<Integer> voters;
    public static List<String> candidates;
    private static int keySize = 1024;
    private static AsymmetricCipherKeyPair CEC_keyPair;

    private static Socket clientSocket; //сокет для общения
    private static ServerSocket server; // серверсокет
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static void main(String[] args) {

        voters = new LinkedList<>(Arrays.asList( 1, 2, 3, 5, 10, 15 ));
        candidates = Arrays.asList("Сергей", "Николай", "Алексей");
        int[] voteNumbers = new int[candidates.size()];

        try {
            try {

                // ключи CEC
                CEC_keyPair = RSA.generateKeys(keySize);

                // регистируем публичный ключ CEC в KCA
                registerCECPublicKey();

                server = new ServerSocket(4004); // сервер сокет прослушивает порт 4004

                System.out.println("ЦИК запущен!"); // хорошо бы серверу объявить о своем запуске

                while (true) {

                    clientSocket = server.accept(); // accept() будет ждать пока кто-нибудь не захочет подключиться

                    try {
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                        out.write("Привет, это ЦИК! Список кандидатов : " + "\n");
                        for (int i = 0; i < candidates.size(); i++) {
                            out.write(candidates.get(i) + "\n");
                            out.flush(); // выталкиваем все из буфера
                        }

                        String voterId = in.readLine();
                        byte[] blinded_msg = readBytes(clientSocket);
                        byte[] sig = readBytes(clientSocket);

                        if (!voters.contains(Integer.parseInt(voterId)))
                            throw new Exception("Ошибка!Избиратель " + voterId + " уже проголосовал или не зарегистрирован!");

                        RSAKeyParameters voterPublicKey = getVoterPublicKey(new BigInteger(voterId));


                        MessageDigest H = MessageDigest.getInstance("SHA-1");
                        byte[] blinded_msg1 = H.digest(blinded_msg);
                       // byte[] unsig = RSA.unsign( voterPublicKey, sig);
                        byte[] unsig = unsign(blinded_msg);

                        if(!Arrays.equals(blinded_msg1, unsig)){
                            throw new IllegalAccessException();
                        }

                        byte[] signCEC = RSA.sign(CEC_keyPair.getPrivate(), blinded_msg);

                        sendBytes(clientSocket, signCEC);

                        byte[] cM = readBytes(clientSocket);
                        byte[] cDS = readBytes(clientSocket);
                        byte[] cB = readBytes(clientSocket);

                        //  Cipher cipher = Cipher.getInstance("RC4");
                        //  cipher.init(Cipher.DECRYPT_MODE, (Key) CEC_keyPair.getPrivate());
//                        byte[] M = cipher.doFinal(cM);
//                        byte[] DS = cipher.doFinal(cDS);
//                        byte[] B = cipher.doFinal(cB);

                        RSAKeyParameters parametersPublic = (RSAKeyParameters)CEC_keyPair.getPublic();
                        BigInteger exponent = parametersPublic.getExponent();
                        RC4 cipher = new RC4(exponent.toByteArray());

                        byte[] M = cipher.decrypt(cM);
                        byte[] DS = cipher.decrypt(cDS);
                        byte[] B = cipher.decrypt(cB);

                        //byte[] unsignDS = RSA.unsign(CEC_keyPair.getPublic(), DS);
                        byte[] unsignDS = M;
                        if(!M.equals(unsignDS)){
                            throw new IllegalAccessException();
                        }

                        System.out.println("M = " + new BigInteger(M) + " vote for " +
                                new String(B) + " - " + candidates.get(Integer.parseInt(new String(B)) - 1));


//                        Integer v = Integer.parseInt(voterId);
//                        System.out.println("v = " + v);
//                        Integer indexOfV = voters.indexOf(v);
//                        System.out.println("indexOfV = " + indexOfV);
                        voters.remove(voters.indexOf(Integer.parseInt(voterId)));
                        //System.out.print("voters : ");
                        for(int i=0;i<voters.size();i++){
                           // System.out.println(voters.get(i));
                        }

                        System.out.println("Results : ");
                        voteNumbers[Integer.parseInt(new String(B)) - 1]++;
                        int sum = 0;
                        for(int i=0;i<voteNumbers.length;i++){
                           // System.out.println(voteNumbers[i]);
                            sum += voteNumbers[i];
                        }
                        for(int i=0;i<voteNumbers.length;i++){
                            System.out.println(candidates.get(i) + " - " + (double)voteNumbers[i] * 100 / sum + " %");
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        //e.printStackTrace();
                    } finally { // в любом случае сокет будет закрыт
                        clientSocket.close();
                        // потоки тоже хорошо бы закрыть
                        in.close();
                        out.close();
                    }
                }
            } finally {
                System.out.println("ЦИК закрыт!");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static byte[] unsign(byte[] blinded_msg) throws NoSuchAlgorithmException {
        MessageDigest H = MessageDigest.getInstance("SHA-1");
        return H.digest(blinded_msg);
    }
    private static void registerCECPublicKey() throws IOException {
        Socket KCASocket = new Socket("localhost", 4006); // этой строкой мы запрашиваем; //сокет для общения
        BufferedReader inKCA = new BufferedReader(new InputStreamReader(KCASocket.getInputStream()));; // поток чтения из сокета
        BufferedWriter outKCA = new BufferedWriter(new OutputStreamWriter(KCASocket.getOutputStream())); // поток записи в сокет

        try {
            try {
                //System.out.println("registerCECPublicKey() ");

                outKCA.write("registerCECPublicKey" + "\n"); // отправляем запрос на сервер
//                outKCA.flush();

//                result = new AsymmetricCipherKeyPair(
//                        new RSAKeyParameters(false, n, e),
//                        new RSAPrivateCrtKeyParameters(n, e, d, p, q, dP, dQ, qInv));

                RSAPrivateCrtKeyParameters parametersPrivate = (RSAPrivateCrtKeyParameters)CEC_keyPair.getPrivate();
                RSAKeyParameters parametersPublic = (RSAKeyParameters)CEC_keyPair.getPublic();
                //n = P*Q - модуль
                //e - exponent
                //(e,n) - открытый ключ
                BigInteger modulus = parametersPublic.getModulus();
                BigInteger exponent = parametersPublic.getExponent();

                outKCA.write(modulus.toString() + "\n");
                outKCA.write(exponent.toString() + "\n");
                outKCA.flush();
//                byte[] arr = CEC_keyPair.getPublic();
//                sendBytes(KCASocket, arr);

            } finally { // в любом случае необходимо закрыть сокет и потоки
                //System.out.println("Клиент был закрыт...");
                KCASocket.close();
                inKCA.close();
                inKCA.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static RSAKeyParameters getVoterPublicKey(BigInteger voterId) throws IOException {
        Socket KCASocket = new Socket("localhost", 4006); // этой строкой мы запрашиваем; //сокет для общения
        BufferedReader inKCA = new BufferedReader(new InputStreamReader(KCASocket.getInputStream()));; // поток чтения из сокета
        BufferedWriter outKCA = new BufferedWriter(new OutputStreamWriter(KCASocket.getOutputStream())); // поток записи в сокет

        RSAKeyParameters publicKey = null;
        try {
            try {
                //System.out.println("getVoterPublicKey() ");

                outKCA.write("getVoterPublicKey" + "\n"); // отправляем запрос на сервер
                outKCA.write(voterId + "\n");
                outKCA.flush();

                String n = inKCA.readLine();
                String e = inKCA.readLine();
                publicKey = new RSAKeyParameters(false, new BigInteger(n), new BigInteger(e));

//                byte[] arr = readBytes(KCASocket);
//                publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(arr));

                //System.out.println("cecPublicKey = " + publicKey); // получив - выводим на экран
            } finally { // в любом случае необходимо закрыть сокет и потоки
               // System.out.println("Клиент был закрыт...");
                KCASocket.close();
                inKCA.close();
                inKCA.close();
            }
        } catch (IOException  e) {
            System.err.println(e);
        }
        return publicKey;
    }

    public static byte[] readBytes(Socket socket) throws IOException {
        // Again, probably better to store these objects references in the support class
        InputStream in = socket.getInputStream();
        DataInputStream dis = new DataInputStream(in);

        int len = dis.readInt();
        byte[] data = new byte[len];
        if (len > 0) {
            dis.readFully(data);
        }
        return data;
    }

    public static void sendBytes(Socket socket, byte[] myByteArray) throws IOException {
        sendBytes(socket, myByteArray, 0, myByteArray.length);
    }

    public static void sendBytes(Socket socket, byte[] myByteArray, int start, int len) throws IOException {
        if (len < 0)
            throw new IllegalArgumentException("Negative length not allowed");
        if (start < 0 || start >= myByteArray.length)
            throw new IndexOutOfBoundsException("Out of bounds: " + start);
        // Other checks if needed.

        // May be better to save the streams in the support class;
        // just like the socket variable.
        OutputStream out = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        dos.writeInt(len);
        if (len > 0) {
            dos.write(myByteArray, start, len);
        }
        dos.flush();
    }
}
