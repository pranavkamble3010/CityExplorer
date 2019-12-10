package com.example.cityexplorer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class TripsAdapter extends  RecyclerView.Adapter<TripsAdapter.MyViewHolder>{

    private List<Location> resources;
    TripCardInteractionListener tripCardInteractionListener;


    public interface TripCardInteractionListener {
        void onAddPlacesClick(LatLng latLng);
    }

    public TripsAdapter(List<Location> resources,
                        TripCardInteractionListener tripCardInteractionListener){

        this.resources = resources;
        this.tripCardInteractionListener = tripCardInteractionListener;


    }

    @NonNull
    @Override
    public TripsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripsAdapter.MyViewHolder holder, final int position) {
        final MyViewHolder tempMyHolder = holder;

        //Log.d("onBindViewHolder", "onBindViewHolder: ");
        tempMyHolder.lbl_tripTitle.setText(resources.get(position).getTripTitle());
        tempMyHolder.lbl_cityName.setText(resources.get(position).getDescription());
        Button btn_addPlaces = tempMyHolder.btn_addNewPlace;

        btn_addPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripCardInteractionListener.onAddPlacesClick(resources.get(tempMyHolder.getLayoutPosition()).getLatLng());

            }
        });

    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final Context context;
        TextView lbl_tripTitle;
        TextView lbl_cityName;
        Button btn_addNewPlace;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            lbl_tripTitle = itemView.findViewById(R.id.lbl_tripTitle);
            lbl_cityName = itemView.findViewById(R.id.lbl_cityName);
            btn_addNewPlace = itemView.findViewById(R.id.btn_addPlaces);
        }
    }
}
