package com.example.smartpantry;

public class Recipe {
    public int id;
    public String name, ingredients, steps;
    public Recipe(int id, String name, String ingredients, String steps) {
        this.id=id; this.name=name; this.ingredients=ingredients; this.steps=steps;
    }
}
