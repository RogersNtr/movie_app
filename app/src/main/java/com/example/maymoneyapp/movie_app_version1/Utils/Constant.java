package com.example.maymoneyapp.movie_app_version1.Utils;

import com.example.maymoneyapp.movie_app_version1.BuildConfig;

/**
 * Created by Roger Nengwe on 26/07/2018.
 */

public final class Constant {
    public final static String BASE_URL_API = "https://api.themoviedb.org/3/movie";
    public final static String BASE_URL_IMAGE = "https://image.tmdb.org/t/p"; // Base URL for uploading the image
    public final static String FILE_SIZE = "w185"; //Default size of the image. Possible to extract it from Json Response.
    public static final String PARAM_API_KEY = "api_key";
    public static final String API_KEY = BuildConfig.ApiKey;
}
