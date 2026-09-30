package com.example.smartpantry.ui.pantry;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.data.PantryItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    public interface OnItemActionListener {
        void onItemClicked(PantryItem item);

        void onItemDeleted(PantryItem item);
    }

    private List<PantryItem> items;
    private final OnItemActionListener listener;

    public PantryAdapter(OnItemActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<PantryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryItem item = items.get(position);
        holder.name.setText(item.name);
        holder.qty.setText(formatQuantity(item.quantity) + " " + item.unit);

        if (item.expiryDate != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(holder.itemView.getContext());
            boolean alerts = prefs.getBoolean("expiring_alerts", true);
            int warnDays = Integer.parseInt(prefs.getString("expiry_days", "3"));

            long now = System.currentTimeMillis();
            long warnThreshold = now + warnDays * 24L * 60 * 60 * 1000;
            SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            holder.expiry.setText("Expires: " + fmt.format(new Date(item.expiryDate)));
            holder.expiry.setVisibility(View.VISIBLE);

            if (alerts && item.expiryDate < warnThreshold) {
                // Expired or expiring soon -> amber/red highlight
                holder.expiry.setTextColor(item.expiryDate < now
                        ? Color.parseColor("#C62828")
                        : Color.parseColor("#F57C00"));
            } else {
                holder.expiry.setTextColor(Color.GRAY);
            }
        } else {
            holder.expiry.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClicked(item));
        holder.delete.setOnClickListener(v -> listener.onItemDeleted(item));
    }

    private String formatQuantity(double q) {
        return q == Math.floor(q) ? String.valueOf((long) q) : String.valueOf(q);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, qty, expiry;
        ImageButton delete;

        ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.text_item_name);
            qty = v.findViewById(R.id.text_item_qty);
            expiry = v.findViewById(R.id.text_item_expiry);
            delete = v.findViewById(R.id.button_delete_item);
        }
    }
}
