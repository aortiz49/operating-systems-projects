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
//===================================================
// Imports
//===================================================

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Represents the client protocol used to exchange messages with the server.
 *
 * @author a.ortizg@uniandes.edu.co
 * @author lp.cardozo@uniandes.edu.co
 */
public class ClientProtocol {
    //===================================================
    // Constants
    //===================================================

    /**
     * The SYN signal to be sent to the server to start communication.
     */
    private static final String HELLO = "HOLA";

    /**
     * The SYN-ACK signal expected from the server.
     */
    private static final String OK = "OK";

    /**
     * The error signal.
     */
    private static final String ERROR = "ERROR";

    /**
     * Symmetric-key encryption algorithm.
     */
    private static final String AES = "AES";

    /**
     * Asymmetric-key encryption algorithm.
     */
    private static final String RSA = "RSA";

    /**
     * Keyed-hash message authentication algorithm.
     */
    private static final String HMAC_SHA1 = "HMACSHA1";

    //===================================================
    // Attributes
    //===================================================

    /**
     * The buffer used for user input.
     */
    private static BufferedReader inputFromUser;

    /**
     * The buffer used for server response.
     */
    private static BufferedReader responseFromServer;

    /**
     * The output writer from the client.
     */
    private static PrintWriter outputFromClient;

    //===================================================
    // Communication Protocol
    //===================================================

    /**
     * The main client method to process the protocol.
     *
     * @param stdIn the standard console input from the user
     * @param pIn   the input buffer to the client from the server
     * @param pOut  the output sent from the client
     */
    public static void process(BufferedReader stdIn, BufferedReader pIn, PrintWriter pOut)
            throws IOException, NoSuchAlgorithmException, CertificateException,
            OperatorCreationException {

        // assign the input from the user
        inputFromUser = stdIn;

        // assign the output buffer from the client
        outputFromClient = pOut;

        // assign the response from the server
        responseFromServer = pIn;

        //===================================================
        // Stage 1:
        // 1.   The client requests a connection
        //      by sending a synchronize message to the server
        // 2.   The server acknowledges this request by sending
        //      SYN-ACK back to the client.
        // 3.   The client responds with an ACK by sending the
        //      three encryption algorithms it will use to
        //      generate the keys.
        // 4.   If the server accepts the algorithm sequence,
        //      it sends an approval message. Else, it sends
        //      an error message.
        // -> Continue to stage 2
        //===================================================

        // send the syn signal
        SYN();

        // handle the syn-ack signal
        SYN_ACK();


        // send the ack signal
        ACK();

        // handle serve response
        if (!accepted()) {
            System.exit(-1);
        }

        //===================================================
        // Stage 2A:
        // 1.   The client sends a certificate to the server.
        // 2.   The server accepts or rejects the certificate.
        //  2a. If the server accepts the client's certificate,
        //      it sends an "OK" signal to the client.
        //  2b. If the server rejects the client's certificate,
        //      the communication protocol is aborted.
        // 3.   If the server accepts the algorithm sequence,
        //      it sends an approval message. Else, it sends
        //      an error message.
        // -> Continue to stage 2B
        //===================================================

        // add a provider to the next position available
        Security.addProvider(new BouncyCastleProvider());

        // generate the key pair
        KeyPair keypair = generateAsymmetricKeyPair();

        // generate client-side certificate
        X509Certificate clientCertificate = generateCertificate(keypair);

        // send certificate to the server
        sendCertificate(clientCertificate);

        // receive server response
        receiveCertificate();


        //// stage 2b

        //C(K_C,K_sc)

    }

    //===================================================
    // Auxiliary Methods
    //===================================================

    /**
     * Sends the synchronize message to the server.
     */
    private static void SYN() {
        // send HELLO to the server
        System.out.println("\n========== SYN ==========");
        System.out.println("Message to server: " + HELLO);
        outputFromClient.println(HELLO);
    }

