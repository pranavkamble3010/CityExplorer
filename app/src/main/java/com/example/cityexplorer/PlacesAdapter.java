package com.example.cityexplorer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class PlacesAdapter extends  RecyclerView.Adapter<PlacesAdapter.MyViewHolder>{

    private List<Place> resources;
    TripCardInteractionListener tripCardInteractionListener;


    public interface TripCardInteractionListener {
        void onAddPlacesClick(String tripTitle);
    }

    public PlacesAdapter(List<Place> resources,
                         TripCardInteractionListener tripCardInteractionListener){

        this.resources = resources;
        this.tripCardInteractionListener = tripCardInteractionListener;


    }

    @NonNull
    @Override
    public PlacesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesAdapter.MyViewHolder holder, final int position) {
        final MyViewHolder tempMyHolder = holder;

        //Log.d("onBindViewHolder", "onBindViewHolder: ");
        /*tempMyHolder.lbl_tripTitle.setText(resources.get(position).getTripTitle());
        tempMyHolder.lbl_cityName.setText(resources.get(position).getDescription());
        Button btn_addPlaces = tempMyHolder.btn_addNewPlace;*/

        /*btn_addPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripCardInteractionListener.onAddPlacesClick(resources.get(tempMyHolder.getLayoutPosition()).getId());

            }
        });*/

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

            lbl_tripTitle = itemView.findViewById(R.id.lbl_placeName);
            lbl_cityName = itemView.findViewById(R.id.lbl_cityName);
            btn_addNewPlace = itemView.findViewById(R.id.btn_addPlaces);
        }
    }
}
