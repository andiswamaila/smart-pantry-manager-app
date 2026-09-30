package com.example.smartpantry.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipes ORDER BY name COLLATE NOCASE")
    LiveData<List<Recipe>> getAll();

    @Query("SELECT * FROM recipes WHERE id = :id")
    LiveData<Recipe> getById(long id);

    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    LiveData<List<RecipeIngredient>> getIngredients(long recipeId);

    @Transaction
    @Query("SELECT * FROM recipes")
    List<RecipeWithIngredients> getRecipesWithIngredientsSync();
}
