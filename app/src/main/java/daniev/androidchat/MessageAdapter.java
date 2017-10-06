package daniev.androidchat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * Class responsible for providing content to the recycle viewer
 */
public class MessageAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_SYSTEM_MESSAGE = 2;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 3;
    private Context myContext;
    private List<Message> myMessageList;

    MessageAdapter(Context context, List<Message> messageList){
        myContext = context;
        myMessageList = messageList;
    }

    @Override
    public int getItemCount(){
        return myMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = myMessageList.get(position);
        if (message.getAuthor().equals(ClientController.getUser())) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else if(message.getAuthor().equals("System")){
            return VIEW_TYPE_SYSTEM_MESSAGE;
        } else{
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(myContext)
                    .inflate(R.layout.message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(myContext)
                    .inflate(R.layout.message_received, parent, false);
            return new ReceivedMessageHolder(view);
        } else if(viewType== VIEW_TYPE_SYSTEM_MESSAGE){
            view = LayoutInflater.from(myContext)
                    .inflate(R.layout.system_message, parent, false);
            return new SystemMessageHolder(view);
        }
        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        Message message = myMessageList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_SYSTEM_MESSAGE:
                ((SystemMessageHolder)holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder{
        TextView messageText, timeText;
        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }
        void bind(Message message) {
            messageText.setText(message.getMsg());
            timeText.setText(message.getTime());
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder{
        TextView messageText, timeText, nameText;
        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            nameText = itemView.findViewById(R.id.text_message_name);
        }
        void bind(Message message) {
            messageText.setText(message.getMsg());
            timeText.setText(message.getTime());
            nameText.setText(message.getAuthor());
        }
    }

    private class SystemMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        SystemMessageHolder(View itemView){
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            nameText = itemView.findViewById(R.id.text_message_name);
        }
        void bind(Message message) {
            messageText.setText(message.getMsg());
            timeText.setText(message.getTime());
            nameText.setText(message.getAuthor());
        }
    }
}