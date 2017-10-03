package daniev.androidchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;


public class ChatScreen extends AppCompatActivity {

    private RecyclerView myMessageRecycler;
    private MessageAdapter myMessageAdapter;
    private ArrayList<Message> messageList = new ArrayList<>();
    private ClientController controller = new ClientController(this);
    private EditText chatBox;
    private Button sendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        chatBox = findViewById(R.id.edittext_chatbox);
        sendButton = findViewById(R.id.button_chatbox_send);
        myMessageRecycler = findViewById(R.id.recyclerview_message_list);
        myMessageAdapter = new MessageAdapter(this, messageList);
        myMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        myMessageRecycler.setAdapter(myMessageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = chatBox.getText().toString();
                controller.getSender().sendMSG(msg);
                chatBox.getText().clear();
            }
        });

        Thread controllerThread = new Thread(controller);
        controllerThread.start();
    }

    public void addMessage(Message msg){
        messageList.add(msg);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myMessageAdapter.notifyDataSetChanged();
            }
        });
    }
}
