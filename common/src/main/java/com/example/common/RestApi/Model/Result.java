package com.example.common.RestApi.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("mag")
    @Expose
    private Double mag;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lokasyon")
    @Expose
    private String lokasyon;
    @SerializedName("depth")
    @Expose
    private Double depth;
    @SerializedName("coordinates")
    @Expose
    private List<Double> coordinates = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("rev")
    @Expose
    private Object rev;
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;
    @SerializedName("date_stamp")
    @Expose
    private String dateStamp;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("hash2")
    @Expose
    private String hash2;

    public Double getMag() {
        return mag;
    }

    public void setMag(Double mag) {
        this.mag = mag;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getLokasyon() {
        return lokasyon;
    }

    public void setLokasyon(String lokasyon) {
        this.lokasyon = lokasyon;
    }

    public Double getDepth() {
        return depth;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getRev() {
        return rev;
    }

    public void setRev(Object rev) {
        this.rev = rev;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash2() {
        return hash2;
    }

    public void setHash2(String hash2) {
        this.hash2 = hash2;
    }

}
