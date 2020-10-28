package com.iconic_dev.telleriumfarms.db.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by manuelchris-ogar on 24/10/2020.
 */
@Entity(tableName = "farmer")
public class Farmer implements Serializable {


    public String base_url;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;


    public ArrayList<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<String> coordinates) {
        this.coordinates = coordinates;
    }

    private ArrayList<String> coordinates  = new ArrayList<>();


    private String farmName;


    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmerName) {
        this.farmName = farmerName;
    }

    public String getFarmLocation() {
        return farmLocation;
    }

    public void setFarmLocation(String farmLocation) {
        this.farmLocation = farmLocation;
    }

    private String farmLocation;


    @SerializedName("farmer_id")
    @Expose
    private String farmerId;
    @SerializedName("reg_no")
    @Expose
    private String regNo;
    @SerializedName("bvn")
    @Expose
    private String bvn;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("middle_name")
    @Expose
    private String middleName;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("occupation")
    @Expose
    private String occupation;
    @SerializedName("marital_status")
    @Expose
    private String maritalStatus;
    @SerializedName("spouse_name")
    @Expose
    private String spouseName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("lga")
    @Expose
    private String lga;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("email_address")
    @Expose
    private String emailAddress;
    @SerializedName("id_type")
    @Expose
    private String idType;
    @SerializedName("id_no")
    @Expose
    private String idNo;
    @SerializedName("issue_date")
    @Expose
    private String issueDate;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;
    @SerializedName("id_image")
    @Expose
    private String idImage;
    @SerializedName("passport_photo")
    @Expose
    private String passportPhoto;
    @SerializedName("fingerprint")
    @Expose
    private String fingerprint;

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getPassportPhoto() {
        return passportPhoto;
    }

    public void setPassportPhoto(String passportPhoto) {
        this.passportPhoto = passportPhoto;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }




    public static DiffUtil.ItemCallback<Farmer> DIFF_CALLBACK = new DiffUtil.ItemCallback<Farmer>() {
        @Override
        public boolean areItemsTheSame(@NonNull Farmer shopsData, @NonNull Farmer t1) {
            return shopsData.id == t1.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Farmer shopsData, @NonNull Farmer t1) {
            return shopsData.equals(t1);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Farmer farmer = (Farmer) obj;
        return farmer.farmerId.equalsIgnoreCase(this.farmerId);
    }

    @Override
    public String toString() {
        return "Farmer{" +
                "base_url='" + base_url + '\'' +
                ", id=" + id +
                ", coordinates=" + coordinates +
                ", farmName='" + farmName + '\'' +
                ", farmLocation='" + farmLocation + '\'' +
                ", farmerId='" + farmerId + '\'' +
                ", regNo='" + regNo + '\'' +
                ", bvn='" + bvn + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", surname='" + surname + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", nationality='" + nationality + '\'' +
                ", occupation='" + occupation + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", spouseName='" + spouseName + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", lga='" + lga + '\'' +
                ", state='" + state + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", idType='" + idType + '\'' +
                ", idNo='" + idNo + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", idImage='" + idImage + '\'' +
                ", passportPhoto='" + passportPhoto + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                '}';
    }
}
