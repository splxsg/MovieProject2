package com.example.blues.movieapp;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Blues on 22/03/2016.
 */
public class perference {
       private static String Currentsort = "";
    private static Boolean TrailerFlag = false;
    private static int moviePerPage;
private static String sharemovietrailer;


    public static String getCurrentsort(){
        return Currentsort;
    }

    public static void setCurrentsort(String s){
        perference.Currentsort=s;
    }

    public static void setTrailerFlag(Boolean f)
    {TrailerFlag = f;}

    public static Boolean getTrailerFlag()
    {
        return TrailerFlag;
    }

    public static String getMovieInfoFromJSON(String JSONSTR,String info_name)
            throws JSONException {
        JSONObject ReviewJson = new JSONObject(JSONSTR);
        return ReviewJson.getString(info_name);
    }

    public static void setMoviePerPage(int page)
    {moviePerPage = page;}
    public static int getMoviePerPage()
    {return moviePerPage;}


    public static void showToaster(Context context, CharSequence s)
    {
        CharSequence text = s;//"Add Fav Movie!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast  = Toast.makeText(context, text, duration);
        toast.show();
    }

    public static void setSharemovietrailer(String s)
    {
        sharemovietrailer = s;

    }
    public static String getSharemovietrailer()
    {
        return sharemovietrailer;
    }
}
