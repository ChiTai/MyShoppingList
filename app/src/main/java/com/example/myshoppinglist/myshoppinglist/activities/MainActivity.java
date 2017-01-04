package com.example.myshoppinglist.myshoppinglist.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myshoppinglist.myshoppinglist.R;
import com.example.myshoppinglist.myshoppinglist.others.SessionManager;

import org.w3c.dom.Text;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SessionManager session;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        // Get the user data from session
        HashMap<String, String> user = session.getUserDetails();

        TextView email = (TextView) findViewById(R.id.lblEmail);
        TextView token = (TextView) findViewById(R.id.lblToken);

        btnLogout = (Button) findViewById(R.id.logout);

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        // Displaying user data
        email.setText(user.get("Email : " + SessionManager.KEY_EMAIL));
        token.setText(user.get("Token : " + SessionManager.KEY_TOKEN));

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                session.logoutUser();
            }
        });
    }
}
