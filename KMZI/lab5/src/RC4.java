public class RC4 {

    private final byte[] key;

    private  byte[] S = new byte[256];
    private  byte[] T = new byte[256];
    private  int keyLen;

    public RC4(final byte[] key) {
        this.key = key;
        if (key.length < 1 || key.length > 256) {
            throw new IllegalArgumentException(
                    "key must be between 1 and 256 bytes");
        } else {
            init();
        }
    }

    private void init(){
        keyLen = key.length;
        for (int i = 0; i < 256; i++) {
            S[i] = (byte) i;
            T[i] = key[i % keyLen];
        }
        int j = 0;
        byte tmp;
        for (int i = 0; i < 256; i++) {
            j = (j + S[i] + T[i]) & 0xFF; // FF = 255 в десятичной
            tmp = S[j];
            S[j] = S[i];
            S[i] = tmp;
        }
    }

    public byte[] encrypt(final byte[] plaintext) {
        init();
        final byte[] ciphertext = new byte[plaintext.length];
        int i = 0, j = 0, k, t;
        byte tmp;
        for (int counter = 0; counter < plaintext.length; counter++) {
            i = (i + 1) & 0xFF;
            j = (j + S[i]) & 0xFF;
            tmp = S[j];
            S[j] = S[i];
            S[i] = tmp;
            t = (S[i] + S[j]) & 0xFF;
            k = S[t];
            ciphertext[counter] = (byte) (plaintext[counter] ^ k);
        }
        return ciphertext;
    }

    public byte[] decrypt(final byte[] ciphertext) {
        return encrypt(ciphertext);
    }
}