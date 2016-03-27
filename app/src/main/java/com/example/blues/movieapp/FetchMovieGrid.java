
package com.example.blues.movieapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FetchMovieGrid extends AsyncTask<String, Void, String> {
    private final String LOG_TAG = FetchMovieGrid.class.getSimpleName();
    private boolean sortChange = false; //cant use bool here, dont know why, and this is a flag to show if the sort type changed, if so, the adapter will go to the beginning
    private String currentSort = perference.getCurrentsort();
    private JSONObject[] movieJSONObject;

public void FetchMovieGrid(){}

    public void FetchMovieGrid(JSONObject[] movieJSONObject1){

    }



    private void UpdatemovieJSONObject(String JSONstr)
            throws JSONException {
        int moviePerPage;
        try {
            JSONObject jsonobject = new JSONObject(JSONstr);
            JSONObject[] tempjsonobject;

            final String js_RESULT = "results";

            JSONArray jsonarray = new JSONObject(JSONstr).getJSONArray(js_RESULT);

            if (movieJSONObject == null) {             //if app just start or request a new sort method, movieJSONObject will be initial
                moviePerPage = jsonarray.length();
                perference.setMoviePerPage(moviePerPage);
                movieJSONObject = new JSONObject[jsonarray.length()];
                for (int i = 0; i < jsonarray.length(); i++)
                    movieJSONObject[i] = jsonarray.getJSONObject(i);
            } else                                                    //To list more content under the same sort method
            {
                int original_length = movieJSONObject.length;
                tempjsonobject = movieJSONObject;              //assign temp space for swapping
                movieJSONObject = new JSONObject[jsonarray.length() + original_length];   //enlarge size of movieJSONObject
                for (int i = 0; i < original_length; i++) {
                    movieJSONObject[i] = tempjsonobject[i];       //put original data back
                }
                for (int i = 0; i < jsonarray.length(); i++)       //assign new data to movieJSONObject
                    movieJSONObject[i + original_length] = jsonarray.getJSONObject(i);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error ", e);
        }
    }

    @Override
    protected String doInBackground(String[] params) {
        Log.v(LOG_TAG, "PARAMS LENGTH " + params.length);


        if (params.length == 0) {
            return null;
        }
        if (currentSort != params[0])
            sortChange = true;
        currentSort = params[0];


        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String MovieJsonStr = null;


        try {
            // Construct the URL for requesting data from themoviedb via legal API
            final String MOVIE_DATABASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String Sort = params[0];
            final String PAGE_PARAM = "page";
            final String page = params[1];
            final String Rank = ".desc";
            final String APPID_PARAM = "api_key";
            Uri builtUri = Uri.parse(MOVIE_DATABASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, Sort + Rank)
                    .appendQueryParameter(PAGE_PARAM, page)
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
            MovieJsonStr = buffer.toString();

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
            MovieJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Movie JSON String: " + MovieJsonStr);
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
        if (params[1] == "1")  //if request the first page of sorting, it means we need to initial movieJSONObject
            movieJSONObject = null;
        else
        movieJSONObject = FragmentMovie.movieImageAdapter.getJSON();
        return MovieJsonStr;
    }

    //this is going to happen after the JSON data obtained.
    @Override
    protected void onPostExecute(String result) {

        if (result != null) {
            try {
                UpdatemovieJSONObject(result);  //update the movie infomation to movieJSONObject and notify the adapter data has changed
                FragmentMovie.movieImageAdapter.setJSON(movieJSONObject);
                Log.v(LOG_TAG, "JSON results" + movieJSONObject.toString());
                FragmentMovie.movieImageAdapter.notifyDataSetChanged();
               // perference.loadflag = true;
                if (sortChange)
                    FragmentMovie.movieGridView.setAdapter(FragmentMovie.movieImageAdapter);  //if the sort type changed by menu, gridview goes to top
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
            }
        }
    }
}

