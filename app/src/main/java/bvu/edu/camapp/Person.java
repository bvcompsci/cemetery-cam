package bvu.edu.camapp;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Person {

    @SerializedName("birth_date")
    @Expose
    private String birthDate;
    @SerializedName("death_date")
    @Expose
    private String deathDate;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("headstone")
    @Expose
    private String headstone;

    @SerializedName("lat")
    @Expose
    private String lat;


    @SerializedName("lng")
    @Expose
    private String lng;


    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String[] getPersonInfo(){
        String[] personInfo = {id.toString(), firstName, lastName};
        return personInfo;
    }

    public String getLng() {
        return lng.trim();
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat.trim();
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getHeadstone() {
        return headstone;
    }

    public void setHeadstone(String headstone) {
        this.headstone = headstone;
    }

}
