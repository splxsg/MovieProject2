package com.example.blues.movieapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Blues on 23/03/2016.
 */
public class FetchMovieReview extends AsyncTask<String, Void, String> {
private final String LOG_TAG = FetchMovieReview.class.getSimpleName();

    private View rootView;
    private int ReviewAmount;
    private JSONObject[] movieReviewJSONObject;

public FetchMovieReview(View rView){rootView= rView;}

        private void MovieReviewJSONObject(String JSONstr)

                throws JSONException {

                try {
                        final String js_RESULT = "results";
                        JSONArray jsonarray = new JSONObject(JSONstr).getJSONArray(js_RESULT);

                        movieReviewJSONObject = new JSONObject[jsonarray.length()];
                        for (int i = 0; i < jsonarray.length(); i++)
                                movieReviewJSONObject[i] = jsonarray.getJSONObject(i);
                        ReviewAmount = jsonarray.length();
                } catch (JSONException e) {
                        Log.e(LOG_TAG, "Error ", e);
                }


        }

      /*  private void storeID()
        {
            Log.d("storeID", "1111111111");
            ContentValues movieValue = new ContentValues();
            Log.d("storeID", "22222222222");

                Vector<ContentValues> cVVector = new Vector<ContentValues>(100);
                movieValue.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, "testID");
                movieValue.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, "testMOVIENAME");
                cVVector.add(movieValue);
                ContentValues[] cvArray = new ContentValues[2];
                cVVector.toArray(cvArray);
                int inserted;
                Log.d("storeID", cVVector.toString());
                inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI,cvArray);
                Log.d("intert SQL", "FetchWeatherTask Complete. " + inserted + " Inserted");

        }*/


@Override
protected String doInBackground(String[] params) {

        Log.v(LOG_TAG,"PARAMS LENGTH "+params.length);
        if (params.length == 0) {
        return null;
        }


        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String MovieReviewJsonStr = null;


        try {
// Construct the URL for requesting data from themoviedb via legal API
final String MOVIE_DATABASE_URL = "http://api.themoviedb.org/3/movie/"+params[0]+"/reviews";
final String APPID_PARAM = "api_key";
        Uri builtUri = Uri.parse(MOVIE_DATABASE_URL).buildUpon()
        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_DATABASE_API_KEY)
        .build();
        URL url = new URL(builtUri.toString());

        Log.v(LOG_TAG, "Built URI " + builtUri.toString());
        // Create the request to themoviedb, and open the connection
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        // Read the input stream into a String
        InputStream inputStream = urlConnection.getInputStream();
        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
        // Nothing to do.
        return null;
        }
        reader = new BufferedReader(new InputStreamReader(inputStream));
        MovieReviewJsonStr = buffer.toString();

        String line;
        while ((line = reader.readLine()) != null) {
        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
        // But it does make debugging a *lot* easier if you print out the completed
        // buffer for debugging.
        buffer.append(line + "\n");
        }

        if (buffer.length() == 0) {
        // Stream was empty.  No point in parsing.
        return null;
        }
        MovieReviewJsonStr = buffer.toString();
        Log.v(LOG_TAG, "Movie JSON String: " + MovieReviewJsonStr);
        } catch (IOException e) {
        Log.e(LOG_TAG, "Error ", e);
        // If the code didn't successfully get the movie data, there's no point in attemping
        // to parse it.
        return null;
        } finally {
        if (urlConnection != null) {
        urlConnection.disconnect();
        }
        if (reader != null) {
        try {
        reader.close();
        } catch (final IOException e) {
        Log.e(LOG_TAG, "Error closing stream", e);
        }
        }
        }
        //storeID();
        return MovieReviewJsonStr;
        }

@Override
protected void onPostExecute(String result) {
        if (result != null) {
        try {
        MovieReviewJSONObject(result);
        } catch (JSONException e) {
        Log.e(LOG_TAG, "Error ", e);
        }
        try {
        ((TextView) rootView.findViewById(R.id.review_text)).setText("Review:\n\n");
        for(int i=0;i<ReviewAmount;i++)
        {
        ((TextView) rootView.findViewById(R.id.review_text)).append("From: "+ perference.getMovieInfoFromJSON(movieReviewJSONObject[i].toString(), "author")+"\n");
        ((TextView) rootView.findViewById(R.id.review_text)).append(perference.getMovieInfoFromJSON(movieReviewJSONObject[i].toString(), "content")+"\n\n");
        }
        } catch (JSONException e) {
        }
        }
        }


        }