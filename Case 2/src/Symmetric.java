import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * Represents a symmetric cipher
 */
public class Symmetric {

    /**
     * Constant used to determine the padding mechanism
     */
    private final static String PADDING = "AES/ECB/PKCS5Padding";

    public static byte[] cipher(SecretKey key, String text){
        byte[] cipheredText;

        try{
            Cipher cipherer = Cipher.getInstance(PADDING);
            byte[] clearText = text.getBytes();

            cipherer.init(Cipher.ENCRYPT_MODE,key);
            cipheredText = cipherer.doFinal(clearText);

            return cipheredText;
        }
        catch(Exception e){
            System.out.println("Exception: "  + e.getMessage());
            return null;
        }
    }


    public static byte[] decipher(SecretKey key, byte[] text){
        byte[] clearText;

        try{
            Cipher cipherer = Cipher.getInstance(PADDING);
            cipherer.init(Cipher.DECRYPT_MODE,key);
            clearText = cipherer.doFinal(text);
        }
        catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
            return null;
        }
        return clearText;
    }

}