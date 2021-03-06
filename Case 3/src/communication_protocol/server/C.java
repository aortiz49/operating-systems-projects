package communication_protocol.server;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.concurrent.*;


public class C {
    private static ServerSocket ss;
    private static final String MAESTRO = "MASTER: ";
    private static X509Certificate certSer; /* acceso default */
    private static KeyPair keyPairServidor; /* acceso default */
    private static CpuMonitor cpuMonitor;
    public static ExecutorService threadpool;
    public static int transactions;

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        //int ip = Integer.parseInt(br.readLine());
        System.out.println(MAESTRO + "Starting master server on port " + 8080);

        // Adiciona la libreria como un proveedor de seguridad.
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        System.out.println("Enter thread pool size:");
        int poolsize = Integer.parseInt(br.readLine());
        threadpool = Executors.newFixedThreadPool(poolsize);
        System.out.println("Thread pool created with size: " + poolsize);

        // Crea el archivo de log
        File logFile = null;

        // Crea el archivo de tiempos
        File timeFile = null;

        keyPairServidor = S.grsa();
        certSer = S.gc(keyPairServidor);
        String ruta = "./resultados.txt";
        String times = "./times.txt";
        String cpu = "./cpu.txt";

        logFile = new File(ruta);
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        FileWriter fw = new FileWriter(logFile);
        fw.close();

        timeFile = new File(times);
        if (!timeFile.exists()) {
            timeFile.createNewFile();
        }
        fw = new FileWriter(timeFile);

        fw.close();

        // D.init(certSer, keyPairServidor, logFile, timeFile);
        D_NoSecurity.init(certSer, keyPairServidor, logFile, timeFile);


        // Crea el socket que escucha en el puerto seleccionado.
        ss = new ServerSocket(80, 50, InetAddress.getByName("172.24.41.39"));
        System.out.println(MAESTRO + "Socket creado.");

        System.out.println("Enter the number of transactions to perform: ");
        transactions = Integer.parseInt(br.readLine());

        cpuMonitor = new CpuMonitor(transactions);

        for (int i = 0; i < transactions; i++) {
            try {

                Socket sc = ss.accept();
                if (i == 0) {
                    cpuMonitor.start();
                }

                System.out.println(MAESTRO + "Client " + i + " was accepted.");

                //D d = new D(sc, i);
                D_NoSecurity d = new D_NoSecurity(sc,i);
                threadpool.execute(d);


            } catch (IOException e) {
                System.out.println(MAESTRO + "Error creating client socket.");
                e.printStackTrace();
                CpuMonitor.decreaseCountCpu();
            }
        }

        //awaitTerminationAfterShutdown(threadpool, transactions);
    }

    public static void awaitTerminationAfterShutdown(ExecutorService threadpool, int transactions)
            throws IOException {
        threadpool.shutdown();
        //if (!threadpool.isTerminated()) {
        //threadpool.awaitTermination(200, TimeUnit.SECONDS);
        while (!threadpool.isTerminated()) {
            //Thread.sleep(1000);
        }
        //}
        System.out.println("Clients have stopped");
        cpuMonitor.interrupt();


        /*BufferedReader reader = new BufferedReader(new FileReader("times.txt"));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();

        System.out.println("Lost: "+ (transactions-lines));
        System.exit(0);*/

    }

}
