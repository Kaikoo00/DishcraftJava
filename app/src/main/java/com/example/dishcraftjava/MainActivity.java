package com.example.dishcraftjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button viewIngredientsButton;
    Button viewRecipesButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewIngredientsButton = findViewById(R.id.mainViewIngredientsButton);
        viewIngredientsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, IngredientList.class);
            startActivity(intent);
        });
        viewRecipesButton = findViewById(R.id.mainViewRecipesButton);
        viewRecipesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeList.class);
            startActivity(intent);
        });
    }
    public static ArrayList<RVItem> initData(){
        ArrayList<RVItem> RVItemList = new ArrayList<>();
        RVItemList.add(new RVItem("Salt", "Vegetarian Friendly", R.drawable.ic_salt));
        RVItemList.add(new RVItem("Water", "Vegetarian Friendly", R.drawable.ic_water));
        RVItemList.add(new RVItem("Chicken (boneless)", "Non-Vegetarian", R.drawable.ic_chicken));
        RVItemList.add(new RVItem("Pepper", "Vegetarian Friendly", R.drawable.ic_placeholder));
        RVItemList.add(new RVItem("Garlic", "Vegetarian Friendly", R.drawable.ic_placeholder));
        return RVItemList;
    }
}