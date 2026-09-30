package com.example.smartpantry.ui.pantry;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Pantry List screen: shows all current ingredients from the database in a
 * RecyclerView, with FAB to add and tap-to-edit / delete actions.
 */
public class PantryFragment extends Fragment implements PantryAdapter.OnItemActionListener {

    private PantryAdapter adapter;
    private TextView emptyText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        emptyText = view.findViewById(R.id.text_pantry_empty);
        RecyclerView recycler = view.findViewById(R.id.recycler_pantry);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PantryAdapter(this);
        recycler.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_ingredient);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddEditIngredientActivity.class);
            startActivity(intent);
        });

        AppDatabase.getInstance(requireContext())
                .pantryItemDao()
                .getAll()
                .observe(getViewLifecycleOwner(), this::onPantryChanged);

        return view;
    }

    private void onPantryChanged(List<PantryItem> items) {
        adapter.submitList(items);
        emptyText.setVisibility(items == null || items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClicked(PantryItem item) {
        // Pass the record id to the edit screen via an Intent extra.
        Intent intent = new Intent(getActivity(), AddEditIngredientActivity.class);
        intent.putExtra(AddEditIngredientActivity.EXTRA_ITEM_ID, item.id);
        startActivity(intent);
    }

    @Override
    public void onItemDeleted(PantryItem item) {
        AppExecutors.diskIO().execute(() ->
                AppDatabase.getInstance(requireContext()).pantryItemDao().delete(item));
    }
}
