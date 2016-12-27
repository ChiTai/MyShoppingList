package com.example.myshoppinglist.myshoppinglist.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myshoppinglist.myshoppinglist.R;
import com.example.myshoppinglist.myshoppinglist.others.HttpUtility;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private EditText name;
    private EditText firstname;
    private EditText email;
    private EditText password;
    private TextView login;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe);

        name = (EditText) findViewById(R.id.registerName);
        firstname = (EditText) findViewById(R.id.registerFirstName);
        email = (EditText) findViewById(R.id.registerEmail);
        password = (EditText) findViewById(R.id.registerPassword);
        login = (TextView) findViewById(R.id.login_link);
        registerButton = (Button) findViewById(R.id.btnRegister);

        // Hide the ActionBar
        //getSupportActionBar().hide();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String registerName = name.getText().toString();
                final String registerFirstName = firstname.getText().toString();
                final String registerEmail = email.getText().toString();
                final String registerPassword = password.getText().toString();

                new AsyncRegister().execute(registerEmail, registerPassword, registerFirstName, registerName);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(startLogin);
            }
        });
    }

    private class AsyncRegister extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String url = "http://appspaces.fr/esgi/shopping_list/account/subscribe.php?email=" + params[0] + "&password=" + params[1] + "&firstname=" + params[2] + "&lastname=" + params[3] + "";

                HttpUtility.sendGetRequest(url);

                int responseCode = HttpUtility.getResponseCode();
                String reponseCode1 = String.valueOf(responseCode);

                Log.d("responseCode : ", reponseCode1);

                if(responseCode == 200) {
                    String br = HttpUtility.readMultipleLines();
                    return br;
                } else {
                    return new String("false : " + responseCode);
                }
            }
            catch(Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }
}
