package com.example.fahrtenbuch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ExtraActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        Drawer drawer = new Drawer(this);
        FBExtra fbExtra = new FBExtra(this);
    }
}
