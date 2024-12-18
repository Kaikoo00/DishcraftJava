package com.example.dishcraftjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        String[] ingredients = intent.getStringArrayExtra("Ingredients");
        String output = intent.getStringExtra("Recipe");
        String[] lines = output.split("\\n");

        // Remove excess newlines (empty strings) by filtering
        ArrayList<String> filteredLines = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                filteredLines.add(line.trim()); // Add non-empty, trimmed lines
            }
        }

        // Convert back to array if needed
        String[] steps = filteredLines.toArray(new String[0]);
        String foodName = steps[0];
        String [] finalSteps = Arrays.copyOfRange(steps, 1, steps.length);
        ImageButton buttonBookmark = findViewById(R.id.button_bookmark);

        buttonBookmark.setOnClickListener(v -> {
            FoodItem recipe = new FoodItem(2);
            recipe.setName(foodName);
            recipe.setSteps(Arrays.asList(finalSteps));
            recipe.setIngredients(Arrays.asList(ingredients));
            recipe.saveToFirebase(new FoodItem.OnFoodItemSavedCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.d("Recipe", message);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("Recipe", errorMessage);
                }
            });
            Toast.makeText(this, "Recipe bookmarked!", Toast.LENGTH_SHORT).show();
        });

        TextView textRecipeName = findViewById(R.id.text_recipe_name);
        textRecipeName.setText(foodName);

        // Inisialisasi Spinner Ingredients
        Spinner spinnerIngredients = findViewById(R.id.spinner_ingredients);
        spinnerIngredients.setClickable(false);
        assert ingredients != null;
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item_multiline, ingredients);
        ingredientsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_multiline);
        spinnerIngredients.setAdapter(ingredientsAdapter);

        // Inisialisasi Spinner Steps
        Spinner spinnerSteps = findViewById(R.id.spinner_steps);
        spinnerSteps.setClickable(false);
        ArrayAdapter<String> stepsAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item_multiline, finalSteps);
        stepsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_multiline);
        spinnerSteps.setAdapter(stepsAdapter);

    }
}