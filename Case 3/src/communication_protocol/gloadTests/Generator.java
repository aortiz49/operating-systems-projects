package communication_protocol.gloadTests;

//===================================================
// Imports
//===================================================

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;

import java.util.Scanner;

/**
 * GLoad Core Class - Task
 *
 * @author a.ortizg@uniandes.edu.co
 * @author lp.cardozo@uniandes.edu.co
 */
public class Generator {
    //===================================================
    // Attributes
    //===================================================

    /**
     * Load Generator Service. This will control the generator of all desired transactions.
     */
    private final LoadGenerator generator;

    //===================================================
    // Constructor
    //===================================================

    /**
     * Constructs a new generator with a specified load.
     */
    public Generator(int pNumTasks, int pDelay) {
        Task work = createTask();
        generator = new LoadGenerator("Client - Server Load Test", pNumTasks, work, pDelay);
        generator.generate();
    }

    //===================================================
    // Methods
    //===================================================


    private Task createTask() {
        return new ClientServerTask();
    }

    public static void main(String... args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the number of tasks: ");
        int numTasks = in.nextInt();
        System.out.println("Enter the delay between tasks (ms): ");
        int delay = in.nextInt();
        Generator gen = new Generator(numTasks,delay);
    }
}