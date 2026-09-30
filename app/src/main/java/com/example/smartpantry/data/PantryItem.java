package com.example.smartpantry.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pantry_items")
public class PantryItem {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name;

    public double quantity;

    @NonNull
    public String unit;

    /** Expiry date as epoch millis, or null if not set. */
    public Long expiryDate;

    public PantryItem() {
        name = "";
        unit = "pcs";
    }

    public PantryItem(@NonNull String name, double quantity, @NonNull String unit, Long expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }
}
