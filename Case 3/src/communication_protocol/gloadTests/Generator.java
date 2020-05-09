package communication_protocol.gloadTests;

//===================================================
// Imports
//===================================================

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;

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
    public Generator() {
        Task work = createTask();
        int numberOfTasks = 800;
        int gapBetweenTasks = 20; // 1000ms between tasks
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
        Generator gen = new Generator();
    }
}