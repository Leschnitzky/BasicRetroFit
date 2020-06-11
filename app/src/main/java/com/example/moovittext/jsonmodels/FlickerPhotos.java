package com.example.moovittext.jsonmodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FlickerPhotos {
    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int pages;

    @SerializedName("perpage")
    private int perpage;

    @SerializedName("total")
    private int total;

    @SerializedName("photo")
    private List<FlickerPhoto> photo;

    public FlickerPhotos(int page, int pages, int perpage, int total, List<FlickerPhoto> photo) {
        this.page = page;
        this.pages = pages;
        this.perpage = perpage;
        this.total = total;
        this.photo = photo;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<FlickerPhoto> getPhoto() {
        return photo;
    }

    public void setPhoto(List<FlickerPhoto> photo) {
        this.photo = photo;
    }
}
