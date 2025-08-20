package com.example.fahrtenbuch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Drawer drawer = new Drawer(this);
        FBListe fbListe = new FBListe(this);
    }
}
