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

/**
 * Represents the client protocol used to exchange messages with the server.
 *
 * @author a.ortizg@uniandes.edu.co
 * @author lm.sierra20@uniandes.edu.co
 */
public class ClientProtocol {
    //===================================================
    // Constants
    //===================================================

    /**
     * The SYN signal to be sent to the server to start communication.
     */
    private static final String HELLO = "HELLO";

    /**
     * The SYN-ACK signal expected from the server.
     */
    private static final String OK = "OK";

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
    // Constants
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
    public static void process(BufferedReader stdIn, BufferedReader pIn, PrintWriter pOut) {

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
        //===================================================

        // 1. send the syn signal
        SYN();

        // 2. handle the syn-ack signal
        try {
            SYN_ACK();
        } catch (IOException e) {
            e.getMessage();
        }

        // 3. send the ack signal
        ACK();

        // 4. handle serve response
        try {
            if (!accepted()) {
                System.exit(-1);
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    //===================================================
    // Auxiliary Methods
    //===================================================

    /**
     * Sends the synchronize message to the server.
     */
    public static void SYN() {
        // send HELLO to the server
        System.out.println("\n========== SYN ==========");
        System.out.println("Message to server: \"" + HELLO + "\"");
        outputFromClient.println("\"" + HELLO + "\"");
    }

    /**
     * Handles the acknowledgement from the server.
     *
     * @throws IOException if there is an I/O error.
     */
    public static void SYN_ACK() throws IOException {
        String fromServer = "";

        if ((fromServer = responseFromServer.readLine()).equals("\"OK\"")) {
            System.out.println("\n========== SYN-ACK ==========");
            System.out.println("Response from server: " + "\"OK\"");
        }
    }

    /**
     * Sends the acknowledge message to the server.
     */
    public static void ACK() {
        String message = "Message to server: \"ALGORITHMS\":" + AES + ":" + RSA + ":" + HMAC_SHA1;
        // send the algorithms string to the server
        System.out.println("\n========== SYN ==========");
        System.out.println(message);
        outputFromClient.println(message);
    }

    /**
     * Checks if the algorithm acknowledge string was accepted.
     *
     * @return true if the server accepted the string. False otherwise.
     */
    public static boolean accepted() throws IOException {
        boolean isAccepted = false;
        String fromServer = "";

        if ((fromServer = responseFromServer.readLine()).equals("\"OK\"")) {
            System.out.println("\n========== SERVER RESP ==========");
            System.out.println("Response from server: " + "\"OK\"");
            isAccepted = true;
        }

        else if ((fromServer = responseFromServer.readLine()).equals("\"ERROR\"")) {
            System.out.println("\n========== SERVER RESP ==========");
            System.out.println("Response from server: " + "\"ERROR\"");
        }

        return isAccepted;
    }

}