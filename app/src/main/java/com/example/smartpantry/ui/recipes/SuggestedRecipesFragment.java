package com.example.smartpantry.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.data.AppDatabase;
import com.example.smartpantry.data.AppExecutors;
import com.example.smartpantry.data.PantryItem;
import com.example.smartpantry.data.RecipeWithIngredients;
import com.example.smartpantry.matching.StrictMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Suggested Recipes screen. Observes both the pantry and the recipe collection,
 * re-runs the strict-matching logic whenever either changes, and shows:
 *   1. "You can make" - strict matches (every ingredient, in the right quantity)
 *   2. "Almost there" - bonus section, recipes missing exactly ONE ingredient,
 *      kept clearly separate from the strict list.
 * If nothing matches, a friendly message is shown instead of a blank screen.
 */
public class SuggestedRecipesFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener {

    private RecipeAdapter adapter;
    private TextView emptyText;

    private List<PantryItem> currentPantry;
    private List<RecipeWithIngredients> currentRecipes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggested, container, false);

        emptyText = view.findViewById(R.id.text_suggested_empty);
        RecyclerView recycler = view.findViewById(R.id.recycler_suggested);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeAdapter(this);
        recycler.setAdapter(adapter);

        AppDatabase db = AppDatabase.getInstance(requireContext());
        db.pantryItemDao().getAll().observe(getViewLifecycleOwner(), items -> {
            currentPantry = items;
            recompute();
        });
        AppExecutors.diskIO().execute(() -> {
            List<RecipeWithIngredients> recipes = db.recipeDao().getRecipesWithIngredientsSync();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    currentRecipes = recipes;
                    recompute();
                });
            }
        });

        return view;
    }

    private void recompute() {
        if (currentPantry == null || currentRecipes == null) return;
        List<PantryItem> pantry = currentPantry;
        List<RecipeWithIngredients> recipes = currentRecipes;

        AppExecutors.diskIO().execute(() -> {
            List<StrictMatcher.MatchResult> results = StrictMatcher.match(pantry, recipes);
            List<MatchRow> rows = buildRows(results);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapter.submitRows(rows);
                    // Feedback for zero matches: no strict matches AND nothing even close.
                    boolean anyShown = !rows.isEmpty();
                    emptyText.setVisibility(anyShown ? View.GONE : View.VISIBLE);
                    if (!anyShown) {
                        emptyText.setText(R.string.no_matches);
                    }
                });
            }
        });
    }

    private List<MatchRow> buildRows(List<StrictMatcher.MatchResult> results) {
        List<MatchRow> rows = new ArrayList<>();
        List<StrictMatcher.MatchResult> strict = new ArrayList<>();
        List<StrictMatcher.MatchResult> almost = new ArrayList<>();
        for (StrictMatcher.MatchResult r : results) {
            if (r.allMatch()) strict.add(r);
            else if (r.isAlmostThere()) almost.add(r);
        }
        if (!strict.isEmpty()) {
            rows.add(MatchRow.header("You can make (" + strict.size() + ")"));
            for (StrictMatcher.MatchResult r : strict) rows.add(MatchRow.recipe(r));
        }
        if (!almost.isEmpty()) {
            rows.add(MatchRow.header("Almost there - missing 1 ingredient (" + almost.size() + ")"));
            for (StrictMatcher.MatchResult r : almost) rows.add(MatchRow.recipe(r));
        }
        return rows;
    }

    @Override
    public void onRecipeClicked(StrictMatcher.MatchResult result) {
        Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, result.recipe.recipe.id);
        startActivity(intent);
    }
}
