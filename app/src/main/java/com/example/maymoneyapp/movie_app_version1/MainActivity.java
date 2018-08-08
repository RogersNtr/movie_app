package com.example.maymoneyapp.movie_app_version1;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maymoneyapp.movie_app_version1.Utils.Constant;
import com.example.maymoneyapp.movie_app_version1.Utils.JsonUtils;
import com.example.maymoneyapp.movie_app_version1.Utils.Networksutils;
import com.example.maymoneyapp.movie_app_version1.data.MovieContract;
import com.example.maymoneyapp.movie_app_version1.model.DetailActivity;
import com.example.maymoneyapp.movie_app_version1.model.GridAdapterMovie;
import com.example.maymoneyapp.movie_app_version1.model.Movies;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private GridView mGridView;
    private TextView mTextView;
    private Boolean isErrorOccured = false;
    private final static String TAG = MainActivity.class.getSimpleName();
    private List<Movies> mArrayMovies = new ArrayList<>();
    private GridAdapterMovie mGridMovieAdapter;
    private final static String SAVE_INSTANCE_GRID_KEY = "movies"; // Key for retrieving the instance saved in the Bundle
    private static final String SAVE_OCCURENCE_ERROR = "error_message";
    private static final int CURSOR_LOADER_ID = 13; // the id that uniquely identify a loader.*
    private Cursor mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView = findViewById(R.id.grid_movie);
        mTextView = findViewById(R.id.tv_error_message);


        if(savedInstanceState == null ) {
            if (!isErrorOccured)
                makeAPIRequest(getString(R.string.sort_by_popularity)); //By default, sort by popularity
            else
                showErrorMessage();
            //mArrayMovies = new ArrayList<>();
        }
        else if(savedInstanceState.containsKey(SAVE_INSTANCE_GRID_KEY)){
            if (!isErrorOccured) {
                mArrayMovies = savedInstanceState.getParcelableArrayList(SAVE_INSTANCE_GRID_KEY);
                showGrid(mArrayMovies);
            }else{
                showErrorMessage();
            }
        }
        Log.e(TAG, "init Loader start");
        //getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        Log.e(TAG, "init Loader end");
    }

    /**
     * This method is called after this activity has been paused or restarted.
     * Often, this is after new data has been inserted through an AddTaskActivity,
     * so this restarts the loader to re-query the underlying data for any changes.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all movies
        //getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
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
            if (!isErrorOccured) {
                makeAPIRequest(getString(R.string.sort_by_popularity));
                setTitle(getResources().getString(R.string.movie_title_popular));
            }
            else
                showErrorMessage();
        }
        else if (itemThatWasClicked == R.id.top_rated){
            if (!isErrorOccured) {
                makeAPIRequest(getString(R.string.sort_by_vote));
                setTitle(getResources().getString(R.string.movie_title_top_rated));
            }
            else
                showErrorMessage();
        }
        else if(itemThatWasClicked == R.id.favorite){
            startLoader(null);
            setTitle(getResources().getString(R.string.movie_title_favorite));
        }
        return super.onOptionsItemSelected(item);
    }

    private void startLoader(Bundle bundle){
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, bundle, this);
        //mGridMovieAdapter.swapCursor(data);
    }

    private void makeAPIRequest(String searchCriteria) {
        //Call the Build URl function
        URL urlMain = Networksutils.buildURLAPI(searchCriteria);
        Log.d(TAG, urlMain.toString()); //TODO to delete
        new APIRequest().execute(urlMain);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            static final String TAG = "AsyntaskLoader";
            // Initialize a Cursor, this will hold all the favorite movies of the user
            Cursor mFavoriteMovies = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mFavoriteMovies != null) {
                    Log.d(TAG, "Entering OnstartLoading mfavorites :  " + mFavoriteMovies);
                    // Delivers any previously loaded data immediately
                    deliverResult(mFavoriteMovies);
                } else {
                    Log.d(TAG, "Entering OnstartLoading mfavorites null :  " + mFavoriteMovies);
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data
                Log.e("loadInbackground:", "cursor value before querying: " + 0);
                // Query and load all movies data in the background;
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                    if (cursor != null)
                        Log.e("loadInbackground:", "Number of rows return by the Cursor" + cursor.getCount());
                    Log.e("loadInbackground:", "cursor value from query: " + cursor);
                    return cursor;
                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                Log.d("AsyntaskLoader", "Cursor" + data);
                if(data == null)
                    Log.d("AsyntaskLoader", "Cursor not load Properly, val : " + data);
                mFavoriteMovies = data;
                super.deliverResult(data);
            }
        };
    }
    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mGridMovieAdapter !=null){
            /*if (data !=null)
                mGridMovieAdapter.clear();*/
            mGridMovieAdapter.swapCursor(data, true);
            Toast.makeText(this, "1. mGridMovieAdapter : " + mGridMovieAdapter, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mGridMovieAdapter.swapCursor(null, false);
    }

    public class APIRequest extends AsyncTask<URL, Void, List<Movies>> {
        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }*/

        protected List<Movies> doInBackground(URL... urls) {
            String jsonResponse;
            List<Movies> movies = null;
            jsonResponse = Networksutils.getResponseFromHttpUrl(urls[0]);
            Log.e(TAG, jsonResponse + "json response");//TODO delete
            if(jsonResponse !=null) {
                //Parsing the Api Json Response
                 movies = JsonUtils.parseJsonUtils(jsonResponse);
            }else{
                isErrorOccured = true;
                // finish();
            }
            setmArrayMovies(movies);
            return movies;
        }

        protected void onPostExecute(List<Movies> result) {
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
            //Log.e(TAG, result.toString());
            if (result != null && !result.isEmpty()) {
                showGrid(result);
            } else {
                showErrorMessage();
            }

        }
    }


    private void showErrorMessage() {
        mTextView.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show();
    }

    private void showGrid(List<Movies> result) {
        mGridView.setVisibility(View.VISIBLE);
        mGridMovieAdapter = new GridAdapterMovie(this, result);
        Log.d(TAG, String.valueOf(result.size()));
        mGridView.setAdapter(mGridMovieAdapter);
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
        detailIntent.putExtra(Constant.MOVIE_EXTRA, itemAtPosition);
        detailIntent.putExtra(Constant.MOVIE_POSITION_EXTRA, position);
        startActivity(detailIntent);
    }

    private void setmArrayMovies(List<Movies> mArrayMovies) {
        this.mArrayMovies = mArrayMovies;
    }

}
