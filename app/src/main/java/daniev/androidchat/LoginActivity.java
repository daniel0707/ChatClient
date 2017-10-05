package daniev.androidchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    EditText usernameText;
    EditText IPAddressText;
    EditText portText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.buttonLogIn);
        usernameText = findViewById(R.id.usernameText);
        IPAddressText = findViewById(R.id.IPText);
        portText = findViewById(R.id.portNumberText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] credentials = {usernameText.getText().toString(),
                        IPAddressText.getText().toString(),portText.getText().toString()};
                Intent res = new Intent();
                res.putExtra("login", credentials);
                setResult(RESULT_OK,res);
                finish();
            }
        });
    }
}
