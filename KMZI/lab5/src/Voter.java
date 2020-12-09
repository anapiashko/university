import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyParameters;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Voter {

    private static Socket clientSocket; //сокет для общения
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));; // ридер читающий с консоли
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    private static RSAKeyParameters cecPublicKey;

    public static void main(String[] args) throws IOException {

        // ID избирателя
        System.out.print("Введите ваш ID :");
        int voterId = Integer.parseInt(reader.readLine());
        //int voterId = 10;

        BigInteger M = BigInteger.probablePrime(64, new Random());
        System.out.println("Ваша метка : " + M);

        // ключи избирателя
        AsymmetricCipherKeyPair voter_keyPair = RSA.generateKeys(1024);

        registerVoter(BigInteger.valueOf(voterId), voter_keyPair.getPublic());

        try {
            try {
                // адрес - локальный хост, порт - 4004, такой же как у сервера
                clientSocket = new Socket("localhost", 4004); // этой строкой мы запрашиваем у сервера доступ на соединение
//                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                List<String> candidates = getCandidates();

                //публичный ключ ЦИК
                cecPublicKey = getCECPublicKey();


                //----------- Voter: Generating blinding factor based on CEC's public key -----------//
                BigInteger blindingFactor = RSA.generateBlindingFactor( cecPublicKey);

                //----------------- Voter: Blinding message with CEC's public key -----------------//
                byte[] blinded_msg =
                        RSA.blind( cecPublicKey, blindingFactor, M.toByteArray());

                //------------- Voter: Signing blinded message with Voter's private key -------------//
                byte[] sig = RSA.sign( voter_keyPair.getPrivate(), blinded_msg);

                out.write(voterId + "\n"); // отправляем выбор на сервер
                out.flush();
                sendBytes(clientSocket, blinded_msg);
               // out.flush();
                sendBytes(clientSocket, sig);

//                out.write(new String(blinded_msg) + "\n"); // отправляем выбор на сервер
 //               out.write(new String(sig) + "\n"); // отправляем выбор на сервер

                byte[] signCEC = readBytes(clientSocket);

                byte[] DS = RSA.unblind(cecPublicKey, blindingFactor, signCEC);

                System.out.println("Выберете кандидата : ");
                String choice = reader.readLine(); // ждём пока клиент напишет в консоль кандидата
                System.out.println("Ваш выбор : " + choice);


//                 Cipher cipher = Cipher.getInstance("RC4");
//                  cipher.init(Cipher.ENCRYPT_MODE, (Key) cecPublicKey);
//                  byte[] M = cipher.doFinal(cM);
//                  byte[] DS = cipher.doFinal(cDS);
//                  byte[] B = cipher.doFinal(cB);

                RC4 cipher = new RC4(cecPublicKey.getExponent().toByteArray());

                byte[] cM = cipher.encrypt(M.toByteArray());
                byte[] cDS = cipher.encrypt(DS);
                byte[] cB = cipher.encrypt(choice.getBytes());

                sendBytes(clientSocket, cM);
                sendBytes(clientSocket, cDS);
                sendBytes(clientSocket, cB);

                System.out.println("Голос отправлен в ЦИК");

            }  finally { // в любом случае необходимо закрыть сокет и потоки
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static void registerVoter(BigInteger voterId, AsymmetricKeyParameter publicKeyAsym) throws IOException {

        RSAKeyParameters publicKey = (RSAKeyParameters)publicKeyAsym;

        Socket KCASocket = new Socket("localhost", 4006); // этой строкой мы запрашиваем; //сокет для общения
        BufferedReader inKCA = new BufferedReader(new InputStreamReader(KCASocket.getInputStream()));; // поток чтения из сокета
        BufferedWriter outKCA = new BufferedWriter(new OutputStreamWriter(KCASocket.getOutputStream())); // поток записи в сокет

        try {
            try {
            //    System.out.println("registerVoter() ");

                outKCA.write("registerVoter" + "\n"); // отправляем запрос на сервер
//                outKCA.flush();

//                result = new AsymmetricCipherKeyPair(
//                        new RSAKeyParameters(false, n, e),
//                        new RSAPrivateCrtKeyParameters(n, e, d, p, q, dP, dQ, qInv));

                //n = P*Q - модуль
                //e - exponent
                //(e,n) - открытый ключ
                BigInteger modulus = publicKey.getModulus();
                BigInteger exponent = publicKey.getExponent();

                outKCA.write(voterId + "\n");
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

    private static RSAKeyParameters getCECPublicKey() throws IOException {
        Socket KCASocket = new Socket("localhost", 4006); // этой строкой мы запрашиваем; //сокет для общения
        BufferedReader inKCA = new BufferedReader(new InputStreamReader(KCASocket.getInputStream()));; // поток чтения из сокета
        BufferedWriter outKCA = new BufferedWriter(new OutputStreamWriter(KCASocket.getOutputStream())); // поток записи в сокет

        RSAKeyParameters publicKey = null;
        try {
            try {
                //System.out.println("getCECPublicKey() ");

                outKCA.write("getCECPublicKey" + "\n"); // отправляем запрос на сервер
                outKCA.flush();

                String n = inKCA.readLine();
                String e = inKCA.readLine();
                publicKey = new RSAKeyParameters(false, new BigInteger(n), new BigInteger(e));

//                byte[] arr = readBytes(KCASocket);
//                publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(arr));

                //System.out.println("cecPublicKey = " + publicKey); // получив - выводим на экран
            } finally { // в любом случае необходимо закрыть сокет и потоки
                //System.out.println("Клиент был закрыт...");
                KCASocket.close();
                inKCA.close();
                inKCA.close();
            }
        } catch (IOException  e) {
            System.err.println(e);
        }
        return publicKey;
    }

    private static List<String> getCandidates() throws IOException {
        String server_greeting = in.readLine();
        System.out.println(server_greeting);

        // прочитать всех кандитатов с сервера
        ArrayList<String> candidates = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String response = in.readLine();
            candidates.add(response);
            out.flush(); // выталкиваем все из буфера
        }

        for(int i=0;i<candidates.size();i++){
            System.out.println((i+1) + " - " + candidates.get(i));
        }
        return candidates;
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
