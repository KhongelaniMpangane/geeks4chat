package com.example.geeks4chat.activties;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geeks4chat.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sample contact data
        String[] contacts = {"Contact 1", "Contact 2", "Contact 3"};

        // Set up the ListView and ArrayAdapter
        ListView listViewContacts = findViewById(R.id.listViewContacts);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        listViewContacts.setAdapter(adapter);
    }
}

