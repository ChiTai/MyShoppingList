package com.example.myshoppinglist.myshoppinglist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.example.myshoppinglist.myshoppinglist.R;
import com.example.myshoppinglist.myshoppinglist.models.ShoppingList;


/**
 * Created by ameliebarre1 on 07/01/2017.
 */

public class ShoppingListAdapter extends ArrayAdapter<ShoppingList> {

    public ShoppingListAdapter(Context context, List<ShoppingList> shoppingLists) {
        super(context, 0, shoppingLists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_row,parent, false);
        }

        ShoppingListHolder viewHolder = (ShoppingListHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ShoppingListHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.shoppingListName);
            convertView.setTag(viewHolder);
        }

        //getItem(position) will get the item [position] of the List<ShoppingList> shoppingLists
        ShoppingList shopList = getItem(position);

        // Fill the list with the view
        viewHolder.name.setText(shopList.getName());

        return convertView;
    }

    class ShoppingListHolder{
        public TextView name;
    }

}
