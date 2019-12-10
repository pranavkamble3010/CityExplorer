package com.example.cityexplorer;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewTripsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ViewTripsFragment extends Fragment implements TripsAdapter.TripCardInteractionListener{

    /**Private attributes**/

    private OnFragmentInteractionListener mListener;

    private Button btn_add_trips;
    private RecyclerView rv_allTrips;
    private List<Location> trips;
    private TripsAdapter tripsAdapter;

    /**Private attributes**/

    /**Private methods**/

    private void init(){
        btn_add_trips = getActivity().findViewById(R.id.btn_add_trip);

        trips = new ArrayList<>();
        btn_add_trips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAddTripsClicked();
            }
        });

        rv_allTrips = getActivity().findViewById(R.id.rv_allTrips);
        populateRecyclerView();
        
    }

    private void populateRecyclerView() {

        tripsAdapter = new TripsAdapter(trips,
                this);

        rv_allTrips.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_allTrips.setAdapter(tripsAdapter);
        populateTrips();

    }

    private void populateTrips() {

        trips.removeAll(trips);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trips")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc: task.getResult()) {
                                trips.add(new Location((HashMap<String, Object>)doc.getData()));
                            }
                            Log.d("PopulateTrips", "onComplete: "+trips.size());
                            tripsAdapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(getContext(), "Error loading trips!", Toast.LENGTH_SHORT).show();
                            Log.d("ViewProfileFrag", "onComplete: Error getting trips data");
                        }
                    }
                });


    }

    /**Private methods**/

    /**editable areas**/

    public ViewTripsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Location Explorer - All trips");
        init();
    }

    /**editable areas**/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_trips, container, false);
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

    @Override
    public void onAddPlacesClick(LatLng latLng) {

        mListener.onAddPlacesClicked(latLng);
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
        void onAddTripsClicked();
        void onAddPlacesClicked(LatLng latLng);
    }
}
