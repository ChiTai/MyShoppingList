package com.example.myshoppinglist.myshoppinglist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myshoppinglist.myshoppinglist.R;

/**
 * Created by ameliebarre1 on 30/12/2016.
 */

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString(Intent.EXTRA_TEXT);
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        welcomeMessage.append(username);
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
            case R.id.view_lists:
                Toast.makeText(this, "View my lists", Toast.LENGTH_SHORT).show();
                break;

            case R.id.create_list:
                Toast.makeText(this, "Create a list", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        return true;
    }

}
