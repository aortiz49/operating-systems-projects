/*
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

import java.util.ArrayList;

/**
 * Represents a Client that creates a message request to a Server.
 *
 * @author a.ortizg@uniandes.edu.co
 */
public class Client extends Thread {
    // ===============================================
    // Attributes
    // ===============================================

    /**
     * The client's name.
     */
    private String clientName;

    /**
     * The number of message requests to send to the server.
     */
    private int messageRequests;

    /**
     * The buffer that will be accessed.
     */
    private static Buffer buffer;

    /**
     * Counter to keep track of received messages from the server.
     */
    private int messageCounter;

    // ===============================================
    // Constructor
    // ===============================================

    /**
     * Constructs a new client with a  given name and message requests.
     *
     * @param pMessageRequests the number of message requests
     * @param pClientName      the client's name
     * @param pBuffer          the shared message buffer
     */
    Client(String pClientName, int pMessageRequests, Buffer pBuffer) {
        clientName = pClientName;
        messageRequests = pMessageRequests;
        buffer = pBuffer;
    }

    // ===============================================
    // Methods
    // ===============================================

    /**
     * Creates a message to be sent to the buffer.
     */
    private Message createMessageRequest() {
        return new Message((int) (Math.random() * 100), true);
    }


    /**
     * Starts the thread execution process.
     */
    public void run() {

        while (messageCounter > 0) {
            // creates a message
            Message message = createMessageRequest();

            while(buffer.)
        }
    }


}