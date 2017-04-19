package com.example.android.pets.data;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by mayank on 18/3/17.
 */

public final class PetContract
{
    public static final String TABLE_NAME="pets";

    public static final String _ID="_id";
    public static final String NAME="name";
    public static final String BREED="breed";
    public static final String GENDER="gender";
    public static final String WEIGHT="weight";

    public static final String CONTENT_AUTHORITY = "com.example.android.pets";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PETS = "pets";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

    public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;
    public static final String CONTENT_ITEM_TYPE =ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

    public static final int GENDER_UNKNOWN=0;
    public static final int GENDER_MALE=1;
    public static final int GENDER_FEMALE=2;
}
