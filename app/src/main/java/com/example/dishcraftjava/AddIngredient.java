package com.example.dishcraftjava;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
            boolean veganValue;
            String ingredientName = ingredientNameEV.getText().toString();
            /*TODO Add Picture Storing Method*/
            if ((veganRG.getCheckedRadioButtonId() == R.id.addIngredientRB_Yes)) {
                veganValue = true;
            } else {
                veganValue = false;
            }

            FoodItem newItem = new FoodItem(1);
            newItem.setName(ingredientName);
            newItem.setVegan(veganValue);
            newItem.saveToFirebase(new FoodItem.OnFoodItemSavedCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.d("Ingredient", message);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("Ingredient", errorMessage);
                }
            });
            finish();
        });
    }
}