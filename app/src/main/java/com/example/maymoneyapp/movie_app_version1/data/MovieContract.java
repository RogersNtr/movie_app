package com.example.maymoneyapp.movie_app_version1.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Roger Nengwe on 25/07/2018.
 */

public final class MovieContract {
    //Variables for the content Resolver
    public static final String CONTENT_AUTHORITHY = "com.example.maymoneyapp.movie_app_version1.data.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITHY);
    //Constant for the Provider
    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_ID = 101;

    public static class MovieEntry implements BaseColumns{
        //Defining Table elements
        public static final String TABLE_NAME = "user_movie_favorite";
        public static final String COLUMN_MOVIE_ID = "favorite_movie_id";
        public static final String COLUMN_MOVIE_TITLE = "favorite_movie_title";
        public static final String COLUMN_MOVIE_TRAILER = "favorite_movie_trailer";

        //Building the Content URI
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
    }
}
