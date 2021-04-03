package com.example.common.RestApi.db.entity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Quakes")
public class Quake {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "Hash")
    private String hash;
    @ColumnInfo(name = "Hash2")
    private String hash2;

    @ColumnInfo(name = "Mag")
    private Double mag;
    @ColumnInfo(name = "Lng")
    private Double lng;
    @ColumnInfo(name = "Lat")
    private Double lat;
    @ColumnInfo(name = "Lokasyon")
    private String lokasyon;
    @ColumnInfo(name = "Depth")
    private Double depth;
   /* @ColumnInfo(name = "Coordinates")
    private List<Double> coordinates ;*/
    @ColumnInfo(name = "Title")
    private String title;
   /* @ColumnInfo(name = "Rev")
    private Object rev;*/
    @ColumnInfo(name = "Timestamp")
    private Integer timestamp;
    @ColumnInfo(name = "Datestamp")
    private String dateStamp;
    @ColumnInfo(name = "Date")
    private String date;

    public Quake(String hash, String hash2, Double mag, Double lng, Double lat, String lokasyon, Double depth, String title, Integer timestamp, String dateStamp, String date ){
      //  this.coordinates = coordinates;
        this.date = date;
        this.dateStamp = dateStamp;
        this.depth = depth;
        this.lat = lat;
        this.lng = lng;
        this.lokasyon = lokasyon;
        this.title = title;
        this.hash = hash;
        this.hash2 = hash2;
        this.mag = mag;
    //    this.rev = rev;
        this.timestamp = timestamp;
    }

   /* public List<Double> getCoordinates() {
        return coordinates;
    }
*/
    public Double getDepth() {
        return depth;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public Double getMag() {
        return mag;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

   /* public Object getRev() {
        return rev;
    }
*/
    public String getDate() {
        return date;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public String getHash() {
        return hash;
    }

    public String getHash2() {
        return hash2;
    }

    public String getLokasyon() {
        return lokasyon;
    }

    public String getTitle() {
        return title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setHash2(String hash2) {
        this.hash2 = hash2;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void setLokasyon(String lokasyon) {
        this.lokasyon = lokasyon;
    }

    public void setMag(Double mag) {
        this.mag = mag;
    }

 /*   public void setRev(Object rev) {
        this.rev = rev;
    }*/

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
