package communication_protocol.server;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class C {
    private static ServerSocket ss;
    private static final String MAESTRO = "MASTER: ";
    private static X509Certificate certSer; /* acceso default */
    private static KeyPair keyPairServidor; /* acceso default */

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {


        System.out.println(MAESTRO + "Establish connection port:");
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        //int ip = Integer.parseInt(br.readLine());
        System.out.println(MAESTRO + "Starting master server on port " + 5454);

        // Adiciona la libreria como un proveedor de seguridad.
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        System.out.println("Enter thread pool size:");
        int poolsize = Integer.parseInt(br.readLine());
        Executor threadpool = Executors.newFixedThreadPool(poolsize);
        System.out.println("Thread pool created with size: " + poolsize);

        // Crea el archivo de log
        File logFile = null;

        // Crea el archivo de tiempos
        File timeFile = null;

        // Crea el archivo de cpu
        File cpuFile = null;

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

        cpuFile = new File(cpu);
        if (!cpuFile.exists()) {
            cpuFile.createNewFile();
        }
        fw = new FileWriter(cpuFile);
        fw.close();







        D.init(certSer, keyPairServidor, logFile,timeFile,cpuFile);

        // Crea el socket que escucha en el puerto seleccionado.
        ss = new ServerSocket(5454);
        System.out.println(MAESTRO + "Socket creado.");


        for (int i = 0; true; i++) {
            try {
                Socket sc = ss.accept();
                System.out.println(MAESTRO + "Client " + i + " was accepted.");

                D d = new D(sc, i);
                threadpool.execute(d);

            }
            catch (IOException e) {
                System.out.println(MAESTRO + "Error creating client socket.");
                e.printStackTrace();
            }
        }

    }

}
