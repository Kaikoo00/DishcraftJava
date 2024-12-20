package com.example.dishcraftjava;

import android.content.Intent;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.IngredientViewHolder> {
    /*
    Type variable for differentiating between ingredients and recipes
    and How the ingredients Holder behave (For Selecting or Displaying purpose
    1 For Displaying
    2 For Selecting
    3 For Recipe
    */
    private int type;
    private List<RVItem> RVItemList;
    private SparseBooleanArray selectedItems = new SparseBooleanArray(); // Menyimpan status pilihan

    public RVAdapter(List<RVItem> RVItemList, int type) {
        this.RVItemList = RVItemList;
        this.type = type;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        RVItem RVItem = RVItemList.get(position);

        // Set data item
        holder.nameTextView.setText(RVItem.getName());
        holder.descriptionTextView.setText(RVItem.getDescription());
        holder.imageView.setImageResource(RVItem.getImageResource());

        if (type == 1) { // Displaying purpose behavior
            holder.itemView.setClickable(false);
        } else if (type == 2) {
            holder.itemView.setClickable(true);
            // Handle selection behavior
            if (selectedItems.get(position, false)) {
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }

            holder.itemView.setOnClickListener(v -> {
                if (selectedItems.get(position, false)) {
                    selectedItems.delete(position);
                    holder.itemView.setBackgroundColor(Color.WHITE);
                } else {
                    selectedItems.put(position, true);
                    holder.itemView.setBackgroundColor(Color.LTGRAY);
                }
            });
        } else if (type == 3) {
            holder.itemView.setOnClickListener(v -> {
                String recipeName = RVItem.getName();

                // Initialize Firebase Database Reference
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://dishcraftjava-default-rtdb.asia-southeast1.firebasedatabase.app");
                DatabaseReference recipeRef = database.getReference("Recipe");

                // Query to find the recipe with the matching name
                recipeRef.orderByChild("name").equalTo(recipeName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Retrieve the recipe details
                                String name = snapshot.child("name").getValue(String.class);
                                boolean vegan = snapshot.child("vegan").getValue(Boolean.class);
                                String veganString;
                                if(vegan) veganString = "vegan";
                                else veganString = "Non-Vegan";
                                ArrayList<String> ingredients = new ArrayList<>();
                                ArrayList<String> steps = new ArrayList<>();

                                // Parse ingredients
                                for (DataSnapshot ingredientSnapshot : snapshot.child("ingredients").getChildren()) {
                                    ingredients.add(ingredientSnapshot.getValue(String.class));
                                }

                                // Parse steps
                                for (DataSnapshot stepSnapshot : snapshot.child("steps").getChildren()) {
                                    steps.add(stepSnapshot.getValue(String.class));
                                }

                                String output = name;
                                for(String item : steps){
                                    output += "\n";
                                    output += "\n";
                                    output += item;
                                }

                                String [] ingredientsArray = new String[ingredients.size()];
                                ingredients.toArray(ingredientsArray);
                                // Start RecipeDetails activity
                                Intent intent = new Intent(holder.itemView.getContext(), RecipeDetails.class);
                                intent.putExtra("output", output);
//                                intent.putExtra("vegan", vegan);
                                intent.putExtra("Ingredients", ingredientsArray);
                                holder.itemView.getContext().startActivity(intent);

                                break; // Only one recipe is expected, exit the loop
                            }
                        } else {
                            // No recipe found
                            Toast.makeText(holder.itemView.getContext(), "Recipe not found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error
                        Toast.makeText(holder.itemView.getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }


    @Override
    public int getItemCount() {
        if(RVItemList != null ){
            return RVItemList.size();
        }
        else{
            return 0;
        }
    }

    public void setList(ArrayList<RVItem> filteredList) {
        this.RVItemList = filteredList;
        return;
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        final TextView nameTextView;
        final TextView descriptionTextView;
        final ImageView imageView;

        IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemNameTV);
            descriptionTextView = itemView.findViewById(R.id.itemDescriptionTV);
            imageView = itemView.findViewById(R.id.itemIconIV);
        }
    }

    // Fungsi tambahan jika ingin mendapatkan daftar item yang dipilih
    public List<RVItem> getSelectedItems() {
        List<RVItem> selectedList = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            int key = selectedItems.keyAt(i);
            if (selectedItems.get(key)) {
                selectedList.add(RVItemList.get(key));
            }
        }
        return selectedList;
    }

    public void addItem(RVItem newItem) {
        RVItemList.add(newItem); // Tambahkan item baru ke list
        notifyItemInserted(RVItemList.size() - 1); // Beritahu RecyclerView untuk memperbarui tampilan
    }
    public void removeItem(int position) {
        RVItemList.remove(position); // Hapus item dari list
        notifyItemRemoved(position); // Beritahu RecyclerView untuk memperbarui tampilan
    }
}