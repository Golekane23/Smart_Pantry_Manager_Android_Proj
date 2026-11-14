package com.example.smartpantry;

import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.Holder> {
    public interface Listener { void open(Recipe r); }
    private ArrayList<Recipe> items; private Listener listener;
    public RecipeAdapter(ArrayList<Recipe> items,Listener l){this.items=items;listener=l;}
    @Override public Holder onCreateViewHolder(ViewGroup p,int v){return new Holder(LayoutInflater.from(p.getContext()).inflate(R.layout.row_recipe,p,false));}
    @Override public void onBindViewHolder(Holder h,int p){Recipe r=items.get(p);h.name.setText(r.name);h.ingredients.setText(r.ingredients);h.itemView.setOnClickListener(v->listener.open(r));}
    @Override public int getItemCount(){return items.size();}
    static class Holder extends RecyclerView.ViewHolder {
        TextView name,ingredients; Holder(View v){super(v);name=v.findViewById(R.id.txtRecipeName);ingredients=v.findViewById(R.id.txtRecipeIngredients);}
    }
}
