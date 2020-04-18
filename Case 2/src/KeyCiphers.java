import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

public class KeyCiphers {
    private final static String ALGORITHM = "AES";

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        // symmetric key1
        SecretKey k1;

        // symmetric key2
        SecretKey k2;

        //ciphered text1
        byte[] tc1;

        //ciphered text2
        byte[] tc2;

        BufferedReader pIn = new BufferedReader(new InputStreamReader(System.in));

        // generate the symmetric key
        KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);

        k1 = keygen.generateKey();
        k2 = keygen.generateKey();


        System.out.println("Enter message 1: ");
        tc1 = Symmetric.cipher(k1, pIn.readLine());

        System.out.println("Enter message 2: ");
        tc2 = Symmetric.cipher(k2, pIn.readLine());




    }
}