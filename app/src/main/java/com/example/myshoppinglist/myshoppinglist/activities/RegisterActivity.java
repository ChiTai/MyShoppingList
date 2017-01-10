package com.example.myshoppinglist.myshoppinglist.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myshoppinglist.myshoppinglist.R;
import com.example.myshoppinglist.myshoppinglist.models.ResultCode;
import com.example.myshoppinglist.myshoppinglist.models.User;
import com.example.myshoppinglist.myshoppinglist.others.HttpUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ameliebarre1 on 10/12/16.
 */
public class RegisterActivity extends AppCompatActivity {

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
                Intent startLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(startLogin);
            }
        });
    }

    private class AsyncRegister extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(RegisterActivity.this, R.style.MyCustomAlertDialog);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = "http://appspaces.fr/esgi/shopping_list/account/subscribe.php?email=" + params[0] + "&password=" + params[1] + "&firstname=" + params[2] + "&lastname=" + params[3] + "";

                HttpUtility.sendGetRequest(url);

                int responseCode = HttpUtility.getResponseCode();

                if(responseCode == 200) {
                    String br = HttpUtility.readMultipleLines();
                    return br;
                } else {
                    return "false : " + responseCode;
                }
            }  catch(Exception e) {
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String httpResponseMsg) {

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

                    Toast.makeText(RegisterActivity.this, "Thanks for registering. You can now log in to Shoppy !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else if(resultCode.contentEquals("2")) {
                    Toast.makeText(RegisterActivity.this, "Email already used. Please use another or try to login.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
