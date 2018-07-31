package com.example.maymoneyapp.movie_app_version1.model;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maymoneyapp.movie_app_version1.MainActivity;
import com.example.maymoneyapp.movie_app_version1.R;
import com.example.maymoneyapp.movie_app_version1.Utils.Constant;
import com.example.maymoneyapp.movie_app_version1.Utils.JsonUtils;
import com.example.maymoneyapp.movie_app_version1.Utils.Networksutils;
import com.example.maymoneyapp.movie_app_version1.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roger Nengwe on 22/07/2018.
 */

public class DetailActivity extends AppCompatActivity{
    private static final String TAG = DetailActivity.class.getSimpleName();
    private TextView mTitle;
    private TextView mUserRating;
    private TextView mReleaseDate;
    private TextView mOverview;
    private TextView mTvCurrentMenuItem; //The menu item selected (Review or Trailers).
    private ListView mReview;
    private List<String> mListReview;
    private ListView mTrailersListView;
    private Movies mMovieDetails;
    private ImageView mMovieImageThumbnail;
    private List<String> mTrailersList;
    private boolean isErrorOccuredDetail = false;
    private boolean mFavorite = true; //A variable use to favorite or unfavorite a movie.


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        // Instantiate
        mTitle = findViewById(R.id.tv_title);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mUserRating = findViewById(R.id.tv_rating);
        mOverview = findViewById(R.id.tv_overview);
        mMovieImageThumbnail = findViewById(R.id.movie_poster_detail);
        mTvCurrentMenuItem = findViewById(R.id.tv_label_selected);
        mReview = findViewById(R.id.lv_reviews);
        mTrailersListView = findViewById(R.id.lv_trailers);

        // Get the Movie Object
        Intent intent = getIntent();

