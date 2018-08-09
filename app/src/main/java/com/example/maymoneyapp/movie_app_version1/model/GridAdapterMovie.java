package com.example.maymoneyapp.movie_app_version1.model;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.maymoneyapp.movie_app_version1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Roger Nengwe on 09/07/2018.
 */

public class GridAdapterMovie extends ArrayAdapter<Movies>{

    private final static String TAG = GridAdapterMovie.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param moviePoster A List of Movies objects to display in the GridView
     */
    public GridAdapterMovie(@NonNull Context context, List<Movies> moviePoster) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, moviePoster);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Movies movie = getItem(position);
        //By doing this we re-inflate the view for the Cursor data
        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.

        boolean mIsFavoriteClicked = false;
        Log.d(TAG, "Enter getView  " + mIsFavoriteClicked);
        //if (!mIsFavoriteClicked) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.grid_view_item, parent, false);
            }

            ImageView posterImage = convertView.findViewById(R.id.image_movie);
            try {
                assert movie != null;
                Log.d(TAG, movie.getmMovieImage());
                Picasso.with(getContext())
                        .load(movie.getmMovieImage())
                        .into(posterImage);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        return convertView;
    }

}
