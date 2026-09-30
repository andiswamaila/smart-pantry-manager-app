package com.example.smartpantry.ui.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.smartpantry.R;
import com.example.smartpantry.data.AppDatabase;
import com.example.smartpantry.data.AppExecutors;
import com.example.smartpantry.data.PantryItem;
import com.example.smartpantry.data.RecipeIngredient;
import com.example.smartpantry.matching.StrictMatcher;

import java.util.List;
import java.util.Map;

/**
 * Recipe Detail screen: full ingredient list (marked against the current pantry)
 * and the preparation method. Reached via Intent with EXTRA_RECIPE_ID.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "com.example.smartpantry.RECIPE_ID";

    private AppDatabase db;
    private LinearLayout ingredientList;
    private long recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        db = AppDatabase.getInstance(this);

        ingredientList = findViewById(R.id.layout_ingredient_list);
        recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);

        LiveData<com.example.smartpantry.data.Recipe> recipeLive = db.recipeDao().getById(recipeId);
        recipeLive.observe(this, recipe -> {
            if (recipe != null) {
                setTitle(recipe.name);
                ((TextView) findViewById(R.id.text_detail_name)).setText(recipe.name);
                ((TextView) findViewById(R.id.text_detail_steps)).setText(recipe.steps);
            }
        });

        db.recipeDao().getIngredients(recipeId).observe(this, this::renderIngredients);
    }

    private void renderIngredients(List<RecipeIngredient> ingredients) {
        if (ingredients == null) return;
        // Load the pantry on a background thread so each ingredient can be
        // ticked off (green) or flagged (red) against what the user actually has.
        AppExecutors.diskIO().execute(() -> {
            List<PantryItem> pantry = db.pantryItemDao().getAllSync();
            Map<String, Double> totals = StrictMatcher.pantryTotals(pantry);
            runOnUiThread(() -> {
                ingredientList.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(this);
                for (RecipeIngredient ing : ingredients) {
                    TextView line = (TextView) inflater.inflate(
                            R.layout.item_recipe_ingredient, ingredientList, false);
                    boolean covered = StrictMatcher.isCovered(ing, totals);
                    String qty = ing.quantity == Math.floor(ing.quantity)
                            ? String.valueOf((long) ing.quantity) : String.valueOf(ing.quantity);
                    line.setText((covered ? "\u2713 " : "\u2717 ") + qty + " " + ing.unit + " " + ing.name);
                    line.setTextColor(covered ? 0xFF2E7D32 : 0xFFC62828);
                    ingredientList.addView(line);
                }
            });
        });
    }
}
