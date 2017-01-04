package com.example.myshoppinglist.myshoppinglist.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myshoppinglist.myshoppinglist.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ameliebarre1 on 30/12/2016.
 */

public class HomeActivity extends AppCompatActivity {

    private ListView lv;
    ArrayList<HashMap<String, String>> shoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        shoppingList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        Intent intent = getIntent();
        String listName = intent.getStringExtra("EXTRA_LIST_NAME");

        HashMap<String, String> list = new HashMap<>();

        list.put("name", listName);

        // adding contact to contact list
        shoppingList.add(list);

        ListAdapter adapter = new SimpleAdapter(HomeActivity.this, shoppingList, R.layout.list_item, new String[]{"name"}, new int[]{R.id.listName});
        lv.setAdapter(adapter);

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
                Intent createList = new Intent(HomeActivity.this, CreateListActivity.class);
                startActivity(createList);
                break;

            default:
                break;
        }
        return true;
    }

}
