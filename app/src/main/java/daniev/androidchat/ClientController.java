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
    private String IPaddress;
    private Integer port;
    private Sender sender;
    private Updater updater;
    private boolean haveWeTried = false;

    ClientController(ChatScreen chatScreen, String username, String ip, String portSTR){
        this.user = username;
        this.IPaddress=ip;
        this.port=Integer.parseInt(portSTR);
        this.cs = chatScreen;
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

            sender.sendMSG(":user "+user);
        }
    }

    public static String getUser(){return user;}
    public static void setUser(String str){user = str; }
    public Sender getSender(){return sender;}
}
