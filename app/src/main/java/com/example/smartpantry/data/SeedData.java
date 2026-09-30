package com.example.smartpantry.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Pre-loaded recipe collection. Seeded into the database the first time the app runs.
 * Ingredient format: "name;quantity;unit"
 */
public class SeedData {

    public static void seed(SupportSQLiteDatabase db) {
        // --- Strictly-matchable staples ---
        add(db, "Tomato Pasta",
                "1. Boil the pasta in salted water until al dente.\n2. Chop the tomatoes and garlic.\n3. Fry garlic in olive oil for 1 minute.\n4. Add tomatoes and simmer 10 minutes.\n5. Toss with the drained pasta and serve.",
                new String[]{"pasta;250;g", "tomatoes;4;pcs", "garlic;2;pcs", "olive oil;2;tbsp"});

        add(db, "Garlic Butter Rice",
                "1. Rinse the rice.\n2. Fry the garlic in butter for 1 minute.\n3. Add rice and 400 ml water, cover and simmer 15 minutes.\n4. Fluff with a fork and serve.",
                new String[]{"rice;200;g", "garlic;3;pcs", "butter;50;g"});

        add(db, "Veggie Omelette",
                "1. Beat the eggs with the milk and a pinch of salt.\n2. Melt the butter in a pan.\n3. Pour in the eggs, add chopped tomato.\n4. Cook until set, fold and serve.",
                new String[]{"eggs;3;pcs", "milk;50;ml", "tomatoes;1;pcs", "butter;20;g"});

        add(db, "Fluffy Pancakes",
                "1. Mix the flour, sugar and a pinch of salt.\n2. Whisk in the eggs and milk to a smooth batter.\n3. Fry ladles of batter in butter, 2 minutes per side.\n4. Stack and serve.",
                new String[]{"flour;200;g", "eggs;2;pcs", "milk;300;ml", "sugar;30;g", "butter;30;g"});

        add(db, "Banana Smoothie",
                "1. Peel the bananas and place in a blender.\n2. Add the milk and honey.\n3. Blend until smooth and pour into glasses.",
                new String[]{"bananas;2;pcs", "milk;300;ml", "honey;2;tbsp"});

        add(db, "French Toast",
                "1. Whisk the eggs, milk and cinnamon.\n2. Soak the bread slices in the mixture.\n3. Fry in butter until golden on both sides.\n4. Serve warm.",
                new String[]{"bread;6;pcs", "eggs;3;pcs", "milk;100;ml", "butter;20;g", "cinnamon;1;tsp"});

        add(db, "Cheese Quesadilla",
                "1. Grate the cheese and chop the tomato.\n2. Sprinkle cheese and tomato over one tortilla, top with another.\n3. Toast in a dry pan until the cheese melts.\n4. Cut into wedges and serve.",
                new String[]{"tortillas;4;pcs", "cheese;200;g", "tomatoes;1;pcs"});

        add(db, "Egg Salad",
                "1. Boil the eggs for 9 minutes, then cool and peel.\n2. Mash the eggs with mayonnaise and mustard.\n3. Season and serve on bread or lettuce.",
                new String[]{"eggs;4;pcs", "mayonnaise;3;tbsp", "mustard;1;tsp"});

        add(db, "Breakfast Porridge",
                "1. Simmer the oats with the milk for 5 minutes.\n2. Stir in the honey.\n3. Serve warm with sliced banana if you have one.",
                new String[]{"oats;100;g", "milk;250;ml", "honey;1;tbsp"});

        add(db, "Baked Potatoes",
                "1. Prick the potatoes and bake at 200 C for 45-60 minutes.\n2. Split open and fill with butter and grated cheese.\n3. Sprinkle with sliced spring onion.",
                new String[]{"potatoes;4;pcs", "butter;50;g", "cheese;100;g", "spring onions;2;pcs"});

        add(db, "Tomato Soup",
                "1. Chop the tomatoes, onion and garlic.\n2. Fry the onion and garlic in olive oil for 3 minutes.\n3. Add tomatoes and stock, simmer 20 minutes.\n4. Blend until smooth, swirl in the cream.",
                new String[]{"tomatoes;6;pcs", "onion;1;pcs", "garlic;2;pcs", "cream;100;ml", "vegetable stock;500;ml"});

        add(db, "Fried Rice",
                "1. Cook the rice and let it cool.\n2. Scramble the eggs in a hot pan and set aside.\n3. Stir-fry carrot and peas for 3 minutes.\n4. Add rice, soy sauce and eggs, toss for 2 minutes.",
                new String[]{"rice;250;g", "eggs;2;pcs", "carrot;1;pcs", "peas;100;g", "soy sauce;3;tbsp"});

        add(db, "Chicken Stir Fry",
                "1. Slice the chicken and stir-fry over high heat until browned.\n2. Add chopped broccoli and garlic, cook 3 minutes.\n3. Add soy sauce and a splash of water.\n4. Serve over rice.",
                new String[]{"chicken;400;g", "broccoli;1;pcs", "garlic;2;pcs", "soy sauce;3;tbsp", "rice;200;g"});

        add(db, "Margherita Pizza",
                "1. Dissolve the yeast in warm water, mix with flour, olive oil and salt, knead 5 minutes.\n2. Leave the dough to rise for 1 hour.\n3. Stretch the base, top with crushed tomatoes and torn mozzarella.\n4. Bake at 220 C for 12 minutes.",
                new String[]{"flour;300;g", "yeast;7;g", "tomatoes;3;pcs", "mozzarella;200;g", "olive oil;2;tbsp"});

        add(db, "Lentil Soup",
                "1. Fry the chopped onion, carrot and garlic for 4 minutes.\n2. Add the lentils and stock.\n3. Simmer 25 minutes until tender.\n4. Season and blend half the soup for texture.",
                new String[]{"lentils;250;g", "carrot;2;pcs", "onion;2;pcs", "garlic;2;pcs", "vegetable stock;1;l"});

        add(db, "Beef Tacos",
                "1. Brown the minced beef in a hot pan.\n2. Warm the tortillas.\n3. Fill with beef, chopped tomato, onion and grated cheese.\n4. Fold and serve immediately.",
                new String[]{"minced beef;500;g", "tortillas;8;pcs", "tomatoes;2;pcs", "onion;1;pcs", "cheese;100;g"});

        add(db, "Mushroom Risotto",
                "1. Fry the chopped onion in butter for 3 minutes.\n2. Add the rice, stir 2 minutes, then add wine.\n3. Add hot stock ladle by ladle, stirring, for 18 minutes.\n4. Stir in the mushrooms, parmesan and butter.",
                new String[]{"rice;300;g", "mushrooms;300;g", "onion;1;pcs", "parmesan;100;g", "butter;50;g", "white wine;150;ml"});

        add(db, "Vegetable Curry",
                "1. Fry the chopped onion for 3 minutes.\n2. Add the curry powder and fry 1 minute.\n3. Add chopped potato and carrot, coconut milk and a splash of water.\n4. Simmer 20 minutes until tender. Serve with rice.",
                new String[]{"potatoes;3;pcs", "carrot;2;pcs", "onion;1;pcs", "coconut milk;400;ml", "curry powder;2;tbsp"});

        add(db, "Tuna Pasta Bake",
                "1. Boil the pasta.\n2. Drain the tuna and flake into a bowl with the chopped tomatoes.\n3. Mix with the pasta, top with grated cheese.\n4. Bake at 180 C for 15 minutes.",
                new String[]{"pasta;250;g", "canned tuna;2;can", "tomatoes;2;pcs", "cheese;100;g"});

        add(db, "Simple Salad Bowl",
                "1. Chop the lettuce, tomatoes and spring onion.\n2. Slice the carrot into ribbons.\n3. Toss everything with olive oil and a pinch of salt.\n4. Top with a boiled egg if desired.",
                new String[]{"lettuce;1;pcs", "tomatoes;2;pcs", "carrot;1;pcs", "spring onions;2;pcs", "olive oil;1;tbsp"});
    }

    private static void add(SupportSQLiteDatabase db, String name, String steps, String[] ingredients) {
        ContentValues recipe = new ContentValues();
        recipe.put("name", name);
        recipe.put("steps", steps);
        long recipeId = db.insert("recipes", SQLiteDatabase.CONFLICT_REPLACE, recipe);

        for (String raw : ingredients) {
            String[] parts = raw.split(";");
            ContentValues ing = new ContentValues();
            ing.put("recipeId", recipeId);
            ing.put("name", parts[0]);
            ing.put("quantity", Double.parseDouble(parts[1]));
            ing.put("unit", parts[2]);
            db.insert("recipe_ingredients", SQLiteDatabase.CONFLICT_REPLACE, ing);
        }
    }
}
