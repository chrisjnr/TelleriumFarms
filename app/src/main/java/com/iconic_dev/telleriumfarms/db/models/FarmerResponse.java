package com.iconic_dev.telleriumfarms.db.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by manuelchris-ogar on 25/10/2020.
 */
public class FarmerResponse {



    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }



    public static class Data {

        @SerializedName("farmers")
        @Expose
        private List<Farmer> farmers = null;
        @SerializedName("totalRec")
        @Expose
        private Integer totalRec;
        @SerializedName("imageBaseUrl")
        @Expose
        private String imageBaseUrl;

        public List<Farmer> getFarmers() {
            return farmers;
        }

        public void setFarmers(List<Farmer> farmers) {
            this.farmers = farmers;
        }

        public Integer getTotalRec() {
            return totalRec;
        }

        public void setTotalRec(Integer totalRec) {
            this.totalRec = totalRec;
        }

        public String getImageBaseUrl() {
            return imageBaseUrl;
        }

        public void setImageBaseUrl(String imageBaseUrl) {
            this.imageBaseUrl = imageBaseUrl;
        }

    }
}
