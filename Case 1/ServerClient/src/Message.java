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
 * Represents a message sent by the client.
 *
 */
public class Message {
    // ===============================================
    // Attributes
    // ===============================================

    /**
     * Indicates if the message is incoming or outgoing(from the point of view of the server.
     */
    private boolean isIncoming;

    /**
     * The body of the message.
     */
    private int message;
    // ===============================================
    // Getters & Setters
    // ===============================================

    public boolean isIncoming() {
        return isIncoming;
    }

    public void setIncoming(boolean incoming) {
        isIncoming = incoming;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    // ===============================================
    // Constructor
    // ===============================================

    /**
     * Constructs a new message with a given message text.
     *
     * @param pMessage the body of the new message
     * @param pIsIncoming indicates if the message is incoming
     */
    public Message(int pMessage, boolean pIsIncoming) {
        message = pMessage;
        isIncoming = true;
    }


}