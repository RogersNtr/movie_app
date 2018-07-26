package com.example.maymoneyapp.movie_app_version1.Utils;

import android.net.Uri;
import android.util.Log;

import com.example.maymoneyapp.movie_app_version1.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import static com.example.maymoneyapp.movie_app_version1.Utils.Constant.API_KEY;
import static com.example.maymoneyapp.movie_app_version1.Utils.Constant.BASE_URL_IMAGE;
import static com.example.maymoneyapp.movie_app_version1.Utils.Constant.FILE_SIZE;
import static com.example.maymoneyapp.movie_app_version1.Utils.Constant.PARAM_API_KEY;

/**
 * Created by Roger Nengwe on 06/07/2018.
 */

public final class Networksutils {
   /* private final static String BASE_URL_API = "https://api.themoviedb.org/3/movie";
    private final static String BASE_URL_IMAGE = "https://image.tmdb.org/t/p"; // Base URL for uploading the image
    private final static String FILE_SIZE = "w185"; //Default size of the image. Possible to extract it from Json Response.
    private static final String PARAM_API_KEY = "api_key";
    private static String API_KEY = BuildConfig.ApiKey;*/

    private static final String TAG = Networksutils.class.getSimpleName(); //Class Name

    /**
     * Build the Url used to query the API.
     * @param searchAPIQuery the way to sort the movie(by popular movie or voting range).
     * @return The url used to query the TheMovieApi for movies
     *
     * */
    public static URL buildURLAPI(String searchAPIQuery){
        Uri buildUri = Uri.parse(Constant.BASE_URL_API).buildUpon().appendPath(searchAPIQuery)
                .appendQueryParameter(PARAM_API_KEY, API_KEY).
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

        Log.d(TAG, posted_path.replace("/", ""));
        Uri buildUri = Uri.parse(BASE_URL_IMAGE).buildUpon().appendPath(FILE_SIZE).appendEncodedPath(posted_path).
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
    public static String getResponseFromHttpUrl(URL url){
        HttpsURLConnection urlConnection = null;
        String response = null;
        InputStream inConnection = null;

        try {
            urlConnection = (HttpsURLConnection)url.openConnection();
            Log.e(TAG, urlConnection.toString());
            // assert urlConnection;
            inConnection = urlConnection.getInputStream();
            // assert inConnection ==  null;
            // Log.e(TAG, inConnection.toString());
            Scanner scanner = new Scanner(inConnection);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput){
                response = scanner.next();
            }
            scanner.close();
        } catch (IOException e) {
            Log.d(TAG, "+++++++++Openconnection");
            e.printStackTrace();
        }finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        return response;

    }
}
