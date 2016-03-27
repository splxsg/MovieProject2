package com.example.blues.movieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements FragmentMovie.Callback {
    private boolean mTwoPane;

    private final String FRAGMENTMOVIE_TAG = "FMTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // if(findViewById(R.id.Detail_Movie) != null) {
          //  mTwoPane = true;
        if (findViewById(R.id.Detail_Movie) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Detail_Movie, new Blankpage())
                        .commit();

            }
        }
        else{
            mTwoPane = false;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.Fragment_Movie, new FragmentMovie())
                    .commit();
        }
     //   }
        //else{
       //     mTwoPane = false;
       // }
      /* if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.FragmentMovie, new FragmentMovie(), FRAGMENTMOVIE_TAG)
                    .commit();
        }*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


   /* @Override
    protected void onResume() {
        super.onResume();
        DetailFragment DF = (DetailFragment)getSupportFragmentManager().findFragmentById(R.id.Fragment_Movie)

        String location = Utility.getPreferredLocation(this);
        // update the location in our second pane using the fragment manager


        if (location != null && !location.equals(mLocation)) {
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if ( null != ff ) {
                ff.onLocationChanged();
            }
            mLocation = location;
        }
    }*/

   /* @Override
    protected void onResume() {
        super.onResume();
        String csort = perference.getCurrentsort();
        if (csort != Currentsort) {
            FragmentMovie FM = (FragmentMovie) getSupportFragmentManager().findFragmentByTag(FRAGMENTMOVIE_TAG);
            if(null != FM)
                FM.onSortChanged();
        }
    }*/


@Override
    public void onItemSelected(String mdata){
    if (mTwoPane) {
        // In two-pane mode, show the detail view in this activity by
        // adding or replacing the detail fragment using a
        // fragment transaction.
        Bundle args = new Bundle();
        args.putString("mdata",mdata);
          //args.putParcelable("mdata", contentUri);

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Detail_Movie,fragment).commit();


        //       R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
        //.commit();
    } else {
        Intent intent = new Intent(this, DetailActivity.class)
                .putExtra(Intent.EXTRA_TEXT, mdata);
        startActivity(intent);
    }
}




}
