package com.CareCircle.myapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CareCircleList extends AppCompatActivity {

    ListView listView;
    ArrayList<ContactItem> contactItems; // holds ID + display text for each contact
    ArrayAdapter<String> adapter;
    Database db;

    // Helper class to link database ID with display string
    static class ContactItem {
        int id;
        String display;
        ContactItem(int id, String display) {
            this.id = id;
            this.display = display;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_circle_list);

        listView = findViewById(R.id.careList);
        db = new Database(this);  // initialize database helper
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadContacts();  // refresh list every time activity resumes
    }

    private void loadContacts() {
        contactItems = new ArrayList<>();
        ArrayList<String> displayList = new ArrayList<>();

        // fetch all contacts from database
        Cursor cursor = db.getAllContactsCursor();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));

                ContactItem item = new ContactItem(id, name + " - " + phone);
                contactItems.add(item);
                displayList.add(item.display);

            } while (cursor.moveToNext() && contactItems.size() < 5); // limit to 5 contacts
        }
        cursor.close();

        if (contactItems.isEmpty()) {
            Toast.makeText(this, "No contacts found. Please add contacts.", Toast.LENGTH_SHORT).show();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        listView.setAdapter(adapter);

        // long press on a contact to delete it
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            ContactItem selected = contactItems.get(position);

            new AlertDialog.Builder(CareCircleList.this)
                    .setTitle("Delete Contact")
                    .setMessage("Do you really want to delete this contact?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (db.deleteContactById(selected.id)) {
                            Toast.makeText(CareCircleList.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                            contactItems.remove(position);
                            displayList.remove(position);
                            adapter.notifyDataSetChanged(); // refresh list after deletion
                        } else {
                            Toast.makeText(CareCircleList.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });
    }
}
