package com.example.maymoneyapp.movie_app_version1.Utils;

import android.util.Log;

import com.example.maymoneyapp.movie_app_version1.model.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roger Nengwe on 21/07/2018.
 */

public final  class JsonUtils {
    private final static String TAG = JsonUtils.class.getSimpleName();
    /**
     * Build the Url used to query the API.
     * @param jsonResponse the json response from the TheMovieDB API request.
     * @return  The content of the http response (Json)
     *@throws java.io.IOException related to network and stream reading
     * */
    public static List<Movies> parseJsonUtils(String jsonResponse){
        ArrayList<Movies> listOfReturnMovies = new ArrayList<>();
        try {
            JSONObject jsonRoot=new JSONObject(jsonResponse);
            JSONArray results = jsonRoot.getJSONArray("results");
            JSONObject jsonResults;
            String imagePath, title, overview, imageName, userRating, releaseDate;
            int id;

            for(int i=0; i<results.length();i++){
                jsonResults = results.getJSONObject(i);
                assert jsonResults == null;
                if(jsonResults!=null) {
                    //Extract Image path
                    imageName = jsonResults.getString("poster_path");
                    imagePath = Networksutils.buildURLImage(imageName).toString();
                    title = jsonResults.getString("title");
                    overview = jsonResults.getString("overview");
                    userRating  = jsonResults.getString("vote_average");
                    releaseDate = jsonResults.getString("release_date");
                    id = jsonResults.getInt("id");
                    listOfReturnMovies.add(new Movies(imagePath, title, overview, userRating + "/10", releaseDate, id));
                }
                else
                    throw new NullPointerException("JsonObject not found");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listOfReturnMovies;
    }
    public static List<String> parseJsonDetailMovieReview(String jsonToParse){
        JSONObject jsonReview ;
        JSONArray results_;
        String review ;
        List<String> listOfReviews = new ArrayList<>();
        String author;
        try {
            JSONObject jsonRoot = new JSONObject(jsonToParse);
            results_ = jsonRoot.getJSONArray("results");
            Log.e(TAG, "length review : " + results_.length());
            for(int i = 0; i<results_.length(); i++) {
                if (results_.length() > 0) {
                    jsonReview = results_.getJSONObject(i); //Take the 1st Review
                    if (jsonReview != null) {
                        review = jsonReview.getString("content");
                        author = jsonReview.getString("author");
                        listOfReviews.add(review);
                    }
                } else {
                    review = "Sorry, No Review for this film";
                    listOfReviews.add(review);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listOfReviews;
    }

    public static List<String> parseJsonDetailTrailers(String jsonToParse){
        JSONObject jsonTrailer;
        JSONArray results_ ;
        String videoName;
        String idMovie; //Movie Id
        ArrayList<String> listTrailers = new ArrayList<>();
        try {
            JSONObject jsonRoot = new JSONObject(jsonToParse);
            results_ = jsonRoot.getJSONArray("results");
            if (results_.length() > 0){
                for(int i = 0; i<results_.length(); i++) {
                    Log.d(TAG, "Length videosArray: " + results_.length());
                    jsonTrailer = results_.getJSONObject(i);
                    videoName = jsonTrailer.getString("name");
                    Log.d(TAG, "Video Name : " + videoName);
                    idMovie = jsonTrailer.getString("id");
                    listTrailers.add(videoName);
                }
            }else
                listTrailers.add("No trailer for this video! Sorry");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listTrailers;

    }
}
