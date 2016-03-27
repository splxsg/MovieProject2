package com.example.blues.movieapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.blues.movieapp.data.MovieContract.MovieEntry;

import org.json.JSONException;

import java.util.Vector;

/**
 * Created by Blues on 23/03/2016.
 */
public class FetchFavData extends AsyncTask<Void, Void, Void> {

    private String JSONSTR;
    private Context context;

    private final String LOG_TAG = FetchFavData.class.getSimpleName();
    private String movieinfo(String jsonstr, String info_name) throws JSONException {
                return perference.getMovieInfoFromJSON(jsonstr, info_name);
    }


    public FetchFavData(String jSONSTR, Context mContext) {
        this.JSONSTR = jSONSTR;
        this.context = mContext;
    }

    private void getMovieDataFromJson()
            throws JSONException {
        Uri inserteduri;
        try {

            Vector<ContentValues> cVVector = new Vector<ContentValues>(1);
            ContentValues movieValues = new ContentValues();

            movieValues.put(MovieEntry.COLUMN_MOVIE_ID, movieinfo(JSONSTR, "id"));
            movieValues.put(MovieEntry.COLUMN_MOVIE_NAME, movieinfo(JSONSTR, "original_title"));
            movieValues.put(MovieEntry.COLUMN_MOVIE_RELEASEDATE, movieinfo(JSONSTR, "release_date"));
            movieValues.put(MovieEntry.COLUMN_MOVIE_Rate, movieinfo(JSONSTR, "vote_average"));
            movieValues.put(MovieEntry.COLUMN_MOVIE_synopsis, movieinfo(JSONSTR, "overview"));
            movieValues.put(MovieEntry.COLUMN_MOVIE_Poster, movieinfo(JSONSTR, "poster_path"));
            movieValues.put(MovieEntry.COLUMN_MOVIE_favourite, "Y");

            cVVector.add(movieValues);
            int inserted = 0;
            inserteduri = context.getContentResolver().insert(MovieEntry.CONTENT_URI,  movieValues);
            if(ContentUris.parseId(inserteduri) != -1)
            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");

        }catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void...Params) {
        try {
            Log.v("fetchfav","123");
            getMovieDataFromJson();
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();


        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        perference.showToaster(context,"Added Favourite Movie!");
    }


}