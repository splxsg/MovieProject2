package com.example.blues.movieapp;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;



public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private final String LOG_TAG = "ImageAdapter";
    private JSONObject[] movieJSONObject;
    public ImageAdapter(Context context) {
        this.mContext = context;

    }

    public void setJSON(JSONObject[] mJSONObject)
    {
        this.movieJSONObject = mJSONObject;
    }

    public JSONObject[] getJSON()
    {
        return movieJSONObject;
    }


    @Override
    public int getCount() {

        if (movieJSONObject != null)
            return movieJSONObject.length;
        else
            return 0;
    }

    @Override
    public String getItem(int position) {
        String s = null;
        if(movieJSONObject != null)
        {try {
            s = getPosterUri(movieJSONObject[position].getString("poster_path")).toString();

        } catch (JSONException e) {
        }
            return s;}
        else
            return null;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {

        ImageView imageView;



//            check to see if we have a view
        if (convertView == null) {

            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(0, 0, 0, 0);
//                no view - so create a new one

        } else {
//                use the recycled view object

            imageView = (ImageView) convertView;
            //textView = (TextView) convertView;
        }
        if((position == getCount()-3)&&perference.getCurrentsort()!="favourite")
        {
            FetchMovieGrid movieTask = new FetchMovieGrid();
          movieTask.execute(perference.getCurrentsort(), getCount()/perference.getMoviePerPage()+1+"");
        }
        String s = getItem(position);
        Picasso.with(mContext)
                .load(s)
                .placeholder(R.raw.placeholder)
                .into(imageView);
        return imageView;

    }
    private Uri getPosterUri(String location) {
        final String MOVIE_DATABASE_URL = "http://image.tmdb.org/t/p/w154" + location;
        Uri builtUri = Uri.parse(MOVIE_DATABASE_URL).buildUpon().build();
        return builtUri;
    }



}

