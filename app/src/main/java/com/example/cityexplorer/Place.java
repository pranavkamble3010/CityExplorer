package com.example.cityexplorer;

import com.google.android.gms.maps.model.LatLng;

public class Place {

    String name;
    LatLng latlng;
    String imageUrl;

    public Place() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", latlng=" + latlng +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
