package com.example.dishcraftjava;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.List;

public class RecipeList extends AppCompatActivity {
    RVAdapter adapter;
    ArrayList<RVItem> RVItemList;
    TextView addNewRecipeButtonTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RVItemList = IngredientList.initData();

        // Initialize Views
        ImageView backButton = findViewById(R.id.recipeListBackButton);
        addNewRecipeButtonTV = findViewById(R.id.recipeListAddNewRecipeButtonTV);
        RecyclerView rvIngredients = findViewById(R.id.recipeListRV);
        EditText searchRecipeEV = findViewById(R.id.recipeListSearchEV);
        adapter = new RVAdapter(RVItemList);
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rvIngredients.setAdapter(adapter);

        backButton.setOnClickListener(v -> {
            finish();
        });

        addNewRecipeButtonTV.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeList.this, GenerateRecipeIngredientSelection.class);
            startActivity(intent);
        });

        searchRecipeEV.setOnEditorActionListener((v, actionId, event) -> {
            String searchText = searchRecipeEV.getText().toString();
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