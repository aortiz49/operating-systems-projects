package communication_protocol.client;/*
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

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

import ciphers.Asymmetric;
import ciphers.Symmetric;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * Represents a client in the system.
 *
 * @author a.ortizg@uniandes.edu.co
 * @author lp.cardozo@uniandes.edu.co
 */
public class Client {
    //===================================================
    // Constants
    //===================================================

    /**
     * The SYN signal to be sent to the server to start communication.
     */
    private final String HELLO = "HOLA";

    /**
     * The SYN-ACK signal expected from the server.
     */
    private final String OK = "OK";

    /**
     * The error signal.
     */
    private final String ERROR = "ERROR";

    /**
     * Ciphers.Symmetric-key encryption algorithm.
     */
    private final String AES = "AES";

    /**
     * Ciphers.Asymmetric-key encryption algorithm.
     */
    private final String RSA = "RSA";

    /**
     * Keyed-hash message authentication algorithm.
     */
    private final String HMAC_SHA1 = "HMACSHA1";

    //===================================================
    // Attributes
    //===================================================

    /**
     * The server hosting the client.
     */
    public final String SERVER = "localhost";

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

    /**
     * Ciphers.Symmetric private key between server and client
     */
    private static SecretKey symmetricServerClientKey;
    /**
     * The start time of the transaction.
     */
    private long startTime;

    /**
     * The end time of the transaction.
     */
    private long endTime;

    private Scanner sc;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private int port;

    /**
     * Constructs a new client
     */
    public Client() {

        try {

            // creates a socket on the client-side
            this.socket = new Socket(SERVER, 5454);

            // connects io streams
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    /**
     * The main client method to process the protocol.
     *
     * @throws IOException               when there is an I/O error.
     * @throws NoSuchAlgorithmException  when a particular cryptographic
     *                                   algorithm is requested but is not available in the
     *                                   environment.
     * @throws OperatorCreationException when there is a BouncyCastle exception.
     * @throws CertificateException      when there is a problem with the certificate.
     */
    public void process() throws IOException, NoSuchAlgorithmException, CertificateException,
            OperatorCreationException, InterruptedException {


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
        // Stage 2A: Certificate Authentication
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

        startTime = System.currentTimeMillis();

        // send certificate to the server
        sendCertificate(clientCertificate);

        // receive server response
        X509Certificate serverCertificate = receiveCertificate();

        //===================================================
        // Stage 2B: Encrypting/Decrypting
        // 1.   The server sends the client the secret key which
        //      is encrypted with the client's public key.
        // 2.   The client decrypts the secret key from the server
        //      using an RSA decryption on the client's public key.
        // 3.   The server sends the client a challenge which is
        //      encrypted with the secret key.
        // 4.   The client decrypts the challenge from the server with
        //      AES decryption on the secret key.
        // 5.   The client sends the server the challenge which is
        //      encrypted using RSA encryption on the server's public key.
        // 6.   The server decrypts the message and responds with OK or ERROR.
        // -> Continue to stage 3
        //===================================================

        // obtain bytes from asymmetric server/client key
        byte[] symmetricServerClientKey = decipherSecretKeyFromServer(keypair);

        // obtain bytes from challenge
        byte[] challengeBytes = decipherChallengeFromServer(symmetricServerClientKey);

        // obtain server's public key
        PublicKey serverPublicKey = getServerPublicKey(serverCertificate);

        // send challenge to server with deciphered server's public key
        sendChallengeToServer(serverPublicKey, challengeBytes);

        // receive server challenge response
        receiveServerChallengeResponse();

        //===================================================
        // Stage 3: Timestamp Request
        // 1.   The client sends the server an agent id entered
        //      by the user encrypted using AES encryption on the
        //      secret key.
        // 2.   The server decrypts the message and sends the client
        //      an encrypted time stamp.
        //  3.  The client decrypts the time stamp using AES
        //      encryption on the secret key.
        //  4.  The client send the sever an OK signal.
        //      (We are assuming the server in this particular case
        //      will not send an erroneous time stamp)
        //===================================================

        // send id to server
        sendIdToServer();

        // receive server response to the id
        decipherResponseToSeverIdRequest();

        // respond to the server after receiving hour
        respondToFinalServerRequest();

        endTime = System.currentTimeMillis();

        escribirMensaje(Long.toString(endTime-startTime));
    }

    /*
     * Generacion del archivo log.
     * Nota:
     * - Debe conservar el metodo .
     * - Es el Ãºnico metodo permitido para escribir en el log.
     */
    private synchronized void escribirMensaje(String pCadena) {

        try {
            File timeFile = new File("./times.txt");
            FileWriter fw = new FileWriter(timeFile,true);
            fw.write(pCadena + "\n");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * Sends the synchronize message to the server.
     */
    private void SYN() {
        // send HELLO to the server
        System.out.println("\n========== SYN ==========");
        System.out.println("Message to server: " + HELLO);
        writer.println(HELLO);
    }

    /**
     * Handles the acknowledgement from the server.
     *
     * @throws IOException if there is an I/O error.
     */
    private void SYN_ACK() throws IOException {
        String fromServer = "";

        if ((fromServer = reader.readLine()).equals(OK)) {
            System.out.println("\n========== SYN-ACK ==========");
            System.out.println("Response from server: " + OK);
        }
    }

    /**
     * Sends the acknowledge message to the server.
     */
    private void ACK() {
        String message = "Message to server: ALGORITMOS:" + AES + ":" + RSA + ":" + HMAC_SHA1;
        // send the algorithms string to the server
        System.out.println("\n========== ACK ==========");
        System.out.println(message);
        writer.println("ALGORITMOS:" + AES + ":" + RSA + ":" + HMAC_SHA1);
    }

    /**
     * Checks if the algorithm acknowledge string was accepted.
     *
     * @return true if the server accepted the string. False otherwise.
     * @throws IOException when there is an I/O error.
     */
    private boolean accepted() throws IOException {
        boolean isAccepted = false;
        String fromServer = "";

        if ((fromServer = reader.readLine()).equals(OK)) {
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
     * @throws OperatorCreationException when there is a BouncyCastle exception.
     * @throws CertificateException      when there is an error with a certificate.
     */
    private X509Certificate generateCertificate(KeyPair keyPair)
            throws OperatorCreationException, CertificateException {
        System.out.println("\n========== GENERATING CLIENT CERTIFICATE  ==========");
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

        System.out.println("CommunicationProtocol.Client certificate generated.");
        return new JcaX509CertificateConverter().setProvider("BC")
                                                .getCertificate(x509CertificateHolder);

    }

    /**
     * Generates the asymmetric key pair that will be used to create the certificate.
     *
     * @return the 1024-bit keypair
     * @throws NoSuchAlgorithmException when the algorithm that is requested isn't in the
     *                                  environment.
     */
    private KeyPair generateAsymmetricKeyPair() throws NoSuchAlgorithmException {
        // generates the asymmetric keys
        KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
        generator.initialize(1024);

        return generator.generateKeyPair();
    }

    /**
     * Converts the X509 certificate to a string and sends it to the server.
     *
     * @param pCertificate the client's X509 certificate
     * @throws java.security.cert.CertificateEncodingException when there is an error with the
     * certificate.
     */
    public void sendCertificate(X509Certificate pCertificate) throws CertificateEncodingException {
        System.out.println("\n========== SENDING CLIENT CERTIFICATE TO SERVER ==========");
        byte[] certBytes = pCertificate.getEncoded();
        String certString = DatatypeConverter.printBase64Binary(certBytes);
        System.out.println("Certificate to send: " + certString);
        writer.println(certString);
    }

    /**
     * Returns the server's X509 certificate.
     *
     * @return the X509 certificate.
     * @throws IOException when there is an error with an I/O operation.
     */
    private X509Certificate receiveCertificate() throws IOException {
        String fromServer = "";

        // print the server response
        if ((fromServer = reader.readLine()).equals(OK)) {
            System.out.println("\n========== RECEIVING SERVER CERTIFICATE ==========");
            System.out.println("Response from server: " + OK);
        }

        // print the server certificate
        fromServer = reader.readLine();
        System.out.println("Server certificate: " + fromServer);
        System.out.println("Message to server: " + OK);

        // send OK (assuming the server's certificate in this case will be OK)
        writer.println(OK);

        // generate server certificate
        byte[] serverCertificateBytes = DatatypeConverter.parseBase64Binary(fromServer);
        CertificateFactory factory = new CertificateFactory();
        X509Certificate serverCertificate = null;
        try {
            serverCertificate = (X509Certificate) factory
                    .engineGenerateCertificate(new ByteArrayInputStream(serverCertificateBytes));
        } catch (CertificateException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return serverCertificate;
    }

    /**
     * Deciphers the secret key from the server using RSA and returns its byte representation.
     *
     * @param pKeyPair the server's key pair containing its public and private keys
     * @return the byte representation of the secret key
     * @throws IOException when there is an error with an I/O operation.
     */
    public byte[] decipherSecretKeyFromServer(KeyPair pKeyPair) throws IOException {
        System.out.println("\n========== DECIPHERING SECRET KEY FROM SERVER ==========");
        Key privateKey = pKeyPair.getPrivate();
        String fromServer = reader.readLine();

        byte[] privateKeyBytes = Asymmetric
                .decipher(privateKey, RSA, DatatypeConverter.parseBase64Binary(fromServer));
        System.out.println("Private symmetric server/client key from server: " + Arrays
                .toString(privateKeyBytes));

        return privateKeyBytes;
    }

    /**
     * Deciphers the challenge from the server using AES and returns its byte representation.
     *
     * @param pServerClientKeyBytes the secret key between sever/client
     * @return the byte representation of the deciphered challenge
     * @throws IOException when there is an error with an I/O operation.
     */
    public byte[] decipherChallengeFromServer(byte[] pServerClientKeyBytes) throws IOException {
        // the challenge string
        System.out.println("\n========== DECIPHERING CHALLENGE FROM SERVER ==========");
        String fromServer = reader.readLine();
        symmetricServerClientKey = new SecretKeySpec(pServerClientKeyBytes, AES);

        byte[] challengeBytes = Symmetric
                .decrypt(symmetricServerClientKey, DatatypeConverter.parseBase64Binary(fromServer));

        System.out
                .println("The deciphered bytes from challenge: " + Arrays.toString(challengeBytes));
        return challengeBytes;
    }

    /**
     * Returns the server's public key.
     *
     * @param pServerCertificate the server's certificate.
     * @return the server's public key
     */
    private PublicKey getServerPublicKey(X509Certificate pServerCertificate) {
        System.out.println("\n========== OBTAINING SERVER'S PUBLIC KEY ==========");
        System.out.println("Server public key: " + pServerCertificate.getPublicKey());
        return pServerCertificate.getPublicKey();
    }

    /**
     * Sends the challenge to the server using RSA.
     *
     * @param pServerKey      the server's pubic key.
     * @param pChallengeBytes the byte representation of the deciphered challenge
     */
    private void sendChallengeToServer(PublicKey pServerKey, byte[] pChallengeBytes) {

        // obtain string representation of the challenge bytes
        String challengeStr = DatatypeConverter.printBase64Binary(pChallengeBytes);

        // cipher the challenge with server public key
        byte[] cipheredChallenge = Asymmetric.encrypt(pServerKey, RSA, challengeStr);

        String cipheredString = DatatypeConverter.printBase64Binary(cipheredChallenge);

        // send challenge to the server
        System.out.println("========== SENDING CHALLENGE TO SERVER ==========");
        System.out.println("Challenge to server: " + Arrays.toString(cipheredChallenge));
        writer.println(cipheredString);
    }

    /**
     * Handles the reception of the server response after sending the challenge.
     *
     * @throws IOException when there is an error with an I/O operation.
     */
    private void receiveServerChallengeResponse() throws IOException {
        System.out.println("\n========== RECEIVING SERVER CHALLENGE RESPONSE ==========");

        // the server response
        String fromServer = reader.readLine();
        System.out.println("Message from server: " + fromServer);
    }

    /**
     * Sends the id of the agent to the server.
     *
     * @throws IOException when there is an error with an I/O operation.
     */
    private void sendIdToServer() throws IOException {
        System.out.println("\n========== SENDING AGENT ID TO SERVER ==========");
        int id = (int) Math.floor(Math.random()*(9999-1000+1)+1000);

        byte[] cypheredAgentId = Symmetric.encrypt(symmetricServerClientKey, Integer.toString(id));

        String cypheredAgentIdString = DatatypeConverter.printBase64Binary(cypheredAgentId);
        writer.println(cypheredAgentIdString);
    }

    /**
     * Decipher the server response to the id request.
     *
     * @throws IOException when there is an error with an I/O operation.
     */
    private void decipherResponseToSeverIdRequest() throws IOException {
        String fromServer = reader.readLine();
        byte[] fromServerByte = DatatypeConverter.parseBase64Binary(fromServer);

        byte[] decipheredResponse = Symmetric.decrypt(symmetricServerClientKey, fromServerByte);

        if (fromServer != null) {
            System.out.println("\n========== RECEIVING SERVER RESPONSE TO ID ==========");
            System.out.println("Response from server to Id request: " + DatatypeConverter
                    .printBase64Binary(decipheredResponse));

        }
    }

    /**
     * The client's final response to the server request.
     */
    private void respondToFinalServerRequest() {
        // send final response to the
        System.out.println("\n========== SENDING FINAL RESPONSE TO THE SERVER ==========");
        System.out.println(OK);
        writer.println(OK);
    }

}

