package daniev.androidchat;

import android.util.Log;

import java.io.PrintStream;
import java.util.concurrent.ArrayBlockingQueue;

import static android.content.ContentValues.TAG;

/**
 * Created by danie on 3.10.2017.
 */

public class Sender implements Runnable {

    private ArrayBlockingQueue<String> messageQueue;
    private PrintStream output;
    private ChatScreen chatScreen;

    public Sender(PrintStream outputStream, ChatScreen cs) {
        this.output = outputStream;
        this.messageQueue = new ArrayBlockingQueue<>(10);
        chatScreen = cs;
    }

    @Override
    public void run() {
        while(true) {

            try {
                String newestMessage = this.messageQueue.take();
                sendMSG(newestMessage);

            } catch (InterruptedException i) {
                Log.d(TAG, "ERROR WHILE ACCESSING MESSAGE BLOCKING QUEUE");
                i.printStackTrace();
            }
        }
    }

    public void addMessage(String msg) {
        this.messageQueue.add(msg);
    }

    public void sendMSG(String msg) {
        String message = msg;
        output.println(message);
    }
}
