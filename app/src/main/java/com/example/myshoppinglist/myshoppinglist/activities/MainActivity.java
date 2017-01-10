package com.example.myshoppinglist.myshoppinglist.activities;

import com.example.myshoppinglist.myshoppinglist.R;
import com.example.myshoppinglist.myshoppinglist.others.HttpUtility;
import com.example.myshoppinglist.myshoppinglist.others.SessionManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    SessionManager session;
    ListView mShoppingList;

    ArrayList<HashMap<String, String>> shoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Session class instance
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        // Get the user data from session
        HashMap<String, String> user = session.getUserDetails();

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "Email : " + user.get(SessionManager.KEY_EMAIL) + " Token : " + user.get(SessionManager.KEY_TOKEN), Toast.LENGTH_LONG).show();

        final String userToken = user.get(SessionManager.KEY_TOKEN);

        shoppingList = new ArrayList<>();
        mShoppingList = (ListView) findViewById(R.id.shoppingLists);

        new AsyncShoppingList().execute(userToken);

    }

    private class AsyncShoppingList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = "http://appspaces.fr/esgi/shopping_list/shopping_list/list.php?token=" + params[0] + "";

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

                        String name = c.getString("name");
                        String created_date = c.getString("created_date");

                        HashMap<String, String> list = new HashMap<>();
                        list.put("name", name);
                        list.put("created_date", created_date);

                        shoppingList.add(list);
                    }

                    ListAdapter adapter = new SimpleAdapter(MainActivity.this, shoppingList, R.layout.shopping_list_row, new String[]{"name", "created_date"}, new int[]{R.id.shoppingListName, R.id.shoppingListDate});
                    mShoppingList.setAdapter(adapter);

                    mShoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            TextView listName = (TextView) findViewById(R.id.shoppingListName);
                            String listNameItem = listName.getText().toString();

                            Intent intent = new Intent(MainActivity.this, EditListActivity.class);
                            intent.putExtra("LIST_NAME", listNameItem);
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
                Toast.makeText(this, "View lists", Toast.LENGTH_SHORT).show();
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
