package com.example.maymoneyapp.movie_app_version1.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maymoneyapp.movie_app_version1.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Etudiant UPS on 22/07/2018.
 */

public class DetailActivity extends AppCompatActivity{
    private static final String TAG = DetailActivity.class.getSimpleName();
    private TextView mTitle;
    private TextView mUserRating;
    private TextView mReleaseDate;
    private TextView mOverview;
    private Movies mMovieDetails;
    private ImageView mMovieImageThumbnail;
    private final static String PARCELABLE_DETAIL_KEY = "saved_detail";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        // Instantiate
        mTitle = findViewById(R.id.tv_title);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mUserRating = findViewById(R.id.tv_rating);
        mOverview = findViewById(R.id.tv_overview);
        mMovieImageThumbnail = (ImageView)findViewById(R.id.movie_poster_detail);

        // Get the Movie Object
        Intent intent = getIntent();


        // Handle Rotation change
        if(savedInstanceState == null){
            mMovieDetails = intent.getParcelableExtra(MOVIE_EXTRA);
            if(mMovieDetails !=null){
                //Populate the UI
                populateUI();
            }
        }else
            mMovieDetails = savedInstanceState.getParcelable(PARCELABLE_DETAIL_KEY);
            populateUI();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(PARCELABLE_DETAIL_KEY, mMovieDetails);
        super.onSaveInstanceState(outState);
    }

    private void populateUI() {
        Log.d(TAG, mMovieDetails.getmRealeaseDate());
        Log.d(TAG, mMovieDetails.getmMovieImage());
        Log.d(TAG, mMovieDetails.getmMovieOverview());
        Log.d(TAG, mMovieDetails.getmUserRating());
        Log.d(TAG, mMovieDetails.getmMovieTitle());

        mReleaseDate.setText(mMovieDetails.getmRealeaseDate());
        mTitle.setText(mMovieDetails.getmMovieTitle());
        mUserRating.setText(mMovieDetails.getmUserRating());
        mOverview.setText(mMovieDetails.getmMovieOverview());
        //Adding the image using Picasso.
        Picasso.with(getBaseContext()).load(mMovieDetails.getmMovieImage()).into(mMovieImageThumbnail);
    }

    public final static String MOVIE_EXTRA = "movie_object";
    public final static String MOVIE_POSITION_EXTRA = "position_clicked";
}
