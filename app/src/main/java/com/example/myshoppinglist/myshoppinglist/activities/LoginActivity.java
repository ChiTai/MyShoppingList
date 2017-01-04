package com.example.myshoppinglist.myshoppinglist.activities;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.myshoppinglist.myshoppinglist.R;
import com.example.myshoppinglist.myshoppinglist.others.HttpUtility;
import com.example.myshoppinglist.myshoppinglist.others.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by ameliebarre1 on 07/12/16.
 */
public class LoginActivity extends AppCompatActivity {

    private TextView register;
    private EditText userEmail;
    private EditText userPassword;
    private Button loginButton;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        session = new SessionManager(getApplicationContext());

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        userEmail = (EditText) findViewById(R.id.loginEmail);
        userPassword = (EditText) findViewById(R.id.loginPassword);
        register = (TextView) findViewById(R.id.register_link);
        loginButton = (Button) findViewById(R.id.btnLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String loginEmail = userEmail.getText().toString();
                final String loginPassword = userPassword.getText().toString();

                if(loginEmail.trim().length() > 0 && loginPassword.trim().length() > 0) {
                    new AsyncLogin().execute(loginEmail, loginPassword);
                } else {
                    Toast.makeText(getApplicationContext(), "Error when logging in...", Toast.LENGTH_LONG).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(startRegister);
            }
        });
    }

    public class AsyncLogin extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(LoginActivity.this, R.style.MyCustomAlertDialog);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = "http://appspaces.fr/esgi/shopping_list/account/login.php?email=" + params[0] + "&password=" + params[1] + "";
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

            final String logPreference = "preferences";

            SharedPreferences sharedPreferences = getSharedPreferences(logPreference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // If progressDialog is showing, we dismiss it
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            super.onPostExecute(httpResponseMsg);

            try {
                JSONObject jsonData = new JSONObject(httpResponseMsg);
                JSONObject resultMessage = jsonData.getJSONObject("result");

                // Get the result code
                String resultCode = jsonData.getString("code");

                if(resultCode.contentEquals("0")) {
                    finish();

                    String token = resultMessage.getString("token");
                    String email = resultMessage.getString("email");

                    session.createLoginSession(email, token);

                    // And then put the token inside a sharedPreferences
                    editor.putString("token", token);
                    editor.putBoolean("isConnected", true);
                    editor.commit();

                    // If everything OK, redirect to the Home Activity (user logged successful)
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } else if(resultCode.contentEquals("3")) { // If user credentials are not recognised
                    Toast.makeText(LoginActivity.this, "Login failed, please try again or check your credentials.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
