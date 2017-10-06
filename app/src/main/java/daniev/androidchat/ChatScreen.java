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

/**
 * Main activity of the application, shows and sends messages, has actions available from toolbar
 */

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

        //appbar replaced with toolbar
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
                        } else if(item.getItemId() == R.id.menu_quit){
                            controller.getSender().addMessage(":quit");
                            controller.disconnect();
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                            return true;
                    }
                });
                popup.show();
            }
        });
        // Set up recycler view
        myMessageAdapter = new MessageAdapter(this, messageList);
        myMessageRecycler.setAdapter(myMessageAdapter);
        myMessageRecycler.setLayoutManager(new LinearLayoutManager(this));

        //if we havent assigned a username then we haven't logged in to server, calling activity to do it
        if(ClientController.getUser()==null){
            callLoginActivity();
        }else{
            Thread controllerThread = new Thread(controller);
            controllerThread.start();
        }
        //after logging in we will have a channel name, so we set it to the action bar
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setTitle("Channel: " + channelName);
        }
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = chatBox.getText().toString();
                if(!msg.isEmpty()) {
                    controller.getSender().addMessage(msg);
                    chatBox.getText().clear();
                }
            }
        });
    }//close onCreate

    public void setChannelName(String str){
        channelName = str;
    }
    //when first time logging in
    public void callLoginActivity(){
        Intent myIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(myIntent,LOGIN_ORDER);
    }
    //upon failing to log in correctly
    public void callLoginActivity(String reason){
        Intent myIntent = new Intent(this, LoginActivity.class);
        myIntent.putExtra("Reason",reason);
        startActivityForResult(myIntent,LOGIN_ORDER);
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
