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

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

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

        ingredientRef = FirebaseDatabase
                .getInstance("https://dishcraftjava-77bc5-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Ingredient");
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
            String[] ingredients = new String[selectedItems.size()];
            for (int i = 0; i < selectedItems.size(); i++) {
                ingredients[i] = selectedItems.get(i).getName();
            }

            generateRecipe(ingredients, recipe -> {
                if (recipe != null) {
                    Intent intent = new Intent(GenerateRecipeIngredientSelection.this, RecipeDetails.class);
                    intent.putExtra("Ingredients", ingredients);
                    intent.putExtra("output", recipe);
                    Log.d("GenerateRecipe", "Recipe generated successfully: " + recipe);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Failed to generate recipe. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
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
                                R.drawable.ic_stock_food
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

    private void generateRecipe(String[] ingredients, RecipeCallback callback) {
        GenerativeModel gen = new GenerativeModel("gemini-1.5-flash", "AIzaSyC9YMIzvpKyObnG2B88irYaa0SOa5Ho2lE");
        GenerativeModelFutures model = GenerativeModelFutures.from(gen);

        Content content = new Content.Builder()
                .addText("Generate only the name of the recipe and detailed steps to cook the recipe using the following ingredients "+ Arrays.toString(ingredients) + " and separate each line with exactly one newline")
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                callback.onRecipeGenerated(resultText); // Pass the result to the callback
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("GenerateRecipe", "Failed to generate recipe: " + t.getMessage());
                callback.onRecipeGenerated(null); // Pass null to indicate failure
            }
        }, this.getMainExecutor());
    }
}