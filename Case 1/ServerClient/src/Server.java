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

/**
 * Represents a Server that responds to a Client request.
 */
public class Server extends Thread {
    // ===============================================
    // Attributes
    // ===============================================

    /**
     * The server's name
     */
    private String serverName;

    /**
     * The buffer that will be accessed.
     */
    private static Buffer buffer;

    // ===============================================
    // Constructor
    // ===============================================

    /**
     * Constructs a new server with a  given name and buffer.
     *
     * @param pServerName the server's name
     * @param pBuffer     the new buffer
     */
    public Server(String pServerName, Buffer pBuffer) {
        serverName = pServerName;
        buffer = pBuffer;
    }

    // ===============================================
    // Methods
    // ===============================================

    /**
     * Receives the message from the client, and prepares the response.
     *
     * @param pMessage the client's message
     * @param pName    the client's name
     * @return the processed response
     */
    public Message attendToRequest(String pName, Message pMessage) {

        int message = pMessage.getMessage();
        pMessage.setMessage(++message);
       //HEY pMessage.getClient().addMessage(pMessage);
        System.out.println("Attended message from: " + pName);

        return pMessage;
    }


}