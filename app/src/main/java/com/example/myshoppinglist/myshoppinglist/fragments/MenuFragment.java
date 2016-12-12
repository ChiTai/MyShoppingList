package com.example.myshoppinglist.myshoppinglist.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.myshoppinglist.myshoppinglist.R;

/**
 * Created by ameliebarre1 on 11/12/2016.
 */

public class MenuFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.create_action:
                return false;

            case R.id.view_action:

                return true;

            default:
                break;
        }

        return false;
    }

}
