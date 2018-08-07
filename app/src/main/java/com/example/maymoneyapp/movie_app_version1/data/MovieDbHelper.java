package com.example.maymoneyapp.movie_app_version1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Roger Nengwe on 25/07/2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATA_BASE_NAME = "UserFavoriteMovie.db";
    private static final int DATA_BASE_VERSION = 1;
   /* private static final String SQL_CREATE_ENTITIES = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
            MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER, " +
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER + " TEXT NOT NULL);";*/

    private static final String SQL_CREATE_ENTITIES = "CREATE TABLE " +
            MovieContract.MovieEntry.TABLE_NAME + "(" + MovieContract.MovieEntry._ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER, " +
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_MOVIE_URL + " TEXT NOT NULL);";

    private static final String SQL_DELETE_ENTITIES = "DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME;



    public MovieDbHelper(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTITIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTITIES);

    }
}
