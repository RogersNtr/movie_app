package com.example.maymoneyapp.movie_app_version1.Utils;

import android.util.Log;

import com.example.maymoneyapp.movie_app_version1.model.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Etudiant UPS on 21/07/2018.
 */

public final  class JsonUtils {
    private final static String JSONIDIMAGE = "poster_path"; //The name of the field containing the image path value
    private final static String TAG = JsonUtils.class.getSimpleName();
    /**
     * Build the Url used to query the API.
     * @param jsonResponse the json response from the TheMovieDB API request.
     * @return  The content of the http response (Json)
     *@throws java.io.IOException related to network and stream reading
     * */
    public static List<Movies> parseJsonUtils(String jsonResponse){
        Movies movies = new Movies();
        ArrayList<Movies> listOfReturnMovies = new ArrayList<>();
        try {
            JSONObject jsonRoot=new JSONObject(jsonResponse);
            JSONArray results = jsonRoot.getJSONArray("results");
            JSONObject jsonResults = null;
            String imagePath, title, overview, imageName, userRating, releaseDate;
            for(int i=0; i<results.length();i++){
                jsonResults = results.getJSONObject(i);
                if(jsonResults!=null) {
                    //Extract Image path
                    imageName = jsonResults.getString("poster_path");
                    Log.d(TAG, imageName);
                    imagePath = Networksutils.buildURLImage(imageName).toString();
                    title = jsonResults.getString("title");
                    overview = jsonResults.getString("overview");
                    userRating  = jsonResults.getString("vote_average");
                    releaseDate = jsonResults.getString("release_date");
                    listOfReturnMovies.add(new Movies(imagePath, title, overview, userRating, releaseDate));
                }
                else
                    throw new NullPointerException("JsonObject not found");
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listOfReturnMovies;
    }
}
