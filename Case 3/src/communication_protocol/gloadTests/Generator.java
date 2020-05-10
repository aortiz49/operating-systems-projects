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
    public Generator(int pNumTasks) {
        Task work = createTask();
        int numberOfTasks = pNumTasks;
        int gapBetweenTasks = 40; // 1000ms between tasks
        generator = new LoadGenerator("Client - Server Load Test", numberOfTasks, work,
                                      gapBetweenTasks);
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
        Generator gen = new Generator(numTasks);
    }
}