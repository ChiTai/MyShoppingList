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
import android.widget.Toast;

import com.example.myshoppinglist.myshoppinglist.R;
import com.example.myshoppinglist.myshoppinglist.others.HttpUtility;
import com.example.myshoppinglist.myshoppinglist.others.SessionManager;
import com.example.myshoppinglist.myshoppinglist.others.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ameliebarre1 on 17/01/2017.
 */

public class CreateProductActivity extends AppCompatActivity {

    SessionManager session;

    private EditText productName;
    private EditText productPrice;
    private EditText productQuantity;
    private Button createProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_product);

        // Session class instance
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        // Get the user data from session
        HashMap<String, String> user = session.getUserDetails();
        final String userToken = user.get(SessionManager.KEY_TOKEN);

        //Get the listID from the SharedPreferences in EditListActivity
        Intent getListInf = getIntent();
        final String myShoppingListID = getListInf.getStringExtra("LIST_ID");

        productName = (EditText) findViewById(R.id.myProduct);
        productPrice = (EditText) findViewById(R.id.myProductPrice);
        productQuantity = (EditText) findViewById(R.id.myProductQuantity);
        createProductButton = (Button) findViewById(R.id.createProduct);

        createProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the values from the layout (name, price, quantity)
                String myProduct = productName.getText().toString();
                myProduct = myProduct.replace(" ", "+");
                String myProductPrice = productPrice.getText().toString();
                String myProductQuantity = productPrice.getText().toString();

                new AsyncCreateProduct().execute(userToken, myShoppingListID, myProduct, myProductPrice, myProductQuantity);
            }
        });
    }

    public class AsyncCreateProduct extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            pDialog = new ProgressDialog(CreateProductActivity.this, R.style.MyCustomAlertDialog);
            pDialog.setMessage("Adding product...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = URL.CREATE_PRODUCT_URL + "?token=" + params[0] + "&shopping_list_id=" + params[1] + "&name=" + params[2] + "&quantity=" + params[3] + "&price=" + params[4] + "";
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
                int resultCode = jsonData.getInt("code");

                if (resultCode == 0) {

                    Intent intent = new Intent(CreateProductActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateProductActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
