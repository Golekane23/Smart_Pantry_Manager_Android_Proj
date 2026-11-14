package com.example.smartpantry;

import android.content.*;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db; RecyclerView recycler; IngredientAdapter adapter;
    @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_main);
        db=new DatabaseHelper(this); recycler=findViewById(R.id.recyclerPantry); recycler.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.btnAdd).setOnClickListener(v->startActivity(new Intent(this,AddEditIngredientActivity.class)));
        findViewById(R.id.btnSuggestions).setOnClickListener(v->startActivity(new Intent(this,SuggestedRecipesActivity.class)));
        findViewById(R.id.btnSettings).setOnClickListener(v->startActivity(new Intent(this,SettingsActivity.class)));
    }
    @Override protected void onResume(){super.onResume();loadPantry();}
    private void loadPantry(){
        ArrayList<Ingredient> list=db.getIngredients();
        ((TextView)findViewById(R.id.txtPantryCount)).setText("Your pantry • "+list.size()+" ingredient(s)");
        adapter=new IngredientAdapter(list,new IngredientAdapter.Listener(){
            public void edit(Ingredient i){Intent x=new Intent(MainActivity.this,AddEditIngredientActivity.class);x.putExtra("id",i.id);x.putExtra("name",i.name);x.putExtra("quantity",i.quantity);x.putExtra("unit",i.unit);x.putExtra("expiry",i.expiry);startActivity(x);}
            public void delete(Ingredient i){new android.app.AlertDialog.Builder(MainActivity.this).setTitle("Delete ingredient").setMessage("Delete "+i.name+"?").setNegativeButton("Cancel",null).setPositiveButton("Delete",(d,w)->{db.deleteIngredient(i.id);loadPantry();}).show();}
        }); recycler.setAdapter(adapter);
    }
}
