package com.example.myapplication;

public class Crafts {
    private int Age;
    private String Name;
    private String shortDescription;

    public Crafts(){

    }
    public Crafts(int Age, String Name, String shortDescription){
        this.Age = Age;
        this.Name = Name;
        this.shortDescription = shortDescription;
    }

    public String getName() {
        return Name;
    }

    public int getAge() {
        return Age;
    }

    public String getShortDescription() {
        return shortDescription;
    }
}
