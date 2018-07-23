package com.example.maymoneyapp.movie_app_version1;

import android.graphics.Movie;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.Toast;

import com.example.maymoneyapp.movie_app_version1.Utils.JsonUtils;
import com.example.maymoneyapp.movie_app_version1.Utils.Networksutils;
import com.example.maymoneyapp.movie_app_version1.model.GridAdapterMovie;
import com.example.maymoneyapp.movie_app_version1.model.Movies;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridView mGridView;
    private int itemClicked;
    private static String TAG = MainActivity.class.getSimpleName();
    private String mJsonApiResponse;
    private List<Movies> mArrayMovies;
    private GridAdapterMovie movieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mGridView = findViewById(R.id.grid_movie);
        makeAPIRequest(getString(R.string.sort_by_popularity)); //By default, sort by popularity

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClicked = item.getItemId();
        String menu_title = null;
        try{
            menu_title = (String)item.getTitle();
        }catch (ClassCastException e){
            Log.e(TAG, getString(R.string.cast_error));
            e.printStackTrace();
        }

        if (itemThatWasClicked == R.id.most_popular){
            makeAPIRequest(getString(R.string.sort_by_popularity));
            itemClicked = 0;
        }
        else if (itemThatWasClicked == R.id.top_rated){
            makeAPIRequest(getString(R.string.sort_by_vote));
            itemClicked = 1;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeAPIRequest(String searchCriteria) {
        //Call the Build URl function
        URL url = Networksutils.buildURLAPI(searchCriteria);
        Log.d(TAG, url.toString()); //TODO to delete
        new APIRequest().execute(url);
    }
    private class APIRequest extends AsyncTask<URL, Void, List<Movies>> {
        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }*/

        protected List<Movies> doInBackground(URL... urls) {
            String jsonResponse = null;
            jsonResponse = Networksutils.getResponseFromHttpUrl(urls[0]);
            Log.d(TAG, jsonResponse);//TODO delete
            //Parsing the Api Json Response
            List<Movies> movies = JsonUtils.parseJsonUtils(jsonResponse);
            return movies;
        }

        protected void onPostExecute(List<Movies> result) {
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (result != null && !result.isEmpty()) {
                showGrid(result);
            } else {
                showErrorMessage();
            }

        }
    }

    private void showErrorMessage() {
        Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show();


    }

    private void showGrid(List<Movies> result) {
        movieAdapter = new GridAdapterMovie(this, result);
        Log.d(TAG, movieAdapter.toString());
        mGridView.setAdapter(movieAdapter);
        Toast.makeText(this, "Grid", Toast.LENGTH_SHORT).show();
    }
    public void setJsonResponse(String jsonResponse){
        mJsonApiResponse = jsonResponse;
    }
}
