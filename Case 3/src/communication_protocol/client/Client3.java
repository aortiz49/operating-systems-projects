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
package communication_protocol.client;
//===================================================
// Imports
//===================================================

import org.bouncycastle.operator.OperatorCreationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Scanner;

/**
 * Represents a client in the system.
 *
 * @author a.ortizg@uniandes.edu.co
 * @author lp.cardozo@uniandes.edu.co
 */
public class Client3 {
    //===================================================
    // Constants
    //===================================================

    /**
     * The server hosting the client.
     */
    public static final String SERVER = "localhost";

    //===================================================
    // Methods
    //===================================================

    /**
     * The main methods of the client.
     *
     * @param args arguments to the main method
     * @throws IOException               when there is an I/O error.
     * @throws NoSuchAlgorithmException  when a particular cryptographic
     *                                   algorithm is requested but is not available in the
     *                                   environment.
     * @throws OperatorCreationException when there is a BouncyCastle exception.
     * @throws CertificateException      when there is a problem with the certificate.
     */
    public static void main(String[] args)
            throws IOException, NoSuchAlgorithmException, OperatorCreationException,
            CertificateException {

        Scanner sc = new Scanner(System.in);
        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        int port;

        System.out.println("CommunicationProtocol.Client....");
        System.out.print("Write the connection port with the server: ");
        port = sc.nextInt();

        try {
            // creates a socket on the client-side
            socket = new Socket(SERVER, port);

            // connects io streams
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // creates a stream to read what the client writes
        BufferedReader stdInd = new BufferedReader(new InputStreamReader(System.in));

        // executes the protocol on the client-side
        ClientProtocol.process(stdInd, reader, writer);

        // closes streams and socket
        stdInd.close();
        writer.close();
        reader.close();
        socket.close();

    }
}