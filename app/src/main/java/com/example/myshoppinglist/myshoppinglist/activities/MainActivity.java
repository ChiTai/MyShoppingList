package com.example.myshoppinglist.myshoppinglist.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.myshoppinglist.myshoppinglist.R;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "test";
    private EditText name;
    private EditText firstname;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe);

        name = (EditText) findViewById(R.id.registerName);
        firstname = (EditText) findViewById(R.id.registerFirstName);
        email = (EditText) findViewById(R.id.registerEmail);
        password = (EditText) findViewById(R.id.registerPassword);

        Button registerButton = (Button) findViewById(R.id.btnRegister);

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
    }

    private class AsyncRegister extends AsyncTask<String, String, String> {
        ProgressDialog loading = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://appspaces.fr/esgi/shopping_list/account/subscribe.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", params[0]);
                postDataParams.put("password", params[1]);
                postDataParams.put("firstname", params[2]);
                postDataParams.put("lastname", params[3]);

                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if(responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();
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

    public String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()) {
            String key = itr.next();
            Object value = params.get(key);

            if(first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }

}
