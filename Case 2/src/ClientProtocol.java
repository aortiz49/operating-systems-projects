import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Represents the client protocol
 */
public class ClientProtocol {

    public static void process(BufferedReader stdIn, BufferedReader pIn, PrintWriter pOut) throws
            IOException {

        // reads the input
        System.out.println("Write the message to send: ");
        String fromUser = stdIn.readLine();

        // send through tne network
        pOut.println(fromUser);

        String fromServer = "";

        // read what arrives from the network
        // if the server response isn't null, display response
        if((fromServer = pIn.readLine()) !=null){
            System.out.println("Response from Sever: " +  fromServer);
        }
    }
}