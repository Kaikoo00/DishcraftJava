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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecipeList extends AppCompatActivity {
    RVAdapter adapter;
    RecyclerView rvIngredients;
    ArrayList<RVItem> RVItemList;
    TextView addNewRecipeButtonTV;
    EditText searchRecipeEV;
    ImageView backButton;
    FirebaseDatabase database;
    DatabaseReference RecipeRef;

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

        database = FirebaseDatabase.getInstance("https://dishcraftjava-default-rtdb.asia-southeast1.firebasedatabase.app");
        RecipeRef = database.getReference("Recipe");

        backButton = findViewById(R.id.recipeListBackButton);
        addNewRecipeButtonTV = findViewById(R.id.recipeListAddNewRecipeButtonTV);
        rvIngredients = findViewById(R.id.recipeListRV);
        searchRecipeEV = findViewById(R.id.recipeListSearchEV);

        RVItemList = new ArrayList<>();
        adapter = new RVAdapter(RVItemList, 3);
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rvIngredients.setAdapter(adapter);

        // Read Data from Firebase and Populate RecyclerView
        RecipeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RVItemList.clear(); // Clear the list to avoid duplication
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming your data structure matches RVItem
                    String name = snapshot.child("name").getValue(String.class);
                    boolean vegan = snapshot.child("vegan").getValue(Boolean.class);
                    String description;
                    if(vegan) description = "Vegan";
                    else description = "Non-Vegan";
                    int imageResource = R.drawable.ic_stock_food;

                    // Add the item to the list
                    RVItemList.add(new RVItem(name, description, imageResource));
                }
                // Notify adapter about data changes
                adapter.setList(RVItemList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RecipeList.this, "Failed to load recipes: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
            for (RVItem item : RVItemList) {
                if (item.getName().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(item);
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No item matches", Toast.LENGTH_SHORT).show();
                adapter.setList(RVItemList);
            } else {
                adapter.setList(filteredList);
                adapter.notifyDataSetChanged();
            }
            return true;
        });
    }
}