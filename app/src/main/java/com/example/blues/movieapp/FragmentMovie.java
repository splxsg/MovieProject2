package com.example.blues.movieapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONObject;

import javax.security.auth.callback.Callback;

/**
 * Created by Blues on 11/24/2015.
 */
public class FragmentMovie extends Fragment {
    static ImageAdapter movieImageAdapter;
    JSONObject[] movieJSONObject;
    View rootView;
    static GridView movieGridView;

    //int moviePerPage;
    // String currentSort = "";
    public FragmentMovie() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_menu, menu);
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String mdata);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //we save the system resource by ignoring the request of same rank option and toast the message
        int id = item.getItemId();
        Log.d("itemselected",id+"");
        if (id == R.id.action_pop) {
            if (perference.getCurrentsort() == "popularity") {
                perference.showToaster(getActivity(), "Already shown");
            } else {
                FetchMovieGrid movieTask = new FetchMovieGrid();
                perference.setCurrentsort("popularity");
                movieTask.execute(new String[]{"popularity", "1"});
            }
            return true;
        }
        if (id == R.id.action_re_date) {
            if (perference.getCurrentsort() == "vote_average") {
                perference.showToaster(getActivity(), "Already shown");
            } else {
                FetchMovieGrid movieTask = new FetchMovieGrid();
                perference.setCurrentsort("vote_average");
                movieTask.execute(new String[]{"vote_average", "1"});
            }
            return true;
        }
        if (id == R.id.action_fav)
            if (perference.getCurrentsort() == "favourite") {
                perference.showToaster(getActivity(), "Already shown");
            } else {
                ExtractFavData FavTask = new ExtractFavData(getActivity());
                perference.setCurrentsort("favourite");
                FavTask.execute();
            }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movieImageAdapter = new ImageAdapter(getActivity());
        movieJSONObject = null;
        movieGridView = (GridView) rootView.findViewById(R.id.movie_gridView);
        movieGridView.setAdapter(movieImageAdapter);
        FetchMovieGrid movieTask = new FetchMovieGrid();
        perference.setCurrentsort("popularity");
        movieTask.execute(new String[]{"popularity", "1"});

        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String LOG_TAG = "onItemClick_fragmentMovie";
                movieJSONObject = movieImageAdapter.getJSON();
                String moviedata = movieJSONObject[position].toString();

           Log.d("testdata", moviedata);
                ((Callback) getActivity()).onItemSelected(moviedata);



               // getFragmentManager().beginTransaction().replace(R.id.Detail_Movie, new DetailFragment())
                 //       .commit();
               //Intent intent = new Intent(getActivity(), DetailActivity.class)
                //        .putExtra(Intent.EXTRA_TEXT, moviedata);
                //startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


}