package com.panca_nugraha.percoobaan9;

public class User {
    private String photo;
    private String name;
    private String type;
    public String  getType(){
        return type;
    }
    public String getPhoto(){
        return photo;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPhoto(String photo){
        this.photo = photo;
    }
    public void setType(String type){
        this.type = type;
    }
}
