package com.example.dishcraftjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FoodItem {
    private String name;
    private String ingredients;
    private String steps;
    private boolean isVegan;
    private String imageFileName; // Local image file name

    // Firebase reference
    private DatabaseReference databaseRef;

    // Constructor
    public FoodItem(int type) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://dishcraftjava-default-rtdb.asia-southeast1.firebasedatabase.app");
        if(type==1){
            this.databaseRef = database.getReference("Ingredient");
        }
        else if(type==2){
            this.databaseRef = database.getReference("Recipe");
        }
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    // Save image locally
    public void saveImageLocally(Context context, Bitmap image) {
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(context.getFilesDir(), fileName);

        try (FileOutputStream out = new FileOutputStream(file)) {
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            this.imageFileName = fileName;
        } catch (IOException e) {
            Log.e("FoodItem", "Failed to save image locally: " + e.getMessage());
        }
    }

    // Load image from local storage
    public Bitmap loadImageLocally(Context context) {
        if (imageFileName != null) {
            File file = new File(context.getFilesDir(), imageFileName);
            if (file.exists()) {
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        }

        // Return a default image if the file is not found
        return BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_menu_gallery);
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

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    // Callback interface
    public interface OnFoodItemSavedCallback {
        void onSuccess(String message);

        void onFailure(String errorMessage);
    }
}
