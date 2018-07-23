package com.example.maymoneyapp.movie_app_version1.model;

/**
 * Created by Roger Nengwe on 22/07/2018.
 */

public class Movies {
    private String mMovieTitle;
    private String mMovieOverview; //Reviews of the Movie.
    private String mMovieImage; //Poster path field of the "TheMovieDb" API, in the format {imageName}.jpg

    public Movies(){}
    public Movies(String imageName, String movieTitle, String overview){
        //TODO Add the additional parameters of the function in future implementation
        mMovieImage = imageName;
        mMovieTitle = movieTitle;
        mMovieOverview = overview;
    }

    public String getmMovieTitle() {
        return mMovieTitle;
    }

    public String getmMovieOverview() {
        return mMovieOverview;
    }

    public String getmMovieImage(){
        return mMovieImage;
    }
}
