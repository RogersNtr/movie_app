package com.example.maymoneyapp.movie_app_version1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Roger Nengwe on 25/07/2018.
 */

public class MovieProvider extends ContentProvider {
    private MovieDbHelper mMovieDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();


    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH); //An empty matcher
        //Add matches
        //directory
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITHY, MovieContract.MovieEntry.TABLE_NAME, MovieContract.MOVIE); // Multi Row directory
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITHY, MovieContract.MovieEntry.TABLE_NAME + "/#", MovieContract.MOVIE_WITH_ID);
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper  = new MovieDbHelper(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case MovieContract.MOVIE:
                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
                if (id > 0){
                    //Insertion successful
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                }else {
                    throw new SQLException("Failed to insert row into : " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if (getContext()!=null)
            getContext().getContentResolver().notifyChange(uri, null);
        else
            throw new NullPointerException("Unable to get the context in " + MovieProvider.class.getSimpleName());
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
