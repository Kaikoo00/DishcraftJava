package com.example.dishcraftjava;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FoodItem {
    private String name;
    private List<String> ingredients;
    private List<String> steps;
    private boolean isVegan;

    // Firebase reference
    private DatabaseReference databaseRef;

    // Constructor
    public FoodItem(int type) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://dishcraftjava-77bc5-default-rtdb.asia-southeast1.firebasedatabase.app/");
        if(type==1){
            this.databaseRef = database.getReference("Ingredient");
        }
        else if(type==2){
            this.databaseRef = database.getReference("Recipe");
        }
    }

    public FoodItem(){

    }
    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }

    // Save data to Firebase
    public void saveToFirebase(OnFoodItemSavedCallback callback) {
        String key = databaseRef.push().getKey();
        if (key != null) {
            databaseRef.child(key).setValue(this)
                    .addOnSuccessListener(aVoid -> callback.onSuccess("Food item saved successfully."))
                    .addOnFailureListener(e -> callback.onFailure("Failed to save food item: " + e.getMessage()));
        } else {
            callback.onFailure("Failed to generate a unique database key.");
        }
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List <String> steps) {
        this.steps = steps;
    }

    // Callback interface
    public interface OnFoodItemSavedCallback {
        void onSuccess(String message);

        void onFailure(String errorMessage);
    }
}
