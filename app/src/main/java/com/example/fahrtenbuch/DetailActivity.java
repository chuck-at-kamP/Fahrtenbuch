package com.example.fahrtenbuch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Drawer drawer = new Drawer(this);
        FBDetail fbListe = new FBDetail(this);
    }
}
