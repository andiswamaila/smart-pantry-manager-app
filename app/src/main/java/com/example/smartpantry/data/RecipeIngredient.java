package com.example.smartpantry.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_ingredients", indices = {@Index("recipeId")})
public class RecipeIngredient {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long recipeId;

    @NonNull
    public String name;

    public double quantity;

    @NonNull
    public String unit;

    public RecipeIngredient() {
        name = "";
        unit = "pcs";
    }
}
