package com.example.maymoneyapp.movie_app_version1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.maymoneyapp.movie_app_version1.Utils.JsonUtils;
import com.example.maymoneyapp.movie_app_version1.Utils.Networksutils;
import com.example.maymoneyapp.movie_app_version1.model.DetailActivity;
import com.example.maymoneyapp.movie_app_version1.model.GridAdapterMovie;
import com.example.maymoneyapp.movie_app_version1.model.Movies;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridView mGridView;
    private final static String TAG = MainActivity.class.getSimpleName();
    private List<Movies> mArrayMovies = new ArrayList<>();
    private final static String SAVE_INSTANCE_GRID_KEY = "movies"; // Key for retrieving the instance saved in the Bundle



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView = findViewById(R.id.grid_movie);


        if(savedInstanceState == null ) {
            makeAPIRequest(getString(R.string.sort_by_popularity)); //By default, sort by popularity
            //mArrayMovies = new ArrayList<>();
        }
        else if(savedInstanceState.containsKey(SAVE_INSTANCE_GRID_KEY)){
            mArrayMovies = savedInstanceState.getParcelableArrayList(SAVE_INSTANCE_GRID_KEY);
            showGrid(mArrayMovies);
        }



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
       outState.putParcelableArrayList(SAVE_INSTANCE_GRID_KEY, (ArrayList<Movies>) mArrayMovies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClicked = item.getItemId();

        if (itemThatWasClicked == R.id.most_popular){
            makeAPIRequest(getString(R.string.sort_by_popularity));
        }
        else if (itemThatWasClicked == R.id.top_rated){
            makeAPIRequest(getString(R.string.sort_by_vote));
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
            String jsonResponse;
            jsonResponse = Networksutils.getResponseFromHttpUrl(urls[0]);
            Log.d(TAG, jsonResponse);//TODO delete
            //Parsing the Api Json Response
            List<Movies> movies = JsonUtils.parseJsonUtils(jsonResponse);
            setmArrayMovies(movies);
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
        GridAdapterMovie movieAdapter = new GridAdapterMovie(this, result);
        Log.d(TAG, String.valueOf(result.size()));
        mGridView.setAdapter(movieAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Log.d(TAG, ((Movies)adapterView.getItemAtPosition(position)).getmMovieImage() + "Bonjour");
                launchDetailActivity((Movies)adapterView.getItemAtPosition(position), position);
            }
        });
        //Toast.makeText(this, "Grid", Toast.LENGTH_SHORT).show();
    }

    private void launchDetailActivity(Movies itemAtPosition, int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra(DetailActivity.MOVIE_EXTRA, itemAtPosition);
        detailIntent.putExtra(DetailActivity.MOVIE_POSITION_EXTRA, position);
        startActivity(detailIntent);
    }

    private void setmArrayMovies(List<Movies> mArrayMovies) {
        this.mArrayMovies = mArrayMovies;
    }

}
