package com.example.smartpantry;

import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import java.util.*;

public class SuggestedRecipesActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_suggestions);
        RecyclerView rv=findViewById(R.id.recyclerRecipes);rv.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Recipe> matches=new DatabaseHelper(this).getStrictMatches();
        TextView empty=findViewById(R.id.txtEmpty);empty.setVisibility(matches.isEmpty()?View.VISIBLE:View.GONE);
        rv.setAdapter(new RecipeAdapter(matches,r->{Intent i=new Intent(this,RecipeDetailActivity.class);i.putExtra("recipe_id",r.id);startActivity(i);}));
    }
}
