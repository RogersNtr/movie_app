package com.example.maymoneyapp.movie_app_version1.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Roger Nengwe on 22/07/2018.
 */

public class Movies implements Parcelable {
    private String mMovieTitle;
    private String mMovieOverview; //Reviews of the Movie.
    private String mMovieImage; //URL of the "TheMovieDb" API, to get an image.
    private String mUserRating;
    private String mRealeaseDate;

    public Movies(){}
    public Movies(String imageName, String movieTitle, String overview, String userRating, String releaseDate){
        //TODO Add the additional parameters of the function in future implementation
        mMovieImage = imageName;
        mMovieTitle = movieTitle;
        mMovieOverview = overview;
        mUserRating = userRating;
        mRealeaseDate = releaseDate;
    }

    private Movies(Parcel in) {
        mMovieImage = in.readString();
        mMovieTitle = in.readString();
        mMovieOverview = in.readString();
        mUserRating = in.readString();
        mRealeaseDate = in.readString();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel parcelIn) {
            return new Movies(parcelIn);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    public String getmMovieTitle() {
        return mMovieTitle;
    }

    public String getmMovieOverview() {
        return mMovieOverview;
    }

    public String getmMovieImage(){
        return mMovieImage;
    }

    public String getmUserRating() {
        return mUserRating;
    }

    public String getmRealeaseDate() {
        return mRealeaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mMovieImage);
        parcel.writeString(mMovieTitle);
        parcel.writeString(mMovieOverview);
        parcel.writeString(mUserRating);
        parcel.writeString(mRealeaseDate);

    }
}
