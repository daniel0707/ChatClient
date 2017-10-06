package daniev.androidchat;

import java.io.PrintStream;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Class responsible for printing to outputsream of the socket we are using
 */

public class Sender implements Runnable {
    private ArrayBlockingQueue<String> messageQueue;
    private PrintStream output;

    Sender(PrintStream outputStream) {
        this.output = outputStream;
        this.messageQueue = new ArrayBlockingQueue<>(10);
    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            try {
                String newestMessage = this.messageQueue.take();
                sendMSG(newestMessage);
            } catch (InterruptedException i) {
                i.printStackTrace();
            }
        }
    }

    void addMessage(String msg) {
        this.messageQueue.add(msg);
    }

    void sendMSG(String msg) {
        output.println(msg);
    }
}