        // Handle Rotation change
        if(savedInstanceState == null){
            Log.d(TAG, "+++++++++++++GOGOGOG");
            mMovieDetails = intent.getParcelableExtra(Constant.MOVIE_EXTRA);
            assert mMovieDetails == null;
            makeRequestAPIMovies(getString(R.string.show_trailers));//Show trailers by default.
        }else{
            mMovieDetails = savedInstanceState.getParcelable(Constant.PARCELABLE_MOVIE_DETAIL_KEY);
            mTrailersList = savedInstanceState.getStringArrayList(Constant.MOVIE_TRAILER_KEY);
            mListReview = savedInstanceState.getStringArrayList(Constant.MOVIE_REVIEWS_KEY);
            if (mTrailersList == null)
                showDetailsReviews(mListReview);
            else if (mListReview == null)
                showDetailsTrailer(mTrailersList);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constant.PARCELABLE_MOVIE_DETAIL_KEY, mMovieDetails);
        outState.putStringArrayList(Constant.MOVIE_TRAILER_KEY, (ArrayList<String>)mTrailersList);
        outState.putStringArrayList(Constant.MOVIE_REVIEWS_KEY, (ArrayList<String>)mListReview);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_reviews_trailers, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClickId = item.getItemId();
        if (R.id.menu_item_review == itemClickId){
            makeRequestAPIMovies(getString(R.string.show_reviews));
        }else if (R.id.menu_item_trailer == itemClickId){
            makeRequestAPIMovies(getString(R.string.show_trailers));
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Function to handle Click of the Button Mark as Favorite in the detail Screen.
     * @param view: The view button that has been clicked
     * */
    public void onClickMarkAsFavorite(View view) {
        ContentValues contentValues = new ContentValues();
        Button btn = (Button)view;
        if (mMovieDetails != null ) {
            if (mFavorite) {
                //stack data in the content value
                mFavorite = false;
                view.setBackgroundColor(getResources().getColor(R.color.green_light));
                btn.setText(getString(R.string.mark_as_unfavorite_btn));
                Log.d(TAG, "value of ID : " + mMovieDetails.getmMovieId());
                contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovieDetails.getmMovieId());
                contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, mMovieDetails.getmMovieTitle());
                //contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER, mTrailersList);

                //Insert data in the database using a content resolver
                Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
                if (uri !=null)
                    Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
            }else{
                mFavorite  = true;
                btn.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                
            }

        }
    }

    //===Asyntask for Trailers
    private class MovieDetailAPITrailerRequest extends AsyncTask<URL, Void, List<String>>{

        @Override
        protected List<String> doInBackground(URL... urls) {
            String jsonResultReview, jsonResultVideos;

            List<String> trailersList =  new ArrayList<>();
            if (urls[0] != null) {
                jsonResultVideos = Networksutils.getResponseFromHttpUrl(urls[0]);
                Log.e(TAG, "video" + jsonResultVideos);
                //Handling Trailer Videos
                if (jsonResultVideos != null && !jsonResultVideos.equals("")) {
                    mTrailersList = JsonUtils.parseJsonDetailTrailers(jsonResultVideos);
                    setmTrailersList(mTrailersList);
                    Log.e(TAG, mTrailersList.toString());
                } else {
                    isErrorOccuredDetail = true;
                    showErrorMessage();
                }
            }
            return mTrailersList;
        }

        @Override
        protected void onPostExecute(List<String> trailer) {
            super.onPostExecute(trailer);
            showDetailsTrailer(trailer);
        }
    }
    //Asyntask for REVIEW
    private class MovieDetailAPIReviewsRequest extends AsyncTask<URL, Void, List<String>>{

        @Override
        protected List<String> doInBackground(URL... urls) {
            String jsonResultReview, jsonResultVideos;

            List<String> trailersList =  new ArrayList<>();
            List<String> review;
            if (urls[0] != null) {
                jsonResultReview = Networksutils.getResponseFromHttpUrl(urls[0]);
                Log.e(TAG, "review" + jsonResultReview);
                if (jsonResultReview != null && !jsonResultReview.equals("")) {
                    mListReview = JsonUtils.parseJsonDetailMovieReview(jsonResultReview);
                    //String [] splitReview = review.split("");
                    setReview(mListReview);
                    for(int i = 0;i<mListReview.size();i++)
                        Log.e(TAG, mListReview.get(i) + "bbbbbb");
                } else {
                    isErrorOccuredDetail = true;
                    showErrorMessage();
                }
            }

            return mListReview;
        }

        @Override
        protected void onPostExecute(List<String> reviews) {
            super.onPostExecute(reviews);
            //populateUI();
            showDetailsReviews(reviews);
        }
    }

    //====Helper methods
    private void showDetailsTrailer(List<String> trailerList) {
        populateUI();
        mReview.setVisibility(View.GONE);
        mTrailersListView.setVisibility(View.VISIBLE);
        mTvCurrentMenuItem.setText(getString(R.string.trailer_label));
        ArrayAdapter<String> arrayAdapterTrailers = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, trailerList);
        mTrailersListView.setAdapter(arrayAdapterTrailers);
        mTrailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                performSearchTrailer((String)adapterView.getItemAtPosition(position));
            }
        });
    }

    private void showDetailsReviews(List<String> reviewList) {
        mTrailersListView.setVisibility(View.GONE);
        mReview.setVisibility(View.VISIBLE);
        mTvCurrentMenuItem.setText(getString(R.string.label_reviews));
        ArrayAdapter<String> arrayAdapterReview = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reviewList);
        mReview.setAdapter(arrayAdapterReview);
    }

    private void performSearchTrailer(String query){
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    private void showErrorMessage() {
        Toast.makeText(getApplicationContext(),getString(R.string.error_connection_problem), Toast.LENGTH_LONG).show();
    }

    public void setmTrailersList(List<String> mTrailersList) {
        this.mTrailersList = mTrailersList;
    }

    private void populateUI() {
        Log.d(TAG, mMovieDetails.getmRealeaseDate());
        Log.d(TAG, mMovieDetails.getmMovieImage());
        Log.d(TAG, mMovieDetails.getmMovieOverview());
        Log.d(TAG, mMovieDetails.getmUserRating());
        Log.d(TAG, mMovieDetails.getmMovieTitle());
        Log.d(TAG, ""+mMovieDetails.getmMovieId());


        //TODO Check the nullability of the different class
        mReleaseDate.setText(mMovieDetails.getmRealeaseDate());
        mTitle.setText(mMovieDetails.getmMovieTitle());
        mUserRating.setText(mMovieDetails.getmUserRating());
        mOverview.setText(mMovieDetails.getmMovieOverview());

        //Adding the image using Picasso.
        Picasso.with(getBaseContext()).load(mMovieDetails.getmMovieImage()).into(mMovieImageThumbnail);

    }
    private void setReview( List<String> reviews){
        mListReview = reviews;
    }

    /**
     * @param requestType : It represent the type of request(Review or trailer) trigger by te user.
     * */
    private void makeRequestAPIMovies(String requestType) {
        int movieID=-100 ;
        ArrayList<URL> urls = new ArrayList<>();
        URL urlReview = null;
        URL urlVideos = null;
        URL[] url = null;
        if(mMovieDetails !=null) {
            movieID = mMovieDetails.getmMovieId();
            Log.e(TAG, "movieID : "+ movieID);
            if (movieID >0) {
                urlReview = Networksutils.buildURLDetail(Constant.REVIEW_PATH, movieID);
                urlVideos = Networksutils.buildURLDetail(Constant.VIDEO_PATH, movieID);
                if (urlReview!=null && urlVideos!=null) {
                    urls.add(urlReview);
                    urls.add(urlVideos);
                    //Convert the urls to an Array to pass to the Asyntask
                    try {
                        url = (URL[]) urls.toArray(new URL[urls.size()]);
                    }catch (ClassCastException e){
                        showErrorMessage();
                        e.printStackTrace();
                    }
                    if (requestType.equalsIgnoreCase(getString(R.string.show_trailers)))
                        new MovieDetailAPITrailerRequest().execute(urlVideos);
                    else if (requestType.equalsIgnoreCase(getString(R.string.show_reviews)))
                        new MovieDetailAPIReviewsRequest().execute(urlReview);
                    else
                        showErrorMessage();
                }
            }
        }
    }

}
