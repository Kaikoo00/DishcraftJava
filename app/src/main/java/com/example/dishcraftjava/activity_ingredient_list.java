package com.example.dishcraftjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class activity_ingredient_list extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingredient_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView backButton = findViewById(R.id.ingredientListBackButton);
        backButton.setOnClickListener(v -> {
            finish();
        });

        TextView addNewIngredientButtonTV = findViewById(R.id.ingredientListAddNewIngredientButtonTV);
        addNewIngredientButtonTV.setOnClickListener(v -> {
            Intent intent = new Intent(activity_ingredient_list.this, AddIngredient.class);
            startActivity(intent);
        });

        List<RVItem> RVItemList = new ArrayList<>();
        RVItemList.add(new RVItem("Salt", "Vegetarian Friendly", R.drawable.ic_salt));
        RVItemList.add(new RVItem("Water", "Vegetarian Friendly", R.drawable.ic_water));
        RVItemList.add(new RVItem("Chicken (boneless)", "Non-Vegetarian", R.drawable.ic_chicken));
        RVItemList.add(new RVItem("Pepper", "Vegetarian Friendly", R.drawable.ic_placeholder));
        RVItemList.add(new RVItem("Garlic", "Vegetarian Friendly", R.drawable.ic_placeholder));

        RecyclerView rvIngredients = findViewById(R.id.ingredientListRV);
        RVAdapter adapter = new RVAdapter(RVItemList);
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rvIngredients.setAdapter(adapter);
    }
}