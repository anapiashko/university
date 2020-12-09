import org.bouncycastle.crypto.params.RSAKeyParameters;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

//Центр Сертификации Ключей - ЦСК (Key Certification Authority - KCA)
public class KCA {

    private static Map<BigInteger, RSAKeyParameters> voters = new HashMap<>();
    private static RSAKeyParameters cecPublicKey;

    private static Socket clientSocket; //сокет для общения
    private static ServerSocket server; // серверсокет
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    private static InputStream inputStream;

    public static void main(String[] args) {

        try {
            try {
                server = new ServerSocket(4006); // серверсокет прослушивает порт 4004

                System.out.println("ЦСК запущен!"); // хорошо бы серверу объявить о своем запуске

                while (true) {

                    clientSocket = server.accept(); // accept() будет ждать пока кто-нибудь не захочет подключиться

                    try {
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                        inputStream = clientSocket.getInputStream();

                        String request = in.readLine(); // ждём пока клиент что-нибудь нам напишет
                        System.out.println("Request from client : " + request + " and response = " + cecPublicKey );

                        if(request.equals("getCECPublicKey")){
                            //n
                            out.write(cecPublicKey.getModulus().toString() + "\n");
                            //e
                            out.write(cecPublicKey.getExponent().toString() + "\n");
                            out.flush(); // выталкиваем все из буфера
//                            byte[] arr = cecPublicKey.getEncoded();
//                            sendBytes(clientSocket, arr);
                        }else if(request.equals("registerCECPublicKey")){
                            String n = in.readLine();
                            String e = in.readLine();
                            cecPublicKey = new RSAKeyParameters(false, new BigInteger(n), new BigInteger(e));
//                            byte[] arr = readBytes(clientSocket);
//                            cecPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(arr));
//                            System.out.println("publ = " + cecPublicKey.toString());
                        }else if(request.equals("registerVoter")){
                            String voterId = in.readLine();
                            String n = in.readLine();
                            String e = in.readLine();
                            RSAKeyParameters publicKey = new RSAKeyParameters(false, new BigInteger(n), new BigInteger(e));
                            voters.put(new BigInteger(voterId), publicKey);
                        }else if(request.equals("getVoterPublicKey")){
                            String voterId = in.readLine();
                            RSAKeyParameters publicKey = voters.get(new BigInteger(voterId));
                            //n
                            out.write(publicKey.getModulus().toString() + "\n");
                            //e
                            out.write(publicKey.getExponent().toString() + "\n");
                            out.flush(); // выталкиваем все из буфера
                        }

                    } finally { // в любом случае сокет будет закрыт
                        clientSocket.close();
                        // потоки тоже хорошо бы закрыть
                        in.close();
                        out.close();
                    }
                }
            } finally {
                System.out.println("ЦСК закрыт!");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
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
    }
}
