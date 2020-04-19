import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

public class Asymmetric {

    public static byte[] cifrar(Key llave, String algoritmo, String texto) {
        byte[] textoCifrado;

        try {
            Cipher cifrador = Cipher.getInstance(algoritmo);
            byte[] textoClaro = DatatypeConverter.parseBase64Binary(texto);


            cifrador.init(Cipher.ENCRYPT_MODE, llave);
            textoCifrado = cifrador.doFinal(textoClaro);

            return textoCifrado;
        } catch (Exception e) {
            System.out.println("Excepción: " + e.getMessage());
            return null;
        }
    }

    public static byte[] descifrar(Key llave, String algoritmo, byte[] texto) {
        byte[] textoClaro;

        try {
            Cipher cifrador = Cipher.getInstance(algoritmo);
            cifrador.init(Cipher.DECRYPT_MODE, llave);
            textoClaro = cifrador.doFinal(texto);
        } catch (Exception e) {
            System.out.println("Excepción: " + e.getMessage());
            return null;
        }

        return textoClaro;
    }

    public static void imprimir(byte[] contenido) {
        int i = 0;
        for (; i < contenido.length - 1; i++) {
            System.out.print(contenido[i] + " ");
        }
        System.out.println(contenido[i] + " ");
    }


}