package com.example.maymoneyapp.movie_app_version1.Utils;

import com.example.maymoneyapp.movie_app_version1.BuildConfig;

/**
 * Created by Roger Nengwe on 26/07/2018.
 */

public final class Constant {
    //Constant for the API Request and to build Url
    public final static String BASE_URL_API = "https://api.themoviedb.org/3/movie";
    public final static String BASE_URL_IMAGE = "https://image.tmdb.org/t/p"; // Base URL for uploading the image
    public final static String FILE_SIZE = "w185"; //Default size of the image. Possible to extract it from Json Response.
    public static final String PARAM_API_KEY = "api_key";
    public static final String API_KEY = BuildConfig.ApiKey;

    //Constant for the DetailActivity Intent
    public final static String MOVIE_EXTRA = "movie_object";
    public final static String MOVIE_POSITION_EXTRA = "position_clicked";

    //Constant for building the URL for fetching data for the the movies and reviews end points.(DetailActivity Class)
    public static final String REVIEW_PATH = "reviews";
    public static final String VIDEO_PATH = "videos";

    //Constant for DetailActivity, to deal with rotation change
    public final static String PARCELABLE_MOVIE_DETAIL_KEY = "saved_detail";
    public final static String MOVIE_TRAILER_KEY = "saved_trailers";
    public final static String MOVIE_REVIEWS_KEY = "saved_reviews";
}
