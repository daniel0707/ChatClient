package daniev.androidchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity class responsible for taking login credentials and relaying to main activity
 */
public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    EditText usernameText;
    EditText IPAddressText;
    EditText portText;
    String errorPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.buttonLogIn);
        usernameText = findViewById(R.id.usernameText);
        IPAddressText = findViewById(R.id.IPText);
        portText = findViewById(R.id.portNumberText);
        Bundle extras = getIntent().getExtras();

        //if there are extras, an error occurred on last login attempt
        if(extras != null){
            errorPopup = extras.getString("Reason");
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, errorPopup, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL,0,500);
            TextView v = toast.getView().findViewById(android.R.id.message);
            v.setTextColor(Color.RED);
            toast.show();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usernameText.getText().toString().equals("")) {
                    String errorMsg = "Username cannot be empty";
                    Toast toast = Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL,0,500);
                    TextView v = toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(Color.RED);
                    toast.show();
                }else if(IPAddressText.getText().toString().equals("")){
                    String errorMsg = "IP cannot be empty";
                    Toast toast = Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL,0,500);
                    TextView v = toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(Color.RED);
                    toast.show();
                }else if(portText.getText().toString().equals("")){
                    String errorMsg = "Port cannot be empty";
                    Toast toast = Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL,0,500);
                    TextView v = toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(Color.RED);
                    toast.show();
                }else {
                    String[] credentials = {usernameText.getText().toString(),
                            IPAddressText.getText().toString(), portText.getText().toString()};
                    Intent res = new Intent();
                    res.putExtra("login", credentials);
                    setResult(RESULT_OK, res);
                    finish();
                }
            }
        });
    }
}
