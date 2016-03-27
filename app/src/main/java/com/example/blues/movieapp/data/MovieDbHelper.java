package com.example.blues.movieapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.blues.movieapp.data.MovieContract.MovieEntry;
/**
 * Created by Blues on 23/03/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 8;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

 @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){

     final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
             MovieEntry._ID + " INTEGER PRIMARY KEY," +
             MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL," +
             MovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL," +
             MovieEntry.COLUMN_MOVIE_RELEASEDATE + " TEXT NOT NULL, " +
             MovieEntry.COLUMN_MOVIE_Rate + " TEXT NOT NULL, " +
             MovieEntry.COLUMN_MOVIE_synopsis + " TEXT NOT NULL, " +
             MovieEntry.COLUMN_MOVIE_Poster + " TEXT NOT NULL, "+
             MovieEntry.COLUMN_MOVIE_favourite + " TEXT NOT NULL " +
             " );";
     sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
     Log.v("MovieDBHelper","create! ");
 }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
