package com.example.myapplication.ModelClass;

public class Chicken {
    private String breed;
    private String age;
    private String birthdate;
    private String vitamins;

    public Chicken() {
        // Default constructor required for Firebase
    }

    public Chicken(String breed, String age, String birthdate, String vitamins) {
        this.breed = breed;
        this.age = age;
        this.birthdate = birthdate;
        this.vitamins = vitamins;
    }

    // Getters and setters (optional for Firebase but useful for retrieval)
    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getVitamins() {
        return vitamins;
    }

    public void setVitamins(String vitamins) {
        this.vitamins = vitamins;
    }
}
