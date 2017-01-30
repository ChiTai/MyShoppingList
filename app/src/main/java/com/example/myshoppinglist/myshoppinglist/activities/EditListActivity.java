package com.example.myshoppinglist.myshoppinglist.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myshoppinglist.myshoppinglist.R;
import com.example.myshoppinglist.myshoppinglist.others.HttpUtility;
import com.example.myshoppinglist.myshoppinglist.others.SessionManager;
import com.example.myshoppinglist.myshoppinglist.others.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ameliebarre1 on 09/01/2017.
 */

public class EditListActivity extends AppCompatActivity {

    SessionManager session;

    private EditText listNameField;
    private Button updateListButton;
    private Button deleteListButton;
    private TextView createProductButton;
    private Boolean isCompleted;

    ListView mProductList;

    ArrayList<HashMap<String, String>> productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_list);

        Intent intent = getIntent();
        final String listId = intent.getStringExtra("LIST_ID");
        final String listName = intent.getStringExtra("LIST_NAME");

        listNameField = (EditText) findViewById(R.id.updateListField);
        listNameField.setText(listName);

        isCompleted = false;

        // Session class instance
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        // Get the user data from session
        HashMap<String, String> user = session.getUserDetails();
        final String userToken = user.get(SessionManager.KEY_TOKEN);

        deleteListButton = (Button) findViewById(R.id.deleteList);
        updateListButton = (Button) findViewById(R.id.updateList);
        createProductButton = (TextView) findViewById(R.id.addProduct);

        updateListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String listName = listNameField.getText().toString();
                listName = listName.replace(" ", "+");

                new AsyncUpdateShoppingList().execute(userToken, listId, listName, String.valueOf(isCompleted));
            }
        });

        createProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent getListInf = getIntent();
                final String listId = getListInf.getStringExtra("LIST_ID");

                Intent intent = new Intent(EditListActivity.this, CreateProductActivity.class);
                intent.putExtra("LIST_ID", listId);
                startActivity(intent);
            }
        });

        deleteListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            new AsyncDeleteShoppingList().execute(userToken, listId);
            }
        });

        productList = new ArrayList<>();
        mProductList = (ListView) findViewById(R.id.productLists);

        new AsyncDisplayProductList().execute(userToken, listId);
    }

    private class AsyncUpdateShoppingList extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(EditListActivity.this, R.style.MyCustomAlertDialog);
            pDialog.setMessage("Updating shopping list...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = URL.EDIT_SHOPPING_LIST_URL + "?token=" + params[0] + "&id=" + params[1] + "&name=" + params[2] + "";
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
                    Intent intent = new Intent(EditListActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(EditListActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class AsyncDeleteShoppingList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = URL.REMOVE_SHOPPING_LIST_URL + "?token=" + params[0] + "&id=" + params[1] + "";
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
                    Intent deleteList = new Intent(EditListActivity.this, MainActivity.class);
                    startActivity(deleteList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class AsyncDisplayProductList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = URL.PRODUCT_LIST_URL + "?token=" + params[0] + "&shopping_list_id=" + params[1] + "";

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

            super.onPostExecute(httpResponseMsg);

            try {
                JSONObject jsonData = new JSONObject(httpResponseMsg);
                JSONArray listResult = jsonData.getJSONArray("result");

                // Get the result code
                int resultCode = jsonData.getInt("code");

                if(resultCode == 0) {

                    for (int i = 0; i < listResult.length(); i++) {
                        JSONObject c = listResult.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");
                        String quantity = c.getString("quantity");
                        Double price = c.getDouble("price");
                        String priceToString = String.valueOf(price);

                        HashMap<String, String> list = new HashMap<>();
                        list.put("id", id);
                        list.put("name", name);
                        list.put("quantity", quantity);
                        list.put("price", priceToString);

                        productList.add(list);
                    }

                    ListAdapter adapter = new SimpleAdapter(EditListActivity.this, productList, R.layout.product_list_row, new String[]{"id", "name", "quantity", "price"}, new int[]{R.id.productID, R.id.productName, R.id.productPrice, R.id.productQuantity});
                    mProductList.setAdapter(adapter);

                    mProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            TextView productID = (TextView) view.findViewById(R.id.productID);
                            String productIDItem = productID.getText().toString();

                            Log.v("product_ID", productIDItem);

                            TextView productName = (TextView) view.findViewById(R.id.productName);
                            String productNameItem = productName.getText().toString();

                            TextView productPrice = (TextView) view.findViewById(R.id.productPrice);
                            String productPriceItem = productPrice.getText().toString();

                            TextView productQuantity = (TextView) view.findViewById(R.id.productQuantity);
                            String productQuantityItem = productQuantity.getText().toString();

                            Intent intent = new Intent(EditListActivity.this, EditProductActivity.class);
                            intent.putExtra("PRODUCT_ID", productIDItem);
                            intent.putExtra("PRODUCT_NAME", productNameItem);
                            intent.putExtra("PRODUCT_PRICE", productPriceItem);
                            intent.putExtra("PRODUCT_QUANTITY", productQuantityItem);
                            startActivity(intent);
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
                Intent viewLists = new Intent(EditListActivity.this, MainActivity.class);
                startActivity(viewLists);
                break;

            case R.id.createList:
                Intent createList = new Intent(EditListActivity.this, CreateListActivity.class);
                startActivity(createList);
                break;

            case R.id.logout:
                session.logoutUser();

            default:
                break;
        }
        return true;
    }

}
