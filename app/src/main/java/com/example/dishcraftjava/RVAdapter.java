package com.example.dishcraftjava;

import android.content.Intent;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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

        if(type == 1){ //Displaying purpose behavior
            holder.itemView.setClickable(false);
        }
        else if(type == 2){
            holder.itemView.setClickable(true);
            // Periksa status pilihan dan ubah warna latar belakang
            if (selectedItems.get(position, false)) {
                holder.itemView.setBackgroundColor(Color.LTGRAY); // Warna jika dipilih
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE); // Warna default
            }

            // Tambahkan listener untuk menangani klik
            holder.itemView.setOnClickListener(v -> {
                holder.itemView.setClickable(true);
                if (selectedItems.get(position, false)) {
                    // Jika sudah dipilih, hapus dari daftar pilihan
                    selectedItems.delete(position);
                    holder.itemView.setBackgroundColor(Color.WHITE);
                } else {
                    // Jika belum dipilih, tambahkan ke daftar pilihan
                    selectedItems.put(position, true);
                    holder.itemView.setBackgroundColor(Color.LTGRAY);
                }
            });
        }
        else if(type == 3){
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), RecipeDetails.class);
                holder.itemView.getContext().startActivity(intent);
                //TODO Take data from firebase and display it in RecipeDetails
            });
        }

    }

    @Override
    public int getItemCount() {
        return RVItemList.size();
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