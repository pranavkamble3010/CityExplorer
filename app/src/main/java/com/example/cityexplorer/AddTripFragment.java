package com.example.cityexplorer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddTripFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddTripFragment extends Fragment {

    //----Private attributes----//

    private OnFragmentInteractionListener mListener;

    private EditText txt_tripName;
    private EditText txt_searchText;
    private Button btn_search;

    private static final String API_KEY = "AIzaSyDfuhIzQ9PwxqBFXhoQYIP88TMqoEVk2Dk";
    private final OkHttpClient client = new OkHttpClient();
    private List<Location> cities;
    private String[] citiesArray;
    private int selectedCityIndex;
    private Button btn_add_trip;
    private Location selectedLocation;

    //----Private attributes----//

    //----Private methods----//

    //----Private methods----//

    private void init() {

        txt_searchText = getActivity().findViewById(R.id.txt_addTrip_placeName);
        txt_tripName = getActivity().findViewById(R.id.txt_addTrip_tripName);
        btn_search = getActivity().findViewById(R.id.btn_addTrip_search);
        btn_add_trip = getActivity().findViewById(R.id.btn_addTrip_addTrip);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call API to get data
                callAutoCompleteAPI();
            }
        });

        btn_add_trip = getActivity().findViewById(R.id.btn_addTrip_addTrip);
        btn_add_trip.setEnabled(false);

        btn_add_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToTripToDatabase();
            }
        });
    }

    private void saveToTripToDatabase() {


        selectedLocation = cities.get(selectedCityIndex);

        //First get the city's geo-coordinates from the API and save them.
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/details/json?key="+API_KEY
                        +"&placeid="+ selectedLocation.getPlaceId())
                .build();

        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    //Log.d("InboxFrag", "onResponse: "+response.body().string());
                    String responseJSON = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseJSON);
                        JSONObject location = jsonObject.getJSONObject("result")
                                .getJSONObject("geometry")
                                .getJSONObject("location");
                        Log.d("geo-cordinates", "onResponse: " + location.toString());
                        LatLng latLng = new LatLng(location.getDouble("lat")
                                , location.getDouble("lng"));
                        selectedLocation.setLatLng(latLng);
                        Log.d("geo-city", "onResponse: " + selectedLocation.toString());
                        saveTripPhase2(selectedLocation);

                    } catch (Exception ex) {
                    }
                }
            });
        }
        catch (Exception ex){

        }


    }

    private void saveTripPhase2(Location location){
        location.setId(UUID.randomUUID().toString());
        location.setTripTitle(txt_tripName.getText().toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trips")
                .document(location.getId())
                .set(location)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //1. Save the trip
                        Log.d("add trip", "Trip saved successfully!"+cities.get(selectedCityIndex));
                        Toast.makeText(getContext(),
                                "Trip saved successfully!", Toast.LENGTH_SHORT).show();
                        mListener.onTripAddedToDatabase();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),
                                "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void callAutoCompleteAPI() {
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/autocomplete/json?key="+API_KEY
                        +"&types=(cities)&input="+txt_searchText.getText().toString())
                .build();

        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    //Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                    Log.d("callAutoCompleteAPI", "onFailure: Something went wrong!");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    //Log.d("InboxFrag", "onResponse: "+response.body().string());
                    cities = new ArrayList<>();
                    String responseJSON = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseJSON);
                        //Log.d("locationsResponse", "onResponse: " + jsonObject.toString());
                        JSONArray predictions = jsonObject.getJSONArray("predictions");
                        citiesArray = new String[predictions.length()];
                        for(int i=0;i<predictions.length();i++){
                            JSONObject prediction = predictions.getJSONObject(i);

                            Location location = new Location();
                            location.setPlaceId(prediction.getString("place_id"));
                            //location.setId(prediction.getString("id"));
                            location.setDescription(prediction.getString("description"));

                            cities.add(location);
                            citiesArray[i] = prediction.getString("description");
                        }

                        Log.d("arraylist", "onResponse: "+cities);
                        Message msg = handler.obtainMessage(200);
                        msg.sendToTarget();


                    } catch (Exception ex) {
                    }
                }
            });
        }
        catch (Exception ex){

        }

    }

    private void populateLocationsInAlertDialog(){

        //citiesArray = (String[]) cities.toArray();
        Log.d("in dialog pop", "populateLocationsInAlertDialog: "+citiesArray.toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose a Keyword")
                .setItems(citiesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txt_searchText.setText(cities.get(i).getDescription());
                        btn_add_trip.setEnabled(true);
                        selectedCityIndex = i;
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(@NonNull Message msg) {

            int code = msg.what;

            switch (code){

                case 200:
                    populateLocationsInAlertDialog();
                    break;
            }
        }
    };

    //----private methods----//


    //----editable area----//

    public AddTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add trip");
        init();
    }



    //----editable area----//


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_trip, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onTripAddedToDatabase();
    }
}
