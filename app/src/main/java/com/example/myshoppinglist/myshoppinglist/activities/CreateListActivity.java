package com.example.myshoppinglist.myshoppinglist.activities;

import com.example.myshoppinglist.myshoppinglist.R;
import com.example.myshoppinglist.myshoppinglist.others.HttpUtility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ameliebarre1 on 12/12/2016.
 */

public class CreateListActivity extends AppCompatActivity {

    private EditText listName;
    private Button createListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_list);

        listName = (EditText) findViewById(R.id.myShoppingList);
        createListButton = (Button) findViewById(R.id.createList);

        createListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String myList = listName.getText().toString();

                // Get the token from the sharedPreferences in LoginActivity
                final String logPreference = "preferences";
                final SharedPreferences sharedPreferences = getSharedPreferences(logPreference, Context.MODE_PRIVATE);
                final String token = sharedPreferences.getString("token", "");

                new AsyncShoppingList().execute(token, myList);
            }
        });
    }

    public class AsyncShoppingList extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CreateListActivity.this, R.style.MyCustomAlertDialog);
            pDialog.setMessage("Creating shopping list...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = "http://appspaces.fr/esgi/shopping_list/shopping_list/create.php?token=" + params[0] + "&name=" + params[1] + "";
                HttpUtility.sendGetRequest(url);

                int responseCode = HttpUtility.getResponseCode();

                if(responseCode == 200) {
                    String br = HttpUtility.readMultipleLines();
                    return br;
                } else {
                    return "false : " + responseCode;
                }
            } catch(Exception e) {
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String httpResponseMsg) {

            // If progressDialog is showing, we dismiss it
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            super.onPostExecute(httpResponseMsg);

            try {
                JSONObject jsonData = new JSONObject(httpResponseMsg);

                // Get the result code
                String resultCode = jsonData.getString("code");

                if (resultCode.contentEquals("0")) {
                    finish();

                    Intent intent = new Intent(CreateListActivity.this, MainActivity.class);
                    startActivity(intent);

                } else if (resultCode.contentEquals("1")) { // If user credentials are not recognised
                    Toast.makeText(CreateListActivity.this, "Missing required parameters.", Toast.LENGTH_LONG).show();
                } else if (resultCode.contentEquals("4")) {
                    Toast.makeText(CreateListActivity.this, "Wrong token.", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(CreateListActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
