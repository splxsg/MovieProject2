package com.example.blues.movieapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.blues.movieapp.data.MovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

/**
 * Created by Blues on 3/26/2016.
 */
public class Blankpage extends Fragment {

    // static private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.blankpage, container, false);
        return rootView;
    }
}
