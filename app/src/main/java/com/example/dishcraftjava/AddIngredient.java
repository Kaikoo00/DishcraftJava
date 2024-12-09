package com.example.dishcraftjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddIngredient extends AppCompatActivity {
    ImageView backButton;
    Button addButton;
    EditText ingredientNameEV;
    RadioGroup veganRG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_ingredient);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ingredientNameEV = findViewById(R.id.addIngredientNameEV);
        veganRG = findViewById(R.id.addIngredientVeganRG);
        backButton = findViewById(R.id.addIngredientBackButton);
        addButton = findViewById(R.id.addIngredientAddButton);

        backButton.setOnClickListener(v -> {
            finish();
        });

        addButton.setOnClickListener(v -> {
            Intent addNewIngredient = new Intent(AddIngredient.this, IngredientList.class);
            Bundle newIngredientInfo = new Bundle();
            String veganValue;
            if ((veganRG.getCheckedRadioButtonId() == R.id.addIngredientRB_Yes)) {
                veganValue = "Vegan";
            } else {
                veganValue = "Non-Vegan";
            }
            newIngredientInfo.putString("name", ingredientNameEV.getText().toString());
            newIngredientInfo.putString("vegan", veganValue);
            /* TODO add picture input and saving method */
        });
    }
}