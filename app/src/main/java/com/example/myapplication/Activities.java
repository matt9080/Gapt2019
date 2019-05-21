package com.example.myapplication;

public class Activities {
    private String age;
    private String materials;
    private String steps;
    private String name;
    private String shortDescription;

    public Activities(){
        //empty constructor
    }

    public Activities(String age, String materials, String name, String shortDescription, String steps){
        this.age = age;
        this.materials = materials;
        this.name = name;
        this.shortDescription = shortDescription;
        this.steps = steps;
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

    public String getMaterials() {
        return materials;
    }

    public String getSteps() {
        return steps;
    }
}
