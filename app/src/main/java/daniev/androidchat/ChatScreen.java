package daniev.androidchat;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;


public class ChatScreen extends AppCompatActivity {

    private RecyclerView myMessageRecycler;
    private MessageAdapter myMessageAdapter;
    private ArrayList<Message> messageList = new ArrayList<>();
    private ClientController controller;
    private EditText chatBox;
    private String channelName ="";
    private final int LOGIN_ORDER = 66;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        chatBox = findViewById(R.id.edittext_chatbox);
        Button sendButton = findViewById(R.id.button_chatbox_send);
        myMessageRecycler = findViewById(R.id.recyclerview_message_list);
        final Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        final ImageButton menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(ChatScreen.this,menuButton);
                popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_userlist) {
                            controller.getSender().addMessage(":userlist");
                        } else if (item.getItemId() == R.id.menu_chat_history) {
                            controller.getSender().addMessage(":messages");
                        } else if (item.getItemId() == R.id.menu_channels) {
                            controller.getSender().addMessage(":channellist");
                        } else if(item.getItemId() == R.id.menu_quit){};
                            return true;
                    }
                });
                popup.show();
            }
        });

        myMessageAdapter = new MessageAdapter(this, messageList);
        myMessageRecycler.setAdapter(myMessageAdapter);
        myMessageRecycler.setLayoutManager(new LinearLayoutManager(this));



        if(controller.getUser()==null){
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(myIntent,LOGIN_ORDER);
        }else{
            Thread controllerThread = new Thread(controller);
            controllerThread.start();
        }

        myToolbar.setTitle("Channel: "+channelName);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = chatBox.getText().toString();
                if(msg!=null&&!msg.isEmpty()) {
                    controller.getSender().addMessage(msg);
                    chatBox.getText().clear();
                }
            }
        });
    }//close onCreate

    public void setChannelName(String str){
        channelName = str;
    }


        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_ORDER) {
            if (resultCode == Activity.RESULT_OK) {
                String[] credentials = data.getStringArrayExtra("login");
                controller = new ClientController(this,credentials[0],credentials[1],credentials[2]);
                Thread controllerThread = new Thread(controller);
                controllerThread.start();
            }
        }
    }

    public void addMessage(final Message msg){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(msg);
                myMessageAdapter.notifyDataSetChanged();
                myMessageRecycler.scrollToPosition(messageList.size()-1);
            }
        });
    }
}
