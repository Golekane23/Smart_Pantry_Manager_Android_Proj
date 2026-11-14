package com.example.smartpantry;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="smart_pantry.db";
    private static final int DB_VERSION=1;
    public DatabaseHelper(Context c) { super(c, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ingredients(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, expiry TEXT)");
        db.execSQL("CREATE TABLE recipes(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, steps TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredients(recipe_id INTEGER, ingredient_name TEXT, quantity REAL, unit TEXT, FOREIGN KEY(recipe_id) REFERENCES recipes(id))");
        db.execSQL("CREATE TABLE settings(setting_key TEXT PRIMARY KEY, setting_value TEXT)");
        seedRecipes(db);
    }
    @Override public void onUpgrade(SQLiteDatabase db,int oldV,int newV) {
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients"); db.execSQL("DROP TABLE IF EXISTS ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes"); db.execSQL("DROP TABLE IF EXISTS settings"); onCreate(db);
    }
    private void seedRecipes(SQLiteDatabase db) {
        String[][] data = {
          {"Tomato Egg Scramble","tomato:2|eggs:2","Chop tomatoes. Cook briefly in a pan, add beaten eggs and stir until set."},
          {"Garlic Pasta","pasta:200|garlic:2|olive oil:1","Boil pasta. Gently cook garlic in olive oil and toss together."},
          {"Cheese Omelette","eggs:2|cheese:50","Beat eggs, cook in a pan, add cheese and fold."},
          {"Tomato Salad","tomato:2|onion:1|olive oil:1","Slice tomato and onion. Dress with olive oil."},
          {"Banana Pancakes","banana:1|eggs:2|flour:100","Mash banana, mix with eggs and flour, then pan-fry small pancakes."},
          {"Simple Fried Rice","rice:200|eggs:1|onion:1","Cook rice. Fry onion, add egg and rice, then stir until hot."},
          {"Potato Hash","potatoes:2|onion:1|olive oil:1","Dice potatoes and onion. Pan-fry until tender and golden."},
          {"Cheese Toast","bread:2|cheese:40","Place cheese between bread slices and toast until melted."},
          {"Apple Oats","apple:1|oats:50|milk:150","Chop apple. Warm oats with milk and top with apple."},
          {"Peanut Banana Oats","banana:1|oats:50|milk:150|peanut butter:1","Cook oats in milk. Top with banana and peanut butter."},
          {"Carrot Soup","carrots:3|onion:1|water:500","Cook chopped carrots and onion in water until soft, then blend."},
          {"Tuna Pasta","pasta:200|tuna:1|olive oil:1","Boil pasta, drain tuna and combine with olive oil."},
          {"Spinach Omelette","eggs:2|spinach:50|onion:1","Cook onion and spinach, add beaten eggs and cook through."},
          {"Yoghurt Fruit Bowl","yoghurt:150|banana:1|apple:1","Slice fruit and serve over yoghurt."},
          {"Garlic Potatoes","potatoes:2|garlic:2|olive oil:1","Cut potatoes, coat with garlic and oil, then roast until crisp."},
          {"Tomato Cheese Pasta","pasta:200|tomato:2|cheese:40","Cook pasta. Warm chopped tomato and stir through with cheese."},
          {"Milk Porridge","oats:60|milk:200","Simmer oats in milk, stirring until creamy."},
          {"Onion Egg Sandwich","bread:2|eggs:2|onion:1","Cook onion and eggs, then serve between bread slices."},
          {"Carrot Salad","carrots:2|lemon:1|olive oil:1","Grate carrots and dress with lemon juice and olive oil."},
          {"Rice and Beans","rice:200|beans:150|onion:1","Cook rice. Heat beans with onion and serve together."}
        };
        for(String[] r:data) {
            ContentValues cv=new ContentValues(); cv.put("name",r[0]); cv.put("steps",r[2]);
            long id=db.insert("recipes",null,cv);
            for(String part:r[1].split("\\|")) {
                String[] p=part.split(":");
                ContentValues iv=new ContentValues(); iv.put("recipe_id",id); iv.put("ingredient_name",p[0]);
                iv.put("quantity",Double.parseDouble(p[1])); iv.put("unit",unitFor(p[0]));
                db.insert("recipe_ingredients",null,iv);
            }
        }
    }
    private static String unitFor(String n) {
        if(n.equals("eggs")||n.equals("tomato")||n.equals("onion")||n.equals("banana")||n.equals("apple")||n.equals("potatoes")||n.equals("carrots")||n.equals("garlic")||n.equals("lemon")||n.equals("bread")) return "pieces";
        if(n.equals("olive oil")||n.equals("peanut butter")) return "tbsp";
        if(n.equals("milk")||n.equals("water")) return "ml";
        if(n.equals("yoghurt")||n.equals("tuna")||n.equals("beans")) return "g";
        return "g";
    }
    public long addIngredient(String n,double q,String u,String e) {
        ContentValues v=new ContentValues(); v.put("name",n); v.put("quantity",q); v.put("unit",u); v.put("expiry",e);
        return getWritableDatabase().insert("ingredients",null,v);
    }
    public int updateIngredient(int id,String n,double q,String u,String e) {
        ContentValues v=new ContentValues(); v.put("name",n); v.put("quantity",q); v.put("unit",u); v.put("expiry",e);
        return getWritableDatabase().update("ingredients",v,"id=?",new String[]{String.valueOf(id)});
    }
    public int deleteIngredient(int id) { return getWritableDatabase().delete("ingredients","id=?",new String[]{String.valueOf(id)}); }
    public ArrayList<Ingredient> getIngredients() {
        ArrayList<Ingredient> out=new ArrayList<>(); Cursor c=getReadableDatabase().rawQuery("SELECT * FROM ingredients ORDER BY name",null);
        try { while(c.moveToNext()) out.add(new Ingredient(c.getInt(0),c.getString(1),c.getDouble(2),c.getString(3),c.getString(4))); }
        finally { c.close(); } return out;
    }
    public ArrayList<Recipe> getStrictMatches() {
        ArrayList<Recipe> out=new ArrayList<>();
        ArrayList<Ingredient> pantry=getIngredients();
        Cursor rc=getReadableDatabase().rawQuery("SELECT id,name,steps FROM recipes ORDER BY name",null);
        try {
            while(rc.moveToNext()) {
                int id=rc.getInt(0); boolean qualifies=true;
                Cursor ic=getReadableDatabase().rawQuery("SELECT ingredient_name,quantity,unit FROM recipe_ingredients WHERE recipe_id=?",new String[]{String.valueOf(id)});
                try {
                    while(ic.moveToNext()) {
                        String required=normalize(ic.getString(0)); double needed=ic.getDouble(1);
                        boolean found=false;
                        for(Ingredient have:pantry) {
                            if(normalize(have.name).equals(required) && compatibleUnits(have.unit,ic.getString(2)) && have.quantity>=needed) { found=true; break; }
                        }
                        if(!found) { qualifies=false; break; }
                    }
                } finally { ic.close(); }
                if(qualifies) out.add(new Recipe(id,rc.getString(1),ingredientSummary(id),rc.getString(2)));
            }
        } finally { rc.close(); }
        return out;
    }
    private String ingredientSummary(int id) {
        StringBuilder s=new StringBuilder(); Cursor c=getReadableDatabase().rawQuery("SELECT ingredient_name,quantity,unit FROM recipe_ingredients WHERE recipe_id=?",new String[]{String.valueOf(id)});
        try { while(c.moveToNext()) { if(s.length()>0)s.append(", "); s.append(c.getString(0)).append(" ").append(c.getString(1)).append(" ").append(c.getString(2)); } }
        finally { c.close(); } return s.toString();
    }
    public Recipe getRecipe(int id) {
        Cursor c=getReadableDatabase().rawQuery("SELECT id,name,steps FROM recipes WHERE id=?",new String[]{String.valueOf(id)});
        try { if(c.moveToFirst()) return new Recipe(id,c.getString(1),ingredientSummary(id),c.getString(2)); }
        finally { c.close(); } return null;
    }
    private String normalize(String s) {
        String n=s==null?"":s.trim().toLowerCase(Locale.ROOT);
        if(n.endsWith("ies") && n.length()>3) n=n.substring(0,n.length()-3)+"y";
        else if(n.endsWith("s") && !n.endsWith("ss") && n.length()>2) n=n.substring(0,n.length()-1);
        return n;
    }
    private boolean compatibleUnits(String a,String b) {
        String x=a==null?"":a.trim().toLowerCase(Locale.ROOT), y=b==null?"":b.trim().toLowerCase(Locale.ROOT);
        if(x.equals(y)) return true;
        return (x.equals("piece")||x.equals("pieces")) && (y.equals("piece")||y.equals("pieces"));
    }
    public boolean getExpiryPreference() {
        Cursor c=getReadableDatabase().rawQuery("SELECT setting_value FROM settings WHERE setting_key='expiry_alerts'",null);
        try { return c.moveToFirst() && "true".equals(c.getString(0)); } finally { c.close(); }
    }
    public void setExpiryPreference(boolean enabled) {
        ContentValues v=new ContentValues(); v.put("setting_key","expiry_alerts"); v.put("setting_value",String.valueOf(enabled));
        getWritableDatabase().insertWithOnConflict("settings",null,v,SQLiteDatabase.CONFLICT_REPLACE);
    }
}
