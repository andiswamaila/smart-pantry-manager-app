package com.example.smartpantry.ui.recipes;

import com.example.smartpantry.matching.StrictMatcher;

/**
 * One row in the Suggested Recipes list: either a section header or a recipe.
 * Sections are "You can make" (strict matches) and "Almost there" (missing 1
 * ingredient) - the bonus list is kept clearly separated from strict matches.
 */
public class MatchRow {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_RECIPE = 1;

    public final int type;
    public final String headerTitle;
    public final StrictMatcher.MatchResult result;

    private MatchRow(int type, String headerTitle, StrictMatcher.MatchResult result) {
        this.type = type;
        this.headerTitle = headerTitle;
        this.result = result;
    }

    public static MatchRow header(String title) {
        return new MatchRow(TYPE_HEADER, title, null);
    }

    public static MatchRow recipe(StrictMatcher.MatchResult result) {
        return new MatchRow(TYPE_RECIPE, null, result);
    }
}
