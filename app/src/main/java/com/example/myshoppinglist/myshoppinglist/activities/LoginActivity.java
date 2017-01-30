package com.example.myshoppinglist.myshoppinglist.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
            final String loginName = userEmail.getText().toString();
            final String loginPassword = userPassword.getText().toString();

            if(!validate()) {
                onLoginFailed();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AsyncLogin().execute(loginName, loginPassword);
                    }
                });
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
        protected void onPostExecute(final String httpResponseMsg) {

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
                int resultCode = jsonData.getInt("code");

                if(resultCode == 0) {
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

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onLoginFailed() {
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            TextView checkEmail = (TextView)findViewById(R.id.emptyEmail);
            checkEmail.setText(getString(R.string.emptyEmail));
            valid = false;
        } else {
            userEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            TextView checkPassword = (TextView)findViewById(R.id.emptyPassword);
            checkPassword.setText(getString(R.string.emptyPassword));
            valid = false;
        } else {
            userPassword.setError(null);
        }

        return valid;
    }
}
