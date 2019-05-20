package com.example.myapplication;

public class Activities {
    private String age;
    private String name;
    private String shortDescription;

    public Activities(){

    }
    public Activities(String Age, String name, String shortDescription){
        this.age = age;
        this.name = name;
        this.shortDescription = shortDescription;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getShortDescription() {
        return shortDescription;
    }
}