    /**
     * Handles the acknowledgement from the server.
     *
     * @throws IOException if there is an I/O error.
     */
    private static void SYN_ACK() throws IOException {
        String fromServer = "";

        if ((fromServer = responseFromServer.readLine()).equals(OK)) {
            System.out.println("\n========== SYN-ACK ==========");
            System.out.println("Response from server: " + OK);
        }
    }

    /**
     * Sends the acknowledge message to the server.
     */
    private static void ACK() {
        String message = "Message to server: ALGORITMOS:" + AES + ":" + RSA + ":" + HMAC_SHA1;
        // send the algorithms string to the server
        System.out.println("\n========== ACK ==========");
        System.out.println(message);
        outputFromClient.println("ALGORITMOS:" + AES +":" + RSA + ":" + HMAC_SHA1);
    }

    /**
     * Checks if the algorithm acknowledge string was accepted.
     *
     * @return true if the server accepted the string. False otherwise.
     */
    private static boolean accepted() throws IOException {
        boolean isAccepted = false;
        String fromServer = "";

        if ((fromServer = responseFromServer.readLine()).equals(OK)) {
            System.out.println("\n========== SERVER RESP ==========");
            System.out.println("Response from server: " + OK);
            isAccepted = true;
        }

        else if ((fromServer).equals(ERROR)) {
            System.out.println("\n========== SERVER RESP ==========");
            System.out.println("Response from server: " + ERROR);
        }

        return isAccepted;
    }

    /**
     * Returns an X509 certificate.
     *
     * @param keyPair the asymmetric key pair used to build the certificate
     * @return the X509 certificate
     * @throws OperatorCreationException if there is an exception when creating a certificate
     * @throws CertificateException      if there is an exception when creating a certificate
     */
    private static X509Certificate generateCertificate(KeyPair keyPair)
            throws OperatorCreationException, CertificateException {

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.YEAR, 10);
        X509v3CertificateBuilder x509v3CertificateBuilder =
                new X509v3CertificateBuilder(new X500Name("CN=localhost"), BigInteger.valueOf(1),
                                             Calendar.getInstance().getTime(),
                                             endCalendar.getTime(), new X500Name("CN=localhost"),
                                             SubjectPublicKeyInfo.getInstance(
                                                     keyPair.getPublic().getEncoded()));
        ContentSigner contentSigner =
                new JcaContentSignerBuilder("SHA1withRSA").build(keyPair.getPrivate());
        X509CertificateHolder x509CertificateHolder = x509v3CertificateBuilder.build(contentSigner);
        return new JcaX509CertificateConverter().setProvider("BC")
                                                .getCertificate(x509CertificateHolder);

    }

    /**
     * Generates the asymmetric key pair that will be used to create the certificate.
     *
     * @return the 1024-bit keypair
     * @throws NoSuchAlgorithmException if the algorithm that is requested isn't in the environment
     */
    private static KeyPair generateAsymmetricKeyPair() throws NoSuchAlgorithmException {
        // generates the asymmetric keys
        KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
        generator.initialize(1024);

        return generator.generateKeyPair();
    }

    /**
     * Converts the X509 certificate to a string and sends it to the server.
     *
     * @param pCertificate the client's X509 certificate
     */
    public static void sendCertificate(X509Certificate pCertificate)
            throws CertificateEncodingException {
        System.out.println("\n========== SENDING CLIENT CERTIFICATE ==========");
        byte[] certBytes = pCertificate.getEncoded();
        String certString = DatatypeConverter.printBase64Binary(certBytes);
        System.out.println("Certificate to send: " + certString);
        outputFromClient.println(certString);
    }

    public static void receiveCertificate() throws IOException {
        String fromServer = "";

        // print the server response
        if ((fromServer = responseFromServer.readLine()).equals(OK)) {
            System.out.println("\n========== RECEIVING SERVER CERTIFICATE ==========");
            System.out.println("Response from server: " + OK);
        }

        // print the server certificate
        fromServer = responseFromServer.readLine();
        System.out.println("Server certificate: " + fromServer);
        System.out.println("Message to server: " + OK);

        // send OK (assuming the server's certificate in this case will be OK)
        outputFromClient.println(OK);

    }

}