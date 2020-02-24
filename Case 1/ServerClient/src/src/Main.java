package src;/*
MIT License

Copyright (c) 2017 Universidad de los Andes - ISIS2203

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Represents the main operation of the application.
 *
 * @author a.ortizg@uniandes.edu.co
 * @author lm.sierra20@uniandes.edu.co
 */
public class Main extends Thread {

    /**
     * The number of servers to initialize the app
     */
    private static int servers;
    private static int clients;
    private static int message_requests;
    private static int buffer_size;

    private static ArrayList<Server> serverList = new ArrayList<>();
    private static ArrayList<Client> clientList = new ArrayList<>();

    private static Buffer buffer;

    public static void readFile() {
        try {
            FileInputStream inputStream = new FileInputStream("data/config.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String reader;
            reader = bufferedReader.readLine();
            StringBuilder strings = new StringBuilder();
            while ((reader = bufferedReader.readLine()) != null) {
                strings.append(Integer.parseInt(reader.split("=")[1]) + "#");
            }

            String values = strings.toString();
            clients = Integer.parseInt(values.split("#")[0]);
            servers = Integer.parseInt(values.split("#")[1]);
            message_requests = Integer.parseInt(values.split("#")[2]);
            buffer_size = Integer.parseInt(values.split("#")[3]);

            buffer = new Buffer(buffer_size);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates server objects.
     *
     * @param pNumberOfServers the number of server objects to create
     */
    public static void createServers(int pNumberOfServers) {
        for (int i = 0; i < pNumberOfServers; i++) {
            serverList.add(new Server("src.Server" + i, buffer));
        }

    }

    /**
     * Creates client objects.
     *
     * @param pNumberOfClients the number of client objects to create
     */
    public static void createClients(int pNumberOfClients) {
        for (int i = 0; i < pNumberOfClients; i++) {
            clientList.add(new Client("Client" + i, message_requests, buffer));
        }
    }

    public static void main(String[] args) {

        // loads configuration file
        readFile();

        // creates the client and server instances
        createClients(clients);
        createServers(servers);

        // starts the ith client thread
        for (Client client : clientList) {
            client.start();
        }

        // starts the ith server thread
        for (Server server : serverList) {
            server.start();
        }
    }
}