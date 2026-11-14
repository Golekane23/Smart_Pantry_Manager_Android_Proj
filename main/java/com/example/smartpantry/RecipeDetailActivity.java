package com.example.smartpantry;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_recipe_detail);
        Recipe r=new DatabaseHelper(this).getRecipe(getIntent().getIntExtra("recipe_id",-1));
        if(r!=null){((TextView)findViewById(R.id.txtDetailName)).setText(r.name);((TextView)findViewById(R.id.txtDetailIngredients)).setText(r.ingredients);((TextView)findViewById(R.id.txtDetailSteps)).setText(r.steps);}
    }
}
