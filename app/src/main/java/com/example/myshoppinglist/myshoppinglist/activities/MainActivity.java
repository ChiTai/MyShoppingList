package com.example.myshoppinglist.myshoppinglist.activities;

import com.example.myshoppinglist.myshoppinglist.R;
import com.example.myshoppinglist.myshoppinglist.others.HttpUtility;
import com.example.myshoppinglist.myshoppinglist.others.SessionManager;
import com.example.myshoppinglist.myshoppinglist.others.URL;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SessionManager session;
    ListView mShoppingList;

    ArrayList<HashMap<String, String>> shoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*---- ACTION BAR SETTINGS ----*/
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.logo);
        }

        /*---- SESSION ----*/
        // Session class instance
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        // Get the user data from session
        HashMap<String, String> user = session.getUserDetails();

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        final String userToken = user.get(SessionManager.KEY_TOKEN);

        /*---- SHOPPING LIST ----*/
        shoppingList = new ArrayList<>();
        mShoppingList = (ListView) findViewById(R.id.shoppingLists);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AsyncDisplayShoppingList().execute(userToken);
            }
        });
    }

    private class AsyncDisplayShoppingList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = URL.SHOPPING_LIST_URL + "?token=" + params[0] + "";

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

                if(listResult.length() == 0) {
                    TextView noList = (TextView) findViewById(R.id.noListMessage);
                    TextView createAList = (TextView) findViewById(R.id.createAList);
                    noList.setText(getText(R.string.noList));
                    createAList.setText(getText(R.string.createAList));

                    createAList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, CreateListActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    for (int i = 0; i < listResult.length(); i++) {
                        JSONObject c = listResult.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");
                        String created_date = c.getString("created_date");

                        final HashMap<String, String> list = new HashMap<>();
                        list.put("id", id);
                        list.put("name", name);
                        list.put("created_date", created_date);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                shoppingList.add(list);
                            }
                        });
                    }

                    ListAdapter adapter = new SimpleAdapter(MainActivity.this, shoppingList, R.layout.shopping_list_row, new String[]{"id", "name", "created_date"}, new int[]{R.id.shoppingListId, R.id.shoppingListName, R.id.shoppingListDate});
                    mShoppingList.setAdapter(adapter);

                    mShoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            final TextView listName = (TextView) view.findViewById(R.id.shoppingListName);
                            final String listNameItem = listName.getText().toString();

                            final TextView listId = (TextView) view.findViewById(R.id.shoppingListId);
                            final String listIdItem = listId.getText().toString();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MainActivity.this, EditListActivity.class);
                                    intent.putExtra("LIST_ID", listIdItem);
                                    intent.putExtra("LIST_NAME", listNameItem);
                                    startActivity(intent);
                                }
                            });
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
                Intent viewLists = new Intent(MainActivity.this, MainActivity.class);
                startActivity(viewLists);
                break;

            case R.id.createList:
                Intent createList = new Intent(MainActivity.this, CreateListActivity.class);
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
