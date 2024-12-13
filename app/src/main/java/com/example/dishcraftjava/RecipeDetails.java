package com.example.dishcraftjava;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        // Data untuk Ingredients dan Steps
        String[] ingredients = {"Chicken", "Flour", "Garlic", "Chili"};
        String[] steps = {"Step 1: abc", "Step 2: cde", "Step 3: def", "Step 4: efg"};

        // Inisialisasi Spinner Ingredients
        Spinner spinnerIngredients = findViewById(R.id.spinner_ingredients);
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, ingredients);
        ingredientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIngredients.setAdapter(ingredientsAdapter);

        // Listener untuk Spinner Ingredients
        spinnerIngredients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedIngredient = parent.getItemAtPosition(position).toString();
                Toast.makeText(RecipeDetails.this, "Selected: " + selectedIngredient, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Inisialisasi Spinner Steps
        Spinner spinnerSteps = findViewById(R.id.spinner_steps);
        ArrayAdapter<String> stepsAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, steps);
        stepsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSteps.setAdapter(stepsAdapter);

        // Listener untuk Spinner Steps
        spinnerSteps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Do Nothing
//                String selectedStep = parent.getItemAtPosition(position).toString();
//                Toast.makeText(RecipeDetails.this, "Selected: " + selectedStep, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
}