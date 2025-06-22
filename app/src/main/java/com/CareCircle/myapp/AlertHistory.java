package com.CareCircle.myapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class AlertHistory extends AppCompatActivity {
    TextView historyView;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_history);

        historyView = findViewById(R.id.historyText);
        db = new Database(this);

        ArrayList<String> historyList = db.getAllCheckIns();

        if (historyList.isEmpty()) {
            historyView.setText("No alert history found.");
        } else {
            // Join all entries with double line breaks for readability
            StringBuilder historyText = new StringBuilder();
            for (String entry : historyList) {
                historyText.append(entry).append("\n\n");
            }
            historyView.setText(historyText.toString().trim());
        }
    }
}
