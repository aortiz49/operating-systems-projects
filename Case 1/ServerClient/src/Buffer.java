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
 * Represents a Buffer that holds messages from the Client to be processed by the Server.
 *
 */
public class Buffer extends Thread {
    // ===============================================
    // Attributes
    // ===============================================
    /**
     * The max size of the buffer.
     */
    private int maxBufferSize;

    /**
     * The current size of the buffer.
     */
    private int bufferSize;

    /**
     * List of messages to be processed by the server.
     */
    private ArrayList<Message> messages;

    // ===============================================
    // Constructor
    // ===============================================

    /**
     * Constructs a new buffer with a given size.
     *
     * @param pMaxBufferSize the buffer size
     */
    public Buffer(int pMaxBufferSize){
        maxBufferSize = pMaxBufferSize;
    }

    // ===============================================
    // Getters & Setters
    // ===============================================


    // ===============================================
    // Methods
    // ===============================================

    /**
     * Adds a message to the buffer.
     *
     * @param pMessage the message
     */
    public void addMessageToBuffer(Message pMessage){

        if(bufferSize == maxBufferSize){

        }
        messages.add(pMessage);
    }



}