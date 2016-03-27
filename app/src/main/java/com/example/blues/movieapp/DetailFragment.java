package com.example.blues.movieapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ShareActionProvider.OnShareTargetSelectedListener;

import com.example.blues.movieapp.data.MovieContract;
import com.squareup.picasso.Picasso;

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
 * Created by Blues on 3/26/2016.
 */
public class DetailFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor> {


    static private String MovieID;
    static private FetchMovieReview MovieReview;
    static private FetchTrailer MovieTrailer;
    static private FetchFavData FavData;
    static private String JSONSTR;
    static private View rootView;
    public ShareActionProvider mShareActionProvider;
    static final public String MOVIE_SHARE_HASHTAG = " #Movie App";
    static final public String MOVIE_SHARE_HEAD = "Check the trailer: ";
    private static final int DETAIL_LOADER = 0;
    Intent shareIntent;

    public DetailFragment(){
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        MovieReview = new FetchMovieReview(rootView);
        Bundle arguments = getArguments();

        ImageView imageView = (ImageView)rootView.findViewById(R.id.detail_image);
        //  Intent intent = getActivity().getIntent();
        //if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
        //  JSONSTR = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (arguments != null) {
            JSONSTR = arguments.getString("mdata");
        }
        else
        {
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                JSONSTR = intent.getStringExtra(Intent.EXTRA_TEXT);
            }
        }
        Log.v("second choice",JSONSTR);
        try{
            MovieID = perference.getMovieInfoFromJSON(JSONSTR,"id") ;
            ((TextView) rootView.findViewById(R.id.detail_title)).setText("Title: " + perference.getMovieInfoFromJSON(JSONSTR, "original_title") + " (" +perference.getMovieInfoFromJSON(JSONSTR, "release_date")+")");
            //  ((TextView) rootView.findViewById(R.id.detail_year)).setText("Release on: " + perference.getMovieInfoFromJSON(JSONSTR, "release_date"));
            ((TextView) rootView.findViewById(R.id.avg_rate)).setText("Rate: " + perference.getMovieInfoFromJSON(JSONSTR, "vote_average") + "/10");
            ((TextView) rootView.findViewById(R.id.detail_text)).setText("Synopsis: " + perference.getMovieInfoFromJSON(JSONSTR, "overview"));
            Picasso.with(getActivity())
                    .load(getPosterUri(perference.getMovieInfoFromJSON(JSONSTR, "poster_path")).toString())
                    .into(imageView);
        }
        catch (JSONException e) {
        }
        final Button trailerbtn1 = (Button) rootView.findViewById(R.id.trailer1);
        final Button trailerbtn2 = (Button) rootView.findViewById(R.id.trailer2);
        final Button trailerbtn3 = (Button) rootView.findViewById(R.id.trailer3);
        trailerbtn1.setVisibility(View.GONE);
        trailerbtn2.setVisibility(View.GONE);
        trailerbtn3.setVisibility(View.GONE);
        perference.setSharemovietrailer(null);
        MovieReview.execute(MovieID);
        MovieTrailer = new FetchTrailer(rootView,getActivity());
        MovieTrailer.execute(MovieID);
        final ToggleButton favbtn = (ToggleButton) rootView.findViewById(R.id.Favbtn);
        if(checkFav())
            favbtn.setChecked(true);
        favbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                if(!favbtn.isChecked())
                    deleteFav();
                else {
                    FavData = new FetchFavData(JSONSTR, getActivity());
                    FavData.execute();
                }
            }
        });
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
       getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    private boolean checkFav()
    {
        Cursor FavCursor = getActivity().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{MovieID},
                null);
        if(FavCursor.moveToFirst())
            return true;
        return false;

    }


    private void deleteFav()
    {
        int rowsdelete = getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{MovieID});
        Log.v("delete", rowsdelete + "");
        perference.showToaster(getActivity(), "Deleted Favourite Movie!");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {return null;}


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //if (mShareActionProvider != null) {
          //  mShareActionProvider.setShareIntent(createShareMovieTrailerIntent());
    //    }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        inflater.inflate(R.menu.menu_share, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        //mShareActionProvider.setShareIntent(createShareMovieTrailerIntent());
       // mShareActionProvider.setOnShareTargetSelectedListener(new onShareTargetSelectedListener(){
        mShareActionProvider.setShareIntent(createShareMovieTrailerIntent());
    //return super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    private Intent createShareMovieTrailerIntent() {
        shareIntent = new Intent(Intent.ACTION_SEND);
        //shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, perference.getSharemovietrailer());
        return shareIntent;
    }


    private Uri getPosterUri(String Poster_path)
    {
        final String MOVIE_DATABASE_URL = "http://image.tmdb.org/t/p/w780"+Poster_path;
        Uri builtUri = Uri.parse(MOVIE_DATABASE_URL).buildUpon().build();
        return builtUri;
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }



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
                        shareIntent.putExtra(Intent.EXTRA_TEXT, MOVIE_SHARE_HEAD + " https://www.youtube.com/watch?v=" + TrailerID1+ MOVIE_SHARE_HASHTAG);
                        trailerbtn1.setVisibility(View.VISIBLE);
                        trailerbtn1.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + TrailerID1));
                                context.startActivity(browserIntent);
                            }
                        });

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





}







