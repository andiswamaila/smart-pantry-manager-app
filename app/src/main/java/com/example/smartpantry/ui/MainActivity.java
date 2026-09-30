package com.example.smartpantry.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.smartpantry.R;
import com.example.smartpantry.ui.pantry.PantryFragment;
import com.example.smartpantry.ui.recipes.SuggestedRecipesFragment;
import com.example.smartpantry.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Host activity with a bottom navigation bar switching between the three
 * main screens: Pantry, Suggested Recipes and Settings.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        if (savedInstanceState == null) {
            loadFragment(new PantryFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();
            if (id == R.id.nav_pantry) {
                fragment = new PantryFragment();
            } else if (id == R.id.nav_recipes) {
                fragment = new SuggestedRecipesFragment();
            } else {
                fragment = new SettingsFragment();
            }
            loadFragment(fragment);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
