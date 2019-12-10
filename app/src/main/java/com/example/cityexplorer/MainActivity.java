package com.example.cityexplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements ViewTripsFragment.OnFragmentInteractionListener,
AddTripFragment.OnFragmentInteractionListener{
    private static final int REQ_LOAD_ALL_TRIPS_FRAGMENT = 201;
    private static final int REQ_LOAD_ADD_TRIP_FRAGMENT = 202;
    private static final int REQ_LOAD_ALL_PLACES_FRAGMENT = 203;


    //-------Private methods-----
    /**
     * Handler class
     * */

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {

            switch (msg.what) {
                case REQ_LOAD_ADD_TRIP_FRAGMENT:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,new AddTripFragment(),"add_trip_fragment")
                            .commit();
                    break;


                case REQ_LOAD_ALL_TRIPS_FRAGMENT:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,new ViewTripsFragment(),"view_trips_fragment")
                            .commit();
                    break;

                case REQ_LOAD_ALL_PLACES_FRAGMENT:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,new PlacesFragment((LatLng)msg.obj),"view_trips_fragment")
                            .commit();
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load all trips fragment at first
        Message msg = handler.obtainMessage(REQ_LOAD_ALL_TRIPS_FRAGMENT);
        msg.sendToTarget();

    }


    @Override
    public void onAddTripsClicked() {
        Message msg = handler.obtainMessage(REQ_LOAD_ADD_TRIP_FRAGMENT);
        msg.sendToTarget();
    }

    @Override
    public void onAddPlacesClicked(LatLng latLng) {
        Message msg = handler.obtainMessage(REQ_LOAD_ALL_PLACES_FRAGMENT);
        msg.obj = latLng;
        msg.sendToTarget();

    }

    @Override
    public void onTripAddedToDatabase() {
        Message msg = handler.obtainMessage(REQ_LOAD_ALL_TRIPS_FRAGMENT);
        msg.sendToTarget();
    }
}
