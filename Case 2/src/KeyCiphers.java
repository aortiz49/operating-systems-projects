import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class KeyCiphers{
    private final static String ALGORITHM = "AES";

    public static void main(String[] args) throws NoSuchAlgorithmException {

        // symmetric key1
        SecretKey k1;

        // symmetric key2
        SecretKey k2;

        // generate the symmetric key
        KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);

        k1 = keygen.generateKey();
        k2 = keygen.generateKey();


    }
}