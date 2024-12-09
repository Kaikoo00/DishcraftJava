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

public class IngredientList extends AppCompatActivity {
    ImageView backButton;
    TextView addNewIngredientButtonTV;
    EditText searchIngredientEV;
    RecyclerView rvIngredients;
    RVAdapter adapter;

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

        // Initialize Trial Data
        ArrayList<RVItem> RVItemList = MainActivity.initData();

        // Initialize Views Used in this Activity
        backButton = findViewById(R.id.ingredientListBackButton);
        addNewIngredientButtonTV = findViewById(R.id.ingredientListAddNewIngredientButtonTV);
        rvIngredients = findViewById(R.id.ingredientListRV);
        adapter = new RVAdapter(RVItemList, 1);
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rvIngredients.setAdapter(adapter);
        searchIngredientEV = findViewById(R.id.ingredientListSearchEV);

        backButton.setOnClickListener(v -> {
            finish();
        });

        addNewIngredientButtonTV.setOnClickListener(v -> {
            Intent intent = new Intent(IngredientList.this, AddIngredient.class);
            startActivity(intent);
        });

        searchIngredientEV.setOnEditorActionListener((v, actionId, event) -> {
            String searchText = searchIngredientEV.getText().toString();
            ArrayList<RVItem> filteredList = new ArrayList<>();
            for(RVItem item: RVItemList) {
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

