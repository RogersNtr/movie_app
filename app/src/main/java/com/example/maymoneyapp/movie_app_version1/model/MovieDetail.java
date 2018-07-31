package com.example.maymoneyapp.movie_app_version1.model;

/**
 * Created by Roger Nengwe on 26/07/2018.
 */

public class MovieDetail {
    private int mMovieDetailId;
    private String mDetailMovieTitle;
    private String mMovieAuthor;
    private String mReviewContent;
    private String mMovieTrailer;

    public MovieDetail(String reviewContent, int movieDetailId){
        mReviewContent = reviewContent;
        mMovieDetailId = movieDetailId;
    }
}
