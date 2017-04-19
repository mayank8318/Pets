package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import com.example.android.pets.R;

/**
 * {@link ContentProvider} for Pets app.
 */
public class PetProvider extends ContentProvider {

    private PetDbHelper mDbHelper;
    /** Tag for the log messages */
    public static final String LOG_TAG = PetProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the pets table */
    private static final int PETS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int PET_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // TODO: Add 2 content URIs to URI matcher
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS,PetProvider.PETS);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS + "/#",PetProvider.PET_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        mDbHelper=new PetDbHelper(getContext());

        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                // TODO: Perform database query on pets table
                cursor=database.query(PetContract.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PET_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = PetContract._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(PetContract.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }
    /**
     * Insert new data into the provider with the given ContentValues.
     */

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPet(Uri uri, ContentValues values) {

        String name = values.getAsString(PetContract.NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        Integer weight=values.getAsInteger(PetContract.WEIGHT);
        if(weight == null || weight<=0)
            throw new IllegalArgumentException("Pet requires a valid weight");
        // TODO: Insert a new pet into the pets database table with the given ContentValues
        SQLiteDatabase db= mDbHelper.getWritableDatabase();
        long id=db.insert(PetContract.TABLE_NAME,null,values);
        Toast.makeText(getContext(), R.string.pet_saved ,Toast.LENGTH_LONG).show();
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs)
    {
        if (contentValues.containsKey(PetContract.NAME)) {
            String name = contentValues.getAsString(PetContract.NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }
        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (contentValues.containsKey(PetContract.GENDER)) {
            Integer gender = contentValues.getAsInteger(PetContract.GENDER);
            /*if (gender == null || !PetContract.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }*/
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (contentValues.containsKey(PetContract.WEIGHT)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = contentValues.getAsInteger(PetContract.WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }

        // No need to check the breed, any value is valid (including null).
        if (contentValues.size() == 0) {
            return 0;
        }
        final int match=sUriMatcher.match(uri);
        switch (match)
        {
            case PETS:
                SQLiteDatabase db= mDbHelper.getWritableDatabase();
                db.update(PetContract.TABLE_NAME,contentValues,selection,selectionArgs);
                break;
            case PET_ID:
                db= mDbHelper.getWritableDatabase();
                selection=PetContract._ID + "=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                db.update(PetContract.TABLE_NAME,contentValues,selection,selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Invalid uri");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return 0;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // Delete all rows that match the selection and selection args
                getContext().getContentResolver().notifyChange(uri,null);
                return database.delete(PetContract.TABLE_NAME, selection, selectionArgs);
            case PET_ID:
                // Delete a single row given by the ID in the URI
                selection = PetContract._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                getContext().getContentResolver().notifyChange(uri,null);
                return database.delete(PetContract.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetContract.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}