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

    public Movies(){}
    public Movies(String imageName, String movieTitle, String overview){
        //TODO Add the additional parameters of the function in future implementation
        mMovieImage = imageName;
        mMovieTitle = movieTitle;
        mMovieOverview = overview;
    }

    protected Movies(Parcel in) {
        mMovieTitle = in.readString();
        mMovieOverview = in.readString();
        mMovieImage = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mMovieImage);
        parcel.writeString(mMovieTitle);
        parcel.writeString(mMovieOverview);

    }
}
