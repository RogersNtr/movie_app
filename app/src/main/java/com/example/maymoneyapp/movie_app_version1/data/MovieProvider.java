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
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Roger Nengwe on 25/07/2018.
 */

public class MovieProvider extends ContentProvider {
    private static final String LOG_TAG  = ContentProvider.class.getSimpleName();
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
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        switch (match){
            case MovieContract.MOVIE:
                //Toast.makeText(getContext(), "enter the MOVIE case query", Toast.LENGTH_SHORT).show();
                returnCursor = db.query(MovieContract.MovieEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case MovieContract.MOVIE_WITH_ID:
                Toast.makeText(getContext(), "enter the MOVIE_WITH_ID case query", Toast.LENGTH_SHORT).show();
                String id = uri.getPathSegments().get(1);
                String mySelection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                String[] mySelectionArgs = new String[]{id};
                returnCursor = db.query(MovieContract.MovieEntry.TABLE_NAME, projection,
                        mySelection, mySelectionArgs, null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        //Set a notification Uri to the Cursor.
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
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
            getContext().getContentResolver().notifyChange(uri, null);//Notify the resolver of the change
        else
            throw new NullPointerException("Unable to get the context in " + MovieProvider.class.getSimpleName());
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int moviesDeleted; // starts as 0

        // Write the code to delete a single row of data
        // Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case MovieContract.MOVIE_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                Log.e(LOG_TAG, "Id of the element to delete : " + id);
                Log.e(LOG_TAG, "Uri of the element to delete : " + uri);
                // Use selections/selectionArgs to filter for this ID
                String whereClause = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                moviesDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, whereClause, new String[]{id});
                Log.e(LOG_TAG, "Id of the movie  delete : " + id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (moviesDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
