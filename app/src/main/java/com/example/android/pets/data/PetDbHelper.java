package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mayank on 19/3/17.
 */

public class PetDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="shelter.db";

    public PetDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TABLE="CREATE TABLE "+
                PetContract.TABLE_NAME+"("+
                PetContract._ID+" INTEGER PRIMARY KEY AUTOINCREMENT"+","+
                PetContract.NAME+" TEXT NOT NULL"+","+
                PetContract.BREED+" TEXT," +
                PetContract.GENDER+" INTEGER NOT NULL, "+
                PetContract.WEIGHT+" INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
