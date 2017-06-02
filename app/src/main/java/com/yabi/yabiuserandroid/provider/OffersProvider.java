package com.yabi.yabiuserandroid.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by rohitsingh on 23/12/16.
 */

public class OffersProvider extends ContentProvider {

    //fields for content provider
    public static final String PROVIDER_NAME = "com.yabi.user.provider";
    public static final String URL = "content://" + PROVIDER_NAME + "/offers";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    //Integer value used in content uri
    static final int OFFERS = 1;
    static final int OFFER_ID = 2;

    private static HashMap<String, String> offerMap;

    public OffersSQLiteHelper sqLiteOpenHelper;
    private SQLiteDatabase database;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "/*", OFFER_ID);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        sqLiteOpenHelper = new OffersSQLiteHelper(context);
        database = sqLiteOpenHelper.getWritableDatabase();
        if (database == null)
            return false;
        else
            return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(sqLiteOpenHelper.TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case OFFER_ID:
                queryBuilder.setProjectionMap(offerMap);
             //   queryBuilder.appendWhere(sqLiteOpenHelper.ID+"="+uri.getPathSegments());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == "") {
            // No sorting-> sort on id by default
            sortOrder = sqLiteOpenHelper.ID;
        }
        Cursor cursor = queryBuilder.query(database, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long row = database.insertWithOnConflict(sqLiteOpenHelper.TABLE_NAME, "", values, SQLiteDatabase.CONFLICT_REPLACE);
        if (row > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLException("Fail to add a new record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case OFFERS:
                count = database.delete(sqLiteOpenHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case OFFER_ID:
                Log.d("selsection", "selection" + selection);
                count = database.delete(sqLiteOpenHelper.TABLE_NAME, sqLiteOpenHelper.ID + " = " + selection +
                        (!TextUtils.isEmpty(null) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
