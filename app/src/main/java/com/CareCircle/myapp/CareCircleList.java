package com.CareCircle.myapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CareCircleList extends AppCompatActivity {
    ListView listView;
    ArrayList<String> contacts;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_circle_list);

        listView = findViewById(R.id.careList);
        db = new Database(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        contacts = db.getAllContacts();

        if (contacts.isEmpty()) {
            Toast.makeText(this, "No contacts found. Please add contacts.", Toast.LENGTH_SHORT).show();
        }

        // Limit to 5 max
        List<String> limitedContacts = contacts.size() > 5
                ? contacts.subList(0, 5)
                : contacts;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, limitedContacts);
        listView.setAdapter(adapter);
    }
}
