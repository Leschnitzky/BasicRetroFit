package com.example.moovittext.jsonmodels;

import com.google.gson.annotations.SerializedName;

public class FlickerMain {
    @SerializedName("photos")
    private FlickerPhotos photos;

    @SerializedName("stat")
    private String stat;

    public FlickerMain(FlickerPhotos photos, String stat) {
        this.photos = photos;
        this.stat = stat;
    }

    public FlickerPhotos getPhotos() {
        return photos;
    }

    public void setPhotos(FlickerPhotos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
