package daniev.androidchat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by danie on 3.10.2017.
 */

public class ClientController implements Runnable{
    private static String user;
    private Socket clientSocket;
    private ChatScreen cs;
    private String IPaddress = "192.168.0.101";
    private Integer port = 54860;
    private Sender sender;
    private Updater updater;
    private boolean haveWeTried = false;

    ClientController(ChatScreen chatScreen){
        cs = chatScreen;
    }
    @Override
    public void run() {
        if(!haveWeTried) {
            try {
                clientSocket = new Socket(this.IPaddress, this.port);
            } catch (IOException io) {
                io.printStackTrace();
            }

            try {
                updater = new Updater(clientSocket.getInputStream(), cs);
                sender = new Sender(new PrintStream(clientSocket.getOutputStream(), true),cs);
            } catch (IOException io) {
                io.printStackTrace();
            }

            Thread senderThread = new Thread(sender);
            Thread updaterThread = new Thread(updater);

            senderThread.start();
            updaterThread.start();

            haveWeTried = true;

            sender.sendMSG(":user Daniel");
        }
    }
    public static String getUser(){return user;}

    public Sender getSender(){return sender;}
}
