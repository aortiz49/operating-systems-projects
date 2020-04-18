import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Represents a client in the system.
 */
public class Client {

    public static final int PORT = 3400;
    public static final String SERVER = "localhost";

    /**
     * Main method.
     *
     * @param args arguments
     * @throws IOException if there is an I/O exception
     */
    public static void main(String[] args) throws IOException {

        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader  = null;

        System.out.println("Client....");

        try{
            // creates a socket no the client-side
            socket = new Socket(SERVER,PORT);

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