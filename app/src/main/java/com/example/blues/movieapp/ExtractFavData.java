package com.example.blues.movieapp;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.example.blues.movieapp.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Blues on 3/24/2016.
 */
public class ExtractFavData extends AsyncTask<Void, Void, Integer> {
    private Context context;
    private final String LOG_TAG = ExtractFavData.class.getSimpleName();
    int FavMovieAmount;
    JSONObject FavMovieJson = new JSONObject();
    JSONObject[] movieJSONObject;
    ExtractFavData(Context mcontext){this.context=mcontext;}


    private void UpdatemovieJSONObject()
            throws JSONException {
        try {
            final String js_RESULT = "results";
            JSONArray jsonarray = FavMovieJson.getJSONArray(js_RESULT);

            Log.v("favarray",jsonarray.toString());

                int original_length = FavMovieAmount;
            movieJSONObject = new JSONObject[original_length];              //assign temp space for swapping
                for (int i = 0; i < original_length; i++) {
                    movieJSONObject[i] = jsonarray.getJSONObject(i);       //put original data back
                }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error ", e);
        }
    }


    @Override
        protected Integer doInBackground(Void... params) {
        String mName, mID, mRate, mDate, mSynopsis, mFav, mPoster;
        JSONArray mjsonarray = new JSONArray();
        int colName, colID, colRate, colDate, colSynopsis, colFav, colPoster;
        Cursor movieCursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        Log.v("getcount", movieCursor.getCount()+"");
        if (movieCursor.moveToFirst()) {


            colName = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_NAME);
            colID = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            colDate = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASEDATE);
            colRate = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_Rate);
            colSynopsis = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_synopsis);
            colPoster = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_Poster);
            colFav = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_favourite);
            FavMovieAmount = movieCursor.getCount();

            try {
                while (!movieCursor.isAfterLast()) {
                    mID = movieCursor.getString(colID);
                    mName = movieCursor.getString(colName);
                    mDate = movieCursor.getString(colDate);
                    mRate = movieCursor.getString(colRate);
                    mSynopsis = movieCursor.getString(colSynopsis);
                    mPoster = movieCursor.getString(colPoster);
                    mFav = movieCursor.getString(colFav);


                    JSONObject mjsonobj = new JSONObject();
                    mjsonobj.put("id", mID);
                    mjsonobj.put("original_title", mName);
                    mjsonobj.put("release_date", mDate);
                    mjsonobj.put("vote_average", mRate);
                    mjsonobj.put("overview", mSynopsis);
                    mjsonobj.put("poster_path", mPoster);
                    mjsonobj.put("Favourite", mFav);
                    mjsonarray.put(mjsonobj);
                    movieCursor.moveToNext();
                }
                FavMovieJson.put("results", mjsonarray);
                FavMovieJson.getJSONArray("results").toString();
            } catch (JSONException e) {
            }

        }
        else {
            FavMovieAmount = 0;
        }

return FavMovieAmount;
    }

    protected void onPostExecute(Integer favmount) {

        if (favmount > 0) {
            try{
                UpdatemovieJSONObject();
                FragmentMovie.movieImageAdapter.setJSON(movieJSONObject);
                Log.v(LOG_TAG, "JSON results" + movieJSONObject.toString());
                FragmentMovie.movieImageAdapter.notifyDataSetChanged();
            } catch(JSONException e)
            {}

            }
        else
        {Log.v("testamount",FavMovieAmount+"");perference.showToaster(context,"No Fav data found");}

    }

    }
