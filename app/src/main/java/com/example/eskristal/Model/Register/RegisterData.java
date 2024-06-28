package com.example.eskristal.Model.Register;

import com.google.gson.annotations.SerializedName;

public class RegisterData {

    @SerializedName("name")
    private String name;

    @SerializedName("username")
    private String username;

    @SerializedName("level")
    private String level;
    @SerializedName("password")
    private String password;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setLevel(String level){
        this.level = level;
    }

    public String getLevel(){
        return level;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

}