package com.example.smartpantry.matching;

import com.example.smartpantry.data.PantryItem;
import com.example.smartpantry.data.RecipeIngredient;
import com.example.smartpantry.data.RecipeWithIngredients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Core business logic of the app: STRICT recipe matching.
 *
 * A recipe is suggested only if EVERY required ingredient is present in the pantry
 * in at least the required quantity. Recipes missing even one ingredient are excluded
 * from the main suggestions (they may be surfaced separately as "almost there").
 *
 * Robustness: ingredient names are normalised (case, punctuation, singular/plural)
 * and quantities are compared in a common base unit (g / ml / count), so
 * "tomato" matches "Tomatoes" and 1 kg satisfies a recipe asking for 500 g.
 */
public class StrictMatcher {

    /** Outcome of testing one recipe against the pantry. */
    public static class MatchResult {
        public RecipeWithIngredients recipe;
        /** Ingredients that are missing or insufficient. Empty = strict match. */
        public final List<RecipeIngredient> missing = new ArrayList<>();

        public boolean allMatch() {
            return missing.isEmpty();
        }

        /** Bonus "almost there" definition: exactly one ingredient short. */
        public boolean isAlmostThere() {
            return missing.size() == 1;
        }
    }

    /** Normalises an ingredient name: lowercase, strip punctuation, singularise. */
    public static String normalizeName(String raw) {
        if (raw == null) return "";
        String s = raw.toLowerCase(Locale.ROOT);
        s = s.replaceAll("[^a-z0-9\\s]", " ");
        s = s.replaceAll("\\s+", " ").trim();
        if (s.isEmpty()) return s;
        String[] words = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            sb.append(singularize(w)).append(' ');
        }
        return sb.toString().trim();
    }

    private static final Map<String, String> IRREGULAR_PLURALS = new HashMap<>();
    static {
        IRREGULAR_PLURALS.put("tomatoes", "tomato");
        IRREGULAR_PLURALS.put("potatoes", "potato");
        IRREGULAR_PLURALS.put("heroes", "hero");
    }

    private static String singularize(String w) {
        String irregular = IRREGULAR_PLURALS.get(w);
        if (irregular != null) return irregular;
        if (w.endsWith("ies") && w.length() > 3) return w.substring(0, w.length() - 3) + "y";
        if (w.endsWith("sses") || w.endsWith("shes") || w.endsWith("ches") || w.endsWith("xes"))
            return w.substring(0, w.length() - 2);
        if (w.endsWith("s") && !w.endsWith("ss") && w.length() > 1) return w.substring(0, w.length() - 1);
        return w;
    }

    /**
     * Converts a quantity to a common base unit: grams for weight, millilitres for
     * volume, or a plain count for countable units. Units that do not convert
     * cleanly (tsp, tbsp, pinch...) return the raw quantity so the match degrades
     * to a count comparison rather than failing outright.
     */
    public static double toBase(double quantity, String unit) {
        if (unit == null) return quantity;
        switch (unit.toLowerCase(Locale.ROOT).trim()) {
            case "kg": return quantity * 1000;
            case "g": return quantity;
            case "l":
            case "litre":
            case "liter": return quantity * 1000;
            case "ml": return quantity;
            default: return quantity; // pcs, can, count-based units
        }
    }

    /** Units where quantity is meaningless - the ingredient only needs to be present. */
    public static boolean isPresenceOnly(String unit) {
        if (unit == null) return true;
        String u = unit.toLowerCase(Locale.ROOT).trim();
        return u.equals("tsp") || u.equals("tbsp") || u.equals("pinch") || u.equals("to taste");
    }

    /** Aggregates pantry quantities by normalised name, converted to base units. */
    public static Map<String, Double> pantryTotals(List<PantryItem> pantry) {
        Map<String, Double> totals = new HashMap<>();
        for (PantryItem item : pantry) {
            String key = normalizeName(item.name);
            if (key.isEmpty()) continue;
            totals.merge(key, toBase(item.quantity, item.unit), Double::sum);
        }
        return totals;
    }

    /**
     * Tests every recipe against the pantry and returns results sorted so that
     * full matches come first, then "almost there" recipes (missing 1 ingredient),
     * then the rest.
     */
    public static List<MatchResult> match(List<PantryItem> pantry, List<RecipeWithIngredients> recipes) {
        Map<String, Double> totals = pantryTotals(pantry);
        List<MatchResult> results = new ArrayList<>();
        for (RecipeWithIngredients r : recipes) {
            MatchResult mr = new MatchResult();
            mr.recipe = r;
            for (RecipeIngredient ing : r.ingredients) {
                String key = normalizeName(ing.name);
                Double have = totals.get(key);
                if (have == null) {
                    mr.missing.add(ing); // not in the pantry at all
                    continue;
                }
                if (isPresenceOnly(ing.unit)) continue; // present is enough
                double need = toBase(ing.quantity, ing.unit);
                if (have < need) mr.missing.add(ing); // not enough of it
            }
            results.add(mr);
        }
        results.sort((a, b) -> Integer.compare(a.missing.size(), b.missing.size()));
        return results;
    }

    /** True if the pantry currently covers this single recipe ingredient. */
    public static boolean isCovered(RecipeIngredient ing, Map<String, Double> pantryTotals) {
        Double have = pantryTotals.get(normalizeName(ing.name));
        if (have == null) return false;
        if (isPresenceOnly(ing.unit)) return true;
        return have >= toBase(ing.quantity, ing.unit);
    }
}
