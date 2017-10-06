package daniev.androidchat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Class responsible for network connection and creation of I/O handling classes
 */

public class ClientController implements Runnable{
    private static String user;
    private Socket clientSocket;
    private ChatScreen cs;
    private String IPaddress;
    private Integer port;
    private Sender sender;
    private Updater updater;
    private Thread senderThread;
    private Thread updaterThread;
    private boolean haveWeTried = false;

    ClientController(ChatScreen chatScreen, String username, String ip, String portSTR){
        user = username;
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
                cs.callLoginActivity("Wrong IP and/or port");
            }
            if(clientSocket !=null) {
                try {
                    updater = new Updater(clientSocket.getInputStream(), cs);
                    sender = new Sender(new PrintStream(clientSocket.getOutputStream(), true));
                } catch (IOException io) {
                    cs.callLoginActivity("Network error, please try again later");
                }
            }
            senderThread = new Thread(sender);
            updaterThread = new Thread(updater);

            senderThread.start();
            updaterThread.start();

            haveWeTried = true;
            //tell server what username we chose
            sender.sendMSG(":user "+user);
        }
    }

    static String getUser(){return user;}
    static void setUser(String str){user = str; }
    Sender getSender(){return sender;}

    //disconnect from application side
    void disconnect(){
        senderThread.interrupt();
        updaterThread.interrupt();
    }
}
