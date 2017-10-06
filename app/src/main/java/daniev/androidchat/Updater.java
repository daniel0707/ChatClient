package daniev.androidchat;

import java.io.InputStream;
import java.util.Scanner;
/**
 * Class responsible for receiving updates from input stream
 */

public class Updater implements Runnable{
    private Scanner reader;
    private ChatScreen chatScreen;

    Updater(InputStream connection, ChatScreen cs) {
        this.reader = new Scanner(connection);
        this.chatScreen = cs;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()){
            if(reader.hasNext()){
                String raw = reader.nextLine();
                if(raw!=null&& !raw.isEmpty()) {
                    System.out.print(raw);
                    //expected format: [0]time, [1]username, [2]msg
                    String[] split = raw.split("\\s", 3);
                    System.out.print(split[1]);
                    filterMsg(split);
                    Message msg = new Message(split[0], split[1], split[2]);
                    addMsg(msg);
                }
            }else{System.out.println("system DID nOT HAVE NEXT");}
        }
    }
    /**
     * Filter msg for system calls
     */
    private void filterMsg(String[] strings){
        if (strings[1].equals("System")){
            if(strings[2].startsWith("Username")){
                chatScreen.callLoginActivity("Username taken");
            }
            if(strings[2].startsWith("Switched to ")) {
                final String[] tempHolder = strings[2].split("\\s");
                chatScreen.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatScreen.setChannelName(tempHolder[2]);
                    }
                });
            }
            if(strings[2].startsWith("User ")){
                String[] temp = strings[2].split("\\s");
                ClientController.setUser(temp[1]);
            }
        }
    }
    private void addMsg(final Message msg){
        chatScreen.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatScreen.addMessage(msg);
            }
        });
    }
}
