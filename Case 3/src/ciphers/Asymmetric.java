package ciphers;/*
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

//===================================================
// Imports
//===================================================

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

/**
 * Class that performs asymmetric encryption and decryption.
 *
 * @author lp.cardozo@uniandes.edu.co
 * @author a.ortizg@uniandes.edu.co
 */
public class Asymmetric {
    //===================================================
    // Methods
    //===================================================

    /**
     * Encrypts the given text using an algorithm of choice with a given key.
     *
     * @param pKey       the key used to encrypt the data
     * @param pAlgorithm the type of algorithm to use for the encryption
     * @param pText      the text to be encrypted
     * @return the byte representation of the encrypted text.
     */
    public static byte[] encrypt(Key pKey, String pAlgorithm, String pText) {
        byte[] cipheredText;

        try {
            Cipher cipherer = Cipher.getInstance(pAlgorithm);
            byte[] clearText = DatatypeConverter.parseBase64Binary(pText);


            cipherer.init(Cipher.ENCRYPT_MODE, pKey);
            cipheredText = cipherer.doFinal(clearText);

            return cipheredText;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }
    }

    /**
     * Decrypts the given text using an algorithm of choice with a given key.
     *
     * @param pKey       the key used to encrypt the data
     * @param pAlgorithm the type of algorithm to use for the decryption
     * @param pText      the text to be decrypted
     * @return the byte representation of the decrypted text.
     */
    public static byte[] decipher(Key pKey, String pAlgorithm, byte[] pText) {
        byte[] clearText;

        try {
            Cipher cipherer = Cipher.getInstance(pAlgorithm);
            cipherer.init(Cipher.DECRYPT_MODE, pKey);
            clearText = cipherer.doFinal(pText);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }

        return clearText;
    }
}