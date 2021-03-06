package communication_protocol.server;

import java.io.*;
import java.lang.management.ManagementFactory;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Class that monitors the CPU usage of the program.
 */
public class CpuMonitor extends Thread {
    //===================================================
    // Attributes
    //===================================================

    /**
     * The file used to store the cpu data.
     */
    private static File cpuFile;
    private static File lostFile;


    public static int tasksToProcess;
    public static int processedTasks;


    //===================================================
    // Constructor
    //===================================================

    public CpuMonitor(int pT) throws IOException {

    	tasksToProcess = pT;
        cpuFile = new File("./cpu.txt");
        if (!cpuFile.exists()) {
            cpuFile.createNewFile();
        }
        FileWriter  fw = new FileWriter(cpuFile);
        fw.close();

        lostFile = new File("./lost.txt");
        if (!lostFile.exists()) {
            lostFile.createNewFile();
        }
        fw = new FileWriter(lostFile);
        fw.close();
    }

    /**
     * The run method of the thread
     */
    public void run() {
        while(tasksRemaining()) {
            try {
                Thread.sleep(100);
                logCpu(Double.toString(getSystemCpuLoad()));

            } catch (Exception e) {
                //Thread.currentThread().interrupt();
                e.getMessage();
            }
        }
        C.threadpool.shutdownNow();
        try {
        	getLostTransactions();
        }
        catch (Exception e) {
        	e.getStackTrace();
        }
    }

    private synchronized void logCpu(String pString) {

        try {
            FileWriter fw = new FileWriter(cpuFile, true);
            fw.write(pString + "\n");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void logLost(String pString) {

        try {
            FileWriter fw = new FileWriter(lostFile, true);
            fw.write(pString + "\n");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Measures the current CPU load.
     *
     * @return the percent usage of the cpu
     * @throws Exception if there is an error (lol?)
     */
    public double getSystemCpuLoad() throws Exception {

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[]{"SystemCpuLoad"});

        if (list.isEmpty()) {
            return Double.NaN;
        }

        Attribute att = (Attribute) list.get(0);
        Double value = (Double) att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0)
            return Double.NaN; // returns a percentage value with 1 decimal point precision
        return ((int) (value * 100000) / 1000.0);

    }

    public synchronized boolean tasksRemaining() {
    	return getProcessedTasks() > 0;
    }

    private int getProcessedTasks() {
    	return tasksToProcess;
    }

    public static synchronized void decreaseCountCpu() {
    	tasksToProcess--;
    }

    public static synchronized void increaseCountCpu() {
        processedTasks++;
    }

    public void getLostTransactions() throws IOException {
        System.out.println("Lost Transactions: "+ (C.transactions-processedTasks));
        logLost("Lost Transactions: "+ (C.transactions-processedTasks));
        System.exit(0);
    }

}
