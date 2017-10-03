package daniev.androidchat;

import java.io.InputStream;
import java.util.Scanner;
/**
 * Created by danie on 3.10.2017.
 */

public class Updater implements Runnable{

    private Scanner reader;
    private ChatScreen chatScreen;

    public Updater(InputStream connection, ChatScreen cs) {
        this.reader = new Scanner(connection);
        this.chatScreen = cs;
    }

    @Override
    public void run() {
        while(true){
            String raw = reader.nextLine();
            if(raw!=null&& !raw.isEmpty()) {
                String[] split = raw.split("\\s", 3);
                for (String str: split
                     ) {
                    System.out.println(str);
                }
                Message msg = new Message(split[0], split[1], split[2]);
                chatScreen.addMessage(msg);
            }
        }
    }
}
