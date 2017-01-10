package com.example.myshoppinglist.myshoppinglist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.example.myshoppinglist.myshoppinglist.R;

/**
 * Created by ameliebarre1 on 09/01/2017.
 */

public class EditListActivity extends AppCompatActivity {

    private EditText listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_list);

        Intent intent = getIntent();
        String getListName = intent.getStringExtra("LIST_NAME");

        listName = (EditText) findViewById(R.id.updateListField);
        listName.setText(getListName);

        Log.d("list_name : ", getListName);
    }
}
