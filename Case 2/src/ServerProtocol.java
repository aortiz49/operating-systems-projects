import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Represents the server protocol.
 */
public class ServerProtocol {

    public static void process(BufferedReader pIn, PrintWriter pOut) throws IOException{
        String inputLine;
        String outputLine;

        // reads input stream
        inputLine = pIn.readLine();
        System.out.println("Input to be processed: " + inputLine);

        // process input
        outputLine = inputLine;

        // writes in the output
        pOut.println(outputLine);
        System.out.println("Processed Exit: "+outputLine);

    }
}