package com.example.smartpantry.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name;

    public String steps;

    public Recipe() {
        name = "";
    }
}
