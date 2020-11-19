public class Main {
    public static void main(String[] args) {
        String key = "key";
        RC4 rc4_c = new RC4(key.getBytes());

        String message = "massage";
        byte[] encrypt = rc4_c.encrypt(message.getBytes());

        RC4 rc4_s = new RC4(key.getBytes());
        byte[] decrypt = rc4_s.decrypt(encrypt);
        System.out.println(new String(decrypt));

        byte[] byteMessage = message.getBytes();
        for (int i = 0; i < message.getBytes().length ; i++) {
            System.out.print(byteMessage[i] +" ");
        }
        System.out.println();

        for (int i = 0; i < decrypt.length; i++) {
            System.out.print(decrypt[i] + " ");
        }

    }
}
