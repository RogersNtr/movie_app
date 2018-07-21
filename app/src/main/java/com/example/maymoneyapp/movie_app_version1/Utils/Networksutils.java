package com.example.maymoneyapp.movie_app_version1.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Roger Nengwe on 06/07/2018.
 */

public final class Networksutils {
    final static String BASE_URL_API = "https://api.themoviedb.org/3/discover/movie";
    final static String BASE_URL_IMAGE = "https://image.tmdb.org/t/p"; // Base URL for uploading the image
    final static String FILE_SIZE = "w185"; //Default size of the image. Possible to extract it from Json Response.
    final static String FILE_PATH=""; // To be extracted from the "poster_path" field of the JSON response.
    private static final String PARAM_API_KEY = "api_key";
    private static String API_KEY = "2cb832e164482a5d3fab6f9d00c6a1d5";

    final static String sortBy = "popularity.desc";
    final static String PARAM_SORT = "sort_by";
    private static final String TAG = "NetWorksutils"; //Class Name

    /**
     * Build the Url used to query the API.
     * @param searchAPIQuery the way to sort the movie(by popular movie or voting range).
     * @return The url used to query the TheMovieApi for movies
     *
     * */
    public static URL buildURLAPI(String searchAPIQuery){
        Uri buildUri = Uri.parse(BASE_URL_API).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY).
                        appendQueryParameter(PARAM_SORT, searchAPIQuery).
                        build();

        Log.d(TAG, "Value of the Url build so far :  "  + buildUri.toString());
        URL url  = null;
        try {
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    /**
     * Build the Url used to query the API.
     * @param posted_path the results Posted_path(Images) from the result of Querying the DataBase
     * @return The url used to query the TheMovieApi for movies
     *@throws MalformedURLException, if the Url was malformed.
     * */
    public static URL buildURLImage(String posted_path){
        Uri buildUri = Uri.parse(BASE_URL_API).buildUpon()
                .appendPath(FILE_SIZE).appendPath(posted_path).
                        build();

        Log.d(TAG, "Value of the Url build so far :  "  + buildUri.toString());
        URL url  = null;
        try {
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    /**
     * Build the Url used to query the API.
     * @param url the url to fetch the http response from.
     * @return  The content of the http response (Json)
     *@throws java.io.IOException related to network and stream reading
     * */
    public static String getResponseFrommHttpUrl(URL url){
        HttpsURLConnection urlConnection = null;
        String response = null;

        try {
            urlConnection = (HttpsURLConnection)url.openConnection();
            InputStream inConnection = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inConnection);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput){
                response = scanner.next();
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
        return response;

    }
}
