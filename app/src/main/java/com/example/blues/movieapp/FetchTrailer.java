
package com.example.blues.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

  public class FetchTrailer extends AsyncTask<String, Void, String> {
        private final String LOG_TAG = FetchTrailer.class.getSimpleName();
        View rootView;
        private JSONObject[] movieTrailerJSONObject;
        private int TrailerAmount;

        private Context context;

        public FetchTrailer(View rView, Context mContext) {
            rootView = rView;
            context = mContext;
        }

        private void MovieTrailerJSONObject(String JSONstr)
                throws JSONException {

            try {
                final String js_RESULT = "results";
                JSONArray jsonarray = new JSONObject(JSONstr).getJSONArray(js_RESULT);

                movieTrailerJSONObject = new JSONObject[jsonarray.length()];

                Log.v("MOVIETRAIL",jsonarray.toString());
                for (int i = 0; i < jsonarray.length(); i++) {
                    movieTrailerJSONObject[i] = jsonarray.getJSONObject(i);
                    // if (perference.getMovieInfoFromJSON(movieTrailerJSONObject[j].toString(), "type") == "Trailer")
                    //   j++;
                }

                TrailerAmount = jsonarray.length();
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
            }
        }


        protected String doInBackground(String[] params) {

            Log.v(LOG_TAG, "PARAMS LENGTH " + params.length);
            if (params.length == 0) {
                return null;
            }
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String MovieVideoJsonStr = null;
            try {
// Construct the URL for requesting data from themoviedb via legal API
                final String MOVIE_DATABASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";
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
                MovieVideoJsonStr = buffer.toString();

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
                MovieVideoJsonStr = buffer.toString();

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
            return MovieVideoJsonStr;
        }


        @Override
        protected void onPostExecute(String result) {
            final String TrailerID1,TrailerID2,TrailerID3;
            if (result != null) {


                try {
                    Log.v("trailexe",result);
                    MovieTrailerJSONObject(result);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error ", e);
                }
                try {
                    final Button trailerbtn1 = (Button) rootView.findViewById(R.id.trailer1);
                    final Button trailerbtn2 = (Button) rootView.findViewById(R.id.trailer2);
                    final Button trailerbtn3 = (Button) rootView.findViewById(R.id.trailer3);
                    if(TrailerAmount>=1) {
                        //DetailFragment.resetloader();

                      //  mShareActionProvider.setShareIntent(createShareMovieTrailerIntent());
                       // getmShareActionProvider().setShareIntent();
                        // DetailActivity.mShareActionProvider.setShareIntent(createShareMovieTrailerIntent());
                        Log.v("trailexe1 ",movieTrailerJSONObject[0].toString());
                        TrailerID1 = perference.getMovieInfoFromJSON(movieTrailerJSONObject[0].toString(), "key");
                        trailerbtn1.setVisibility(View.VISIBLE);
                        trailerbtn1.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + TrailerID1));
                                context.startActivity(browserIntent);
                            }
                        });
                        perference.setSharemovietrailer("https://www.youtube.com/watch?v=" + TrailerID1);
                        Log.d("testfetchtrailer",perference.getSharemovietrailer());
                    }
                    if(TrailerAmount>=2) {
                        Log.v("trailexe2 ",movieTrailerJSONObject[1].toString());
                        TrailerID2 = perference.getMovieInfoFromJSON(movieTrailerJSONObject[1].toString(), "key");
                        trailerbtn2.setVisibility(View.VISIBLE);
                        trailerbtn2.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + TrailerID2));
                                context.startActivity(browserIntent);
                            }
                        });
                    }
                    if(TrailerAmount>=3) {
                        Log.v("trailexe3 ",movieTrailerJSONObject[2].toString());
                        TrailerID3 = perference.getMovieInfoFromJSON(movieTrailerJSONObject[2].toString(), "key");
                        trailerbtn3.setVisibility(View.VISIBLE);
                        trailerbtn3.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + TrailerID3));
                                context.startActivity(browserIntent);
                            }
                        });
                    }
                    if(TrailerAmount==0)
                        perference.showToaster(context,"No trailer is found");
                } catch (JSONException e) {
                }
            }




        }




    }