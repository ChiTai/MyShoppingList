package com.example.myshoppinglist.myshoppinglist.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        btnLogout = (Button) findViewById(R.id.logout);

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        Toast.makeText(getApplicationContext(), "Email : " + user.get(SessionManager.KEY_EMAIL) + " Token : " + user.get(SessionManager.KEY_TOKEN), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewLists:
                Toast.makeText(this, "View lists", Toast.LENGTH_SHORT).show();
                break;

            case R.id.createList:
                Intent createList = new Intent(MainActivity.this, CreateListActivity.class);
                startActivity(createList);
                break;

            case R.id.logout:
                session.logoutUser();

            default:
                break;
        }
        return true;
    }
}
