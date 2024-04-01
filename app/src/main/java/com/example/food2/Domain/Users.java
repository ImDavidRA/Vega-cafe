package com.example.food2.Domain;

import java.io.Serializable;

public class Users implements Serializable {

    final String UserPicPath_base = "https://firebasestorage.googleapis.com/v0/b/vega-cafe.appspot.com/o/userPic.png?alt=media&token=ffde8d31-71ed-4c4a-8d34-0effa269038e";
    private String Id;
    private String Name;
    private String LastName;
    private int Nivel;
    private String UserPicPath;
    private String Email;
    private String Uid;

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public Users() {
    }

    public Users(String name, String lastName, String email, String id, String uid) {

        name = name.substring(0,1).toUpperCase()+name.substring(1);
        lastName = lastName.substring(0,1).toUpperCase()+lastName.substring(1);;

        Name = name;
        LastName = lastName;
        Email = email;
        UserPicPath = UserPicPath_base;
        Id = id;
        Nivel = 0;
        Uid = uid;
    }


    public int getNivel() {
        return Nivel;
    }


    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserPicPath() {
        return UserPicPath;
    }

    public void setUserPicPath(String userPicPath) {
        UserPicPath = userPicPath;
    }
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
}
