package com.example.dishcraftjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GenerateRecipeIngredientSelection extends AppCompatActivity {
    ImageView backButton;
    Button generateRecipeButton;
    TextView addNewIngredientButton;
    EditText searchIngredient;
    RecyclerView rvIngredients;
    RVAdapter adapter;
    ArrayList<RVItem> RVItemList = new ArrayList<>();

    private DatabaseReference ingredientRef;

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

        // Initialize Trial Data

        ingredientRef = FirebaseDatabase.getInstance("https://dishcraftjava-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Ingredient");
        // Initialize Views
        backButton = findViewById(R.id.generateRecipeIngredientListBackButton);
        addNewIngredientButton = findViewById(R.id.generateRecipeIngredientListAddNewIngredientButtonTV);
        generateRecipeButton = findViewById(R.id.generateRecipeIngredientListGenerateRecipeButton);
        rvIngredients = findViewById(R.id.generateRecipeIngredientListRV);
        searchIngredient = findViewById(R.id.generateRecipeIgredientListSearchEV);
        adapter = new RVAdapter(RVItemList, 2);
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rvIngredients.setAdapter(adapter);
        loadIngredients();
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
    private void loadIngredients() {
        ingredientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(RVItemList != null) RVItemList.clear();
                 // Clear the list to avoid duplicates
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodItem ingredient = snapshot.getValue(FoodItem.class);
                    if (ingredient != null) {
                        // Convert FoodItem to RVItem
                        String veganStatus;
                        if (ingredient.isVegan()) veganStatus = "Vegan";
                        else veganStatus = "Non-Vegan";
                        RVItem rvItem = new RVItem(
                                ingredient.getName(),
                                veganStatus,
                                R.drawable.ic_chicken
                        );
                        RVItemList.add(rvItem);
                    }
                }

                // Notify the adapter about data changes
                adapter.setList(RVItemList);
                adapter.notifyDataSetChanged();
                Log.d("IngredientList", "Loaded ingredients: " + RVItemList.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("IngredientList", "Failed to load ingredients: " + databaseError.getMessage());
            }
        });
    }
}