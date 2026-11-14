package com.example.smartpantry;

import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.Holder> {
    public interface Listener { void edit(Ingredient i); void delete(Ingredient i); }
    private ArrayList<Ingredient> items; private Listener listener;
    public IngredientAdapter(ArrayList<Ingredient> items,Listener l){this.items=items;this.listener=l;}
    @Override public Holder onCreateViewHolder(ViewGroup p,int v){return new Holder(LayoutInflater.from(p.getContext()).inflate(R.layout.row_ingredient,p,false));}
    @Override public void onBindViewHolder(Holder h,int pos){
        Ingredient i=items.get(pos); h.name.setText(i.name); h.qty.setText(i.quantity+" "+i.unit+(i.expiry==null||i.expiry.isEmpty()?"":" • Expires "+i.expiry));
        h.edit.setOnClickListener(v->listener.edit(i)); h.delete.setOnClickListener(v->listener.delete(i));
    }
    @Override public int getItemCount(){return items.size();}
    static class Holder extends RecyclerView.ViewHolder {
        TextView name,qty; Button edit,delete;
        Holder(View v){super(v);name=v.findViewById(R.id.txtName);qty=v.findViewById(R.id.txtQuantity);edit=v.findViewById(R.id.btnEdit);delete=v.findViewById(R.id.btnDelete);}
    }
}
