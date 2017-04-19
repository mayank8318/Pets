package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;

/**
 * Created by mayank on 31/3/17.
 */

public class PetCursorAdapter extends CursorAdapter {

    PetCursorAdapter(Context context, Cursor c)
    {
        super(context,c,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_list,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView textView=(TextView)view.findViewById(R.id.name);
        TextView textView2=(TextView)view.findViewById(R.id.summary);

        textView.setText(cursor.getString(cursor.getColumnIndex(PetContract.NAME)));
        textView2.setText(cursor.getString(cursor.getColumnIndex(PetContract.BREED)));

    }
}
