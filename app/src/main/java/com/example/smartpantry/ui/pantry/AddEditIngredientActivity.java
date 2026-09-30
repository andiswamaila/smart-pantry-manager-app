package com.example.smartpantry.ui.pantry;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.data.AppDatabase;
import com.example.smartpantry.data.AppExecutors;
import com.example.smartpantry.data.PantryItem;
import com.example.smartpantry.data.PantryItemDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Add / Edit Ingredient screen. Reached via Intent from the Pantry screen
 * (with EXTRA_ITEM_ID for editing, or without it for adding).
 */
public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "com.example.smartpantry.ITEM_ID";

    private EditText editName, editQuantity;
    private Spinner spinnerUnit;
    private TextView expiryLabel;
    private PantryItemDao dao;

    private long itemId = -1;
    private Long expiryDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);
        dao = AppDatabase.getInstance(this).pantryItemDao();

        editName = findViewById(R.id.edit_name);
        editQuantity = findViewById(R.id.edit_quantity);
        spinnerUnit = findViewById(R.id.spinner_unit);
        expiryLabel = findViewById(R.id.text_expiry_label);
        Button buttonSave = findViewById(R.id.button_save);
        Button buttonDelete = findViewById(R.id.button_delete);

        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(
                this, R.array.units_array, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(unitAdapter);

        // Read the passed-in id to decide between add and edit mode.
        itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            setTitle("Edit ingredient");
            buttonDelete.setVisibility(Button.VISIBLE);
            loadExistingItem();
        } else {
            setTitle("Add ingredient");
        }

        findViewById(R.id.button_pick_expiry).setOnClickListener(v -> showDatePicker());
        findViewById(R.id.button_clear_expiry).setOnClickListener(v -> {
            expiryDate = null;
            updateExpiryLabel();
        });

        buttonSave.setOnClickListener(v -> attemptSave());
        buttonDelete.setOnClickListener(v -> confirmDelete());
    }

    private void loadExistingItem() {
        AppExecutors.diskIO().execute(() -> {
            PantryItem item = dao.getByIdSync(itemId);
            runOnUiThread(() -> {
                if (item == null) return;
                editName.setText(item.name);
                editQuantity.setText(String.valueOf(item.quantity));
                setSpinnerSelection(item.unit);
                expiryDate = item.expiryDate;
                updateExpiryLabel();
            });
        });
    }

    private void setSpinnerSelection(String unit) {
        for (int i = 0; i < spinnerUnit.getCount(); i++) {
            if (spinnerUnit.getItemAtPosition(i).toString().equalsIgnoreCase(unit)) {
                spinnerUnit.setSelection(i);
                return;
            }
        }
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        if (expiryDate != null) cal.setTimeInMillis(expiryDate);
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar chosen = Calendar.getInstance();
            chosen.set(year, month, dayOfMonth, 0, 0, 0);
            expiryDate = chosen.getTimeInMillis();
            updateExpiryLabel();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateExpiryLabel() {
        if (expiryDate == null) {
            expiryLabel.setText("Expiry date: none set");
        } else {
            SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            expiryLabel.setText("Expiry date: " + fmt.format(new Date(expiryDate)));
        }
    }

    /** Input validation before any database write happens. */
    private void attemptSave() {
        String name = editName.getText().toString().trim();
        String qtyText = editQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)) {
            editName.setError("Ingredient name is required");
            return;
        }
        double qty;
        try {
            qty = Double.parseDouble(qtyText);
        } catch (NumberFormatException e) {
            editQuantity.setError("Enter a valid quantity");
            return;
        }
        if (qty <= 0) {
            editQuantity.setError("Quantity must be greater than zero");
            return;
        }

        AppExecutors.diskIO().execute(() -> {
            if (itemId == -1) {
                dao.insert(new PantryItem(name, qty, unit, expiryDate));
            } else {
                PantryItem existing = dao.getByIdSync(itemId);
                if (existing != null) {
                    existing.name = name;
                    existing.quantity = qty;
                    existing.unit = unit;
                    existing.expiryDate = expiryDate;
                    dao.update(existing);
                }
            }
            runOnUiThread(() -> {
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage("Remove this ingredient from your pantry?")
                .setPositiveButton("Delete", (d, w) -> AppExecutors.diskIO().execute(() -> {
                    PantryItem existing = dao.getByIdSync(itemId);
                    if (existing != null) dao.delete(existing);
                    runOnUiThread(this::finish);
                }))
                .setNegativeButton("Cancel", null)
                .show();
    }
}
