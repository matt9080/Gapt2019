package com.example.myapplication;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Activities {
    private String age, id, name, shortDescription;
    private List<String> steps_type, steps_text, steps_image, questions;


    public Activities(){
        //empty constructor
    }




    public Activities(List<String> steps_type, List<String> steps_text, List<String> steps_image, List<String> questions, String age, String id, String name, String shortDescription){

        this.age = age;
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.steps_type = steps_type;
        this.steps_text = steps_text;
        this.steps_image = steps_image;
        this.questions = questions;
    }

    public String getName() {

        return name;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public String getAge() {
        return age;
    }

    public String getShortDescription() {

        return shortDescription;
    }

    public String getID() {
        return id;
    }

    public List<String> getSteps_type() {
        return steps_type;
    }

    public List<String> getSteps_text() {
        return steps_text;
    }

    public List<String> getSteps_image() {
        return steps_image;
    }



}
