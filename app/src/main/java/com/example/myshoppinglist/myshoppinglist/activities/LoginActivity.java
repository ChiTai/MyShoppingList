package com.example.myshoppinglist.myshoppinglist.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.myshoppinglist.myshoppinglist.R;

/**
 * Created by ameliebarre1 on 07/12/16.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        TextView register = (TextView) findViewById(R.id.register_link);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(startRegister);
            }
        });
    }
}
