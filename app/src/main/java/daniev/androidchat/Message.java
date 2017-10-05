package daniev.androidchat;

/**
 * Created by danie on 3.10.2017.
 */

public class Message {
    private String time;
    private String msg;
    private String author;

    Message(String time,String user, String msg){
        this.time=time;
        this.msg=msg;
        this.author=user;
    }

    public String getTime(){return time;}
    public String getMsg(){return msg;}
    public String getAuthor(){return author;}
}
