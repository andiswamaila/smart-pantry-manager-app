package com.example.smartpantry.ui.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.matching.StrictMatcher;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClicked(StrictMatcher.MatchResult result);
    }

    private final List<MatchRow> rows = new ArrayList<>();
    private final OnRecipeClickListener listener;

    public RecipeAdapter(OnRecipeClickListener listener) {
        this.listener = listener;
    }

    public void submitRows(List<MatchRow> newRows) {
        rows.clear();
        rows.addAll(newRows);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return rows.get(position).type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == MatchRow.TYPE_HEADER) {
            return new HeaderHolder(inflater.inflate(R.layout.item_section_header, parent, false));
        }
        return new RecipeHolder(inflater.inflate(R.layout.item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MatchRow row = rows.get(position);
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).title.setText(row.headerTitle);
        } else {
            RecipeHolder h = (RecipeHolder) holder;
            h.name.setText(row.result.recipe.recipe.name);
            if (row.result.allMatch()) {
                h.status.setText("You have everything - ready to cook!");
                h.status.setTextColor(0xFF2E7D32);
            } else {
                h.status.setText("Missing: " + row.result.missing.get(0).name);
                h.status.setTextColor(0xFFF57C00);
            }
            h.itemView.setOnClickListener(v -> listener.onRecipeClicked(row.result));
        }
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        TextView title;

        HeaderHolder(View v) {
            super(v);
            title = v.findViewById(R.id.text_section_header);
        }
    }

    static class RecipeHolder extends RecyclerView.ViewHolder {
        TextView name, status;

        RecipeHolder(View v) {
            super(v);
            name = v.findViewById(R.id.text_recipe_name);
            status = v.findViewById(R.id.text_recipe_status);
        }
    }
}
