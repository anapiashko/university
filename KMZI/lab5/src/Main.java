import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        Transposition t = new Transposition();

        int k[] = {1,2,3};
        t.SetKey(k);

        String encr = t.Encrypt("inputTextBox.Text");

        System.out.println("encr = ");
        System.out.println(encr);

        System.out.println("decr = ");
        System.out.println(t.Decrypt(encr));

        String input = "your string";
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(input.getBytes("UTF-8"));
        byte[] hash = digest.digest();
        System.out.println(Arrays.toString(hash));

    }
}
