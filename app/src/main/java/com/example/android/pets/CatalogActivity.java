package com.example.android.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetDbHelper;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this,EditorActivity.class);

                startActivity(intent);
            }
        });

        View emptyView= findViewById(R.id.empty_view);

        ListView listView=(ListView)findViewById(R.id.list_view_pet);
        listView.setEmptyView(emptyView);

        petCursorAdapter=new PetCursorAdapter(this,null);
        listView.setAdapter(petCursorAdapter);

        getSupportLoaderManager().initLoader(0,null,this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = ContentUris.withAppendedId(PetContract.CONTENT_URI, id);
                Intent intent = new Intent(CatalogActivity.this,EditorActivity.class);
                intent.setData(uri);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //displayDatabaseInfo();
    }

    PetCursorAdapter petCursorAdapter;
    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */

    private void insertDummyData()
    {
//        PetDbHelper mDbHelper = new PetDbHelper(this);
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(PetContract.NAME,"Toto");
        values.put(PetContract.BREED,"Terrier");
        values.put(PetContract.GENDER,PetContract.GENDER_MALE);
        values.put(PetContract.WEIGHT,7);

        getContentResolver().insert(PetContract.CONTENT_URI,values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
                insertDummyData();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={
                PetContract._ID,
                PetContract.NAME,
                PetContract.BREED
        };

        return new CursorLoader(this,
                PetContract.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        petCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        petCursorAdapter.swapCursor(null);
    }
}