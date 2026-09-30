# Smart Pantry Manager App

Android application (100% **Java**) that helps reduce food waste by tracking the
ingredients in your pantry and suggesting recipes you can cook **right now** -
strictly using what you already have.

## Features

- **Pantry management** - add, edit and delete ingredients (name, quantity, unit, optional expiry date)
- **Pantry list** - RecyclerView with a custom adapter, bound to the Room database via LiveData
- **Recipe collection** - 20 recipes pre-loaded on first run (name, ingredients, method)
- **Suggested Recipes** - strict-matching engine: a recipe appears only if **every**
  ingredient is in the pantry in at least the required quantity
- **Almost There (bonus)** - clearly separated section for recipes missing exactly 1 ingredient
- **Recipe detail** - full ingredient list ticked against your pantry + the method
- **Settings** - expiring-soon alert toggle + warning period, unit preference
- **Zero-match feedback** - friendly message instead of a blank screen
- Input validation on all forms, bottom navigation, expiry highlighting

## Tech stack

| Layer | Choice |
|---|---|
| Language | Java 17 |
| Database | Room (SQLite) with full CRUD + persistence |
| UI | Activities + Fragments, RecyclerView, Material Components |
| Async | `ExecutorService` + Room LiveData |

> Deliberately **no** Google Maps / GPS / location services - out of scope.

## The strict-matching rule (`matching/StrictMatcher.java`)

A recipe is suggested only if **every** required ingredient is present in the
pantry in at least the required quantity. Robust to real-world messiness:

- **Name normalisation**: lowercase, punctuation stripped, singular/plural handled
  ("Tomatoes!" matches "tomato"; "spring onions" matches "spring onion")
- **Unit conversion**: quantities compared in base units - g/kg -> grams,
  ml/l -> millilitres, countable units compared as counts (500 g in pantry
  satisfies a recipe needing 1 kg... and vice versa is correctly rejected)
- **Presence-only units** (tsp, tbsp, pinch): being in the pantry at all is enough

## Screens

1. **Pantry List** (MainActivity + PantryFragment)
2. **Add / Edit Ingredient** (AddEditIngredientActivity)
3. **Suggested Recipes** (SuggestedRecipesFragment)
4. **Recipe Detail** (RecipeDetailActivity)
5. **Settings** (SettingsFragment)

## How to run

1. Open the project folder in **Android Studio** (Hedgehog or newer).
   Let Gradle sync - the Gradle wrapper will be generated on first open.
2. Select an emulator or device (min SDK 24 / Android 7.0).
3. Run. The database is created and seeded with 20 recipes on first launch;
   pantry data persists across restarts.

## GitHub setup

```bash
git init
git add .
git commit -m "Initial commit: Smart Pantry Manager"
git branch -M main
git remote add origin https://github.com/andiswamaila <your-username>/smart-pantry-manager-app.git
git push -u origin main
```


