package com.example.fahrtenbuch;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the activity_main layout
        setContentView(R.layout.activity_main);
        Drawer drawer = new Drawer(this);

        Fahrtenbuch fb = new Fahrtenbuch(this);
    }
}
