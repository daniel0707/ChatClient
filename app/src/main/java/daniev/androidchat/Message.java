package daniev.androidchat;

/**
 * Template for message objects
 */
class Message {
    private String time;
    private String msg;
    private String author;

    Message(String time,String user, String msg){
        this.time=time;
        this.msg=msg;
        this.author=user;
    }

    String getTime(){return time;}
    String getMsg(){return msg;}
    String getAuthor(){return author;}
}
