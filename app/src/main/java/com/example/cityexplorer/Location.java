package com.example.cityexplorer;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Location {
    private String description;
    private String id;
    private String placeId;
    private List<Location> places;
    private LatLng latLng;
    private String tripTitle;

    public Location() {
        this.places = new ArrayList<Location>();
    }

    public Location(String description, String id, String placeId, String reference, String mainText,
                    String secondaryText) {
        this.description = description;
        this.id = id;
        this.placeId = placeId;
        this.places = new ArrayList<Location>();
    }


    public Location(HashMap<String, Object> hashMap) {
        this.description = hashMap.get("description").toString();
        this.id = hashMap.get("id").toString();
        this.tripTitle = hashMap.get("tripTitle").toString();
        this.placeId = hashMap.get("placeId").toString();
        this.places = (List<Location>) hashMap.get("places");
        HashMap<String,Object> latLngHashmap = (HashMap<String, Object>) hashMap.get("latLng");
        this.latLng = new LatLng((Double) latLngHashmap.get("latitude"),
                                            (Double) latLngHashmap.get("longitude"));
    }


    public String getTripTitle() {
        return this.tripTitle;
    }

    public void setTripTitle(String tripTitle) {
        this.tripTitle = tripTitle;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public List<Location> getPlaces() {
        return places;
    }

    public void setPlaces(List<Location> places) {
        this.places = places;
    }

    public Location addPlace(Location place){
        this.places.add(place);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }


    @Override
    public String toString() {
        return "Location{" +
                "description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", placeId='" + placeId + '\'' +
                ", latLng=" + latLng +
                '}';
    }
}
