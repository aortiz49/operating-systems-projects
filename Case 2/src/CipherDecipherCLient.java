import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

/**
 * Class to test the symmetric cypher
 */
public class CipherDecipherCLient {
    private final static String ALGORITHM = "AES";

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        // secret key
        SecretKey secretKey;

        // ciphered text
        byte[] cipheredText;

        // deciphered text
        byte[] decipheredText;

        // input reader
        BufferedReader pIn = new BufferedReader(new InputStreamReader(System.in));

        // get user input
        System.out.println("Enter a text string: ");
        String input = pIn.readLine();

        // display the input in the console
        System.out.println("Input message: " + input);

        // convert user input into bytes
        byte[] clearText = input.getBytes();

        // display bytes
        System.out.print("Clear text: ");
        printBytes(clearText);

        // generate the secret key
        KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
        secretKey = keygen.generateKey();
        cipheredText = Symmetric.cipher(secretKey, input);

        // print cyphered text
        if (cipheredText != null) {
            System.out.print("Ciphered text: ");
            printBytes(cipheredText);
        }

        // decipher text
        decipheredText = Symmetric.decipher(secretKey, cipheredText);
        System.out.print("Deciphered text: ");
        printBytes(decipheredText);

        // print in string format
        System.out.println("Deciphered text: " + new String(decipheredText));

    }

    public static void printBytes(byte[] content) {
        int i = 0;
        for (; i < content.length - 1; i++) {
            System.out.print(content[i] + " ");
        }
        System.out.print(content[i] + "\n");
    }
}