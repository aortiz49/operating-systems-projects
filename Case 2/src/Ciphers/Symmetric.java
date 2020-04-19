/*
MIT License

Copyright (c) 2020 Universidad de los Andes - ISIS2603

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package Ciphers;
//===================================================
// Imports
//===================================================

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * Class that performs symmetric encryption and decryption.
 *
 * @author lp.cardozo@uniandes.edu.co
 * @author a.ortizg@uniandes.edu.co
 */
public class Symmetric {

    /**
     * Constant used to determine the padding mechanism.
     */
    private final static String PADDING = "AES/ECB/PKCS5Padding";

    /**
     * Encrypts the given pText using using an AES padding mechanism with a given key.
     *
     * @param pKey  the pKey used to encrypt the data
     * @param pText the pText to be encrypted
     * @return the byte representation of the encrypted pText.
     */
    public static byte[] encrypt(SecretKey pKey, String pText) {
        byte[] cipheredText;

        try {
            Cipher cipherer = Cipher.getInstance(PADDING);
            byte[] clearText = pText.getBytes();

            cipherer.init(Cipher.ENCRYPT_MODE, pKey);
            cipheredText = cipherer.doFinal(clearText);

            return cipheredText;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }
    }

    /**
     * Decrypts the given pText using using an AES padding mechanism with a given key.
     *
     * @param pKey  the pKey used to decrypt the data
     * @param pText the pText to be encrypted
     * @return the byte representation of the decrypted pText.
     */
    public static byte[] decrypt(SecretKey pKey, byte[] pText) {
        byte[] clearText;

        try {
            Cipher cipherer = Cipher.getInstance(PADDING);
            cipherer.init(Cipher.DECRYPT_MODE, pKey);
            clearText = cipherer.doFinal(pText);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }
        return clearText;
    }
}