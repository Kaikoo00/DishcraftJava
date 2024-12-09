package com.example.dishcraftjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GenerateRecipeIngredientSelection extends AppCompatActivity {
    RVAdapter adapter;
    ArrayList<RVItem> RVItemList;
    TextView addNewIngredientButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_generate_recipe_ingredient_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RVItemList = IngredientList.initData();

        // Initialize Views
        ImageView backButton = findViewById(R.id.generateRecipeIngredientListBackButton);
        addNewIngredientButton = findViewById(R.id.generateRecipeIngredientListAddNewIngredientButtonTV);
        Button generateRecipeButton = findViewById(R.id.generateRecipeIngredientListGenerateRecipeButton);
        RecyclerView rvIngredients = findViewById(R.id.generateRecipeIngredientListRV);
        EditText searchIngredient = findViewById(R.id.generateRecipeIgredientListSearchEV);
        adapter = new RVAdapter(RVItemList, 2);
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rvIngredients.setAdapter(adapter);

        backButton.setOnClickListener(v -> {
            finish();
        });

        generateRecipeButton.setOnClickListener(v -> {
            ArrayList<RVItem> selectedItems = (ArrayList<RVItem>) adapter.getSelectedItems();
            Bundle ingredients = new Bundle();
            ingredients.putSerializable("ingredients", selectedItems);
            Intent intent = new Intent(GenerateRecipeIngredientSelection.this, RecipeDetails.class);
            intent.putExtras(ingredients);
            startActivity(intent);
        });

        addNewIngredientButton.setOnClickListener(v -> {
            Intent intent = new Intent(GenerateRecipeIngredientSelection.this, AddIngredient.class);
            startActivity(intent);
        });

        searchIngredient.setOnEditorActionListener((v, actionId, event) -> {
            String searchText = searchIngredient.getText().toString();
            ArrayList<RVItem> filteredList = new ArrayList<>();
            for(RVItem item: RVItemList){
                if(item.getName().toLowerCase().contains(searchText.toLowerCase())){
                    filteredList.add(item);
                }
            }

            if(filteredList.isEmpty()){
                Toast.makeText(this, "No item match", Toast.LENGTH_SHORT).show();
                adapter.setList(RVItemList);
            }
            else{
                adapter.setList(filteredList);
                adapter.notifyDataSetChanged();
            }
            return true;
        });
    }
}