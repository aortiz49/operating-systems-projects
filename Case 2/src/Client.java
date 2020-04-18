import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Scanner;

import javax.security.cert.CertificateEncodingException;

import org.bouncycastle.operator.OperatorCreationException;

/**
 * Represents a client in the system.
 */
public class Client {

    public static final String SERVER = "localhost";

    /**
     * Main method.
     *
     * @param args arguments
     * @throws IOException if there is an I/O exception
     * @throws NoSuchAlgorithmException
     * @throws CertificateEncodingException
     * @throws CertificateException
     * @throws OperatorCreationException
     */
    public static void main(String[] args) throws IOException, CertificateEncodingException, NoSuchAlgorithmException, OperatorCreationException, CertificateException {

        Scanner sc = new Scanner(System.in);
        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader  = null;
        int port;

        System.out.println("Client....");
        System.out.print("Write the connection port with the server: ");
        port = sc.nextInt();

        try{
            // creates a socket on the client-side
            socket = new Socket(SERVER,port);

            // connects io streams
            writer = new PrintWriter(socket.getOutputStream(),true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }

        // creates a stream to read what the client writes
        BufferedReader stdInd = new BufferedReader(new InputStreamReader(System.in));

        // executes the protocol on the client-side
        ClientProtocol.process(stdInd,reader,writer);

        // closes streams and socket
        stdInd.close();
        writer.close();
        reader.close();
        socket.close();

    }
}