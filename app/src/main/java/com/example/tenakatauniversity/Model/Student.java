package com.example.tenakatauniversity.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "students")
public class Student {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "age")
    private int age;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "maritalStatus")
    private String maritalStatus;

    @ColumnInfo(name = "height")
    private Float height;

    @ColumnInfo(name = "longitude")
    private String longitude;

    @ColumnInfo(name = "latitude")
    private String latitude;

    @ColumnInfo(name = "testResults")
    private int testResults;

    @ColumnInfo(name = "country")
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public int getAdmissibility() {
        return admissibility;
    }

    public void setAdmissibility(int admissibility) {
        this.admissibility = admissibility;
    }

    @ColumnInfo(name = "admissibility")
    private int admissibility;

    private boolean isCountryKenya;

    //@ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    //private Byte [] image;
    private byte[] image;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getTestResults() {
        return testResults;
    }

    public void setTestResults(int testResults) {
        this.testResults = testResults;
    }

    public boolean isCountryKenya() {
        return isCountryKenya;
    }

    public void setCountryKenya(boolean countryKenya) {
        isCountryKenya = countryKenya;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
