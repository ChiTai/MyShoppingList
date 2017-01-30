package com.example.myshoppinglist.myshoppinglist.activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.myshoppinglist.myshoppinglist.others.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ameliebarre1 on 22/01/2017.
 */

public class EditProductActivity extends AppCompatActivity {

    SessionManager session;

    private EditText productNameField;
    private EditText productPriceField;
    private EditText productQuantityField;
    private Button updateProductButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);

        // Get the product informations from the EditListActivity
        Intent intent = getIntent();
        final String productID = intent.getStringExtra("PRODUCT_ID");
        final String productName = intent.getStringExtra("PRODUCT_NAME");
        final String productPrice = intent.getStringExtra("PRODUCT_PRICE");
        final String productQuantity = intent.getStringExtra("PRODUCT_QUANTITY");

        productNameField = (EditText) findViewById(R.id.updateProductName);
        productNameField.setText(productName);

        productPriceField = (EditText) findViewById(R.id.updateProductPrice);
        productPriceField.setText(productPrice);

        productQuantityField = (EditText) findViewById(R.id.updateProductQuantity);
        productQuantityField.setText(productQuantity);

        // Session class instance
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        // Get the user data from session
        HashMap<String, String> user = session.getUserDetails();
        final String userToken = user.get(SessionManager.KEY_TOKEN);

        updateProductButton = (Button) findViewById(R.id.updateProductButton);
        deleteButton = (Button) findViewById(R.id.deleteProduct);

        updateProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = productNameField.getText().toString();
                productName = productName.replace(" ", "+");
                String productPrice = productPriceField.getText().toString();
                String productQuantity = productQuantityField.getText().toString();

                Log.v("informations : ", userToken + " " + productID + " " + productName + " " + productQuantity + " " + productPrice);

                new AsyncUpdateProduct().execute(userToken, productID, productName, productQuantity, productPrice);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncDeleteProduct().execute(userToken, productID);
            }
        });
    }

    private class AsyncUpdateProduct extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(EditProductActivity.this, R.style.MyCustomAlertDialog);
            pDialog.setMessage("Updating product informations...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = URL.EDIT_PRODUCT_URL + "?token=" + params[0] + "&id=" + params[1] + "&name=" + params[2] + "&quantity=" + params[3] + "&price=" + params[4] + "";
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
                    Intent intent = new Intent(EditProductActivity.this, MainActivity.class);
                    startActivity(intent);


                } else {
                    Toast.makeText(EditProductActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class AsyncDeleteProduct extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(EditProductActivity.this, R.style.MyCustomAlertDialog);
            pDialog.setMessage("Deleting product...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = URL.REMOVE_PRODUCT_URL + "?token=" + params[0] + "&id=" + params[1] + "";
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

            super.onPostExecute(httpResponseMsg);

            try {
                JSONObject jsonData = new JSONObject(httpResponseMsg);

                // Get the result code
                int resultCode = jsonData.getInt("code");

                if(resultCode == 0) {
                    Intent deleteProduct = new Intent(EditProductActivity.this, MainActivity.class);
                    startActivity(deleteProduct);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
