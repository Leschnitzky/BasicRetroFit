package com.example.moovittext.jsonmodels;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class FlickerPhoto {

    @SerializedName("id")
    private Long id;
    @SerializedName("owner")
    private String owner;
    @SerializedName("secret")
    private String secret;
    @SerializedName("server")
    private Long server;
    @SerializedName("farm")
    private Long farm;
    @SerializedName("title")
    private String title;
    @SerializedName("ispublic")
    private Integer ispublic;
    @SerializedName("isfriend")
    private Integer isfriend;
    @SerializedName("isfamily")
    private Integer isfamily;
    @SerializedName("url_s")
    private String url_s;
    @SerializedName("height_s")
    private Integer height_s;
    @SerializedName("width_s")
    private Integer width_s;

    public FlickerPhoto(Long id, String owner, String secret, Long server, Long farm, String title, Integer ispublic, Integer isfriend, Integer isfamily, String url_s, Integer height_s, Integer width_s) {
        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.title = title;
        this.ispublic = ispublic;
        this.isfriend = isfriend;
        this.isfamily = isfamily;
        this.url_s = url_s;
        this.height_s = height_s;
        this.width_s = width_s;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getServer() {
        return server;
    }

    public void setServer(Long server) {
        this.server = server;
    }

    public Long getFarm() {
        return farm;
    }

    public void setFarm(Long farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIspublic() {
        return ispublic;
    }

    public void setIspublic(Integer ispublic) {
        this.ispublic = ispublic;
    }

    public Integer getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(Integer isfriend) {
        this.isfriend = isfriend;
    }

    public Integer getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(Integer isfamily) {
        this.isfamily = isfamily;
    }

    public String getUrl_s() {
        return url_s;
    }

    public void setUrl_s(String url_s) {
        this.url_s = url_s;
    }

    public Integer getHeight_s() {
        return height_s;
    }

    public void setHeight_s(Integer height_s) {
        this.height_s = height_s;
    }

    public Integer getWidth_s() {
        return width_s;
    }

    public void setWidth_s(Integer width_s) {
        this.width_s = width_s;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this.url_s != null){
            return this.url_s.equals(((FlickerPhoto)obj).url_s);
        }
        return false;
    }
}
