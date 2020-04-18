import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Represents a server in the system.
 */
public class Server {
    public static final int PORT = 3400;

    public static void main(String[] args) throws IOException {
        ServerSocket ss = null;
        boolean cont = true;

        System.out.println("Main Server: ...");

        try {
            ss = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        while (cont) {
            // create the socket on the server-side
            // it stays blocked waiting for the client request

            Socket socket = ss.accept();

            try {
                // connect streams
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // execute the server protocol
                ServerProtocol.process(reader, writer);

                // closes streams and socket
                writer.close();
                reader.close();
                socket.close();

            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}