package com.example.fahrtenbuch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Drawer {

    Activity activity;

    // Declare the DrawerLayout, NavigationView, and Toolbar
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    Drawer(Activity ctx) {
        activity = ctx;

        // Initialize the DrawerLayout, Toolbar, and NavigationView
        drawerLayout = activity.findViewById(R.id.drawer_layout);
        toolbar = activity.findViewById(R.id.toolbar);
        navigationView = activity.findViewById(R.id.nav_view);

        // Create an ActionBarDrawerToggle to handle the drawer's open/close state
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle); // Add the toggle as a listener to the DrawerLayout
        toggle.syncState(); // Synchronize the toggle's state with the linked DrawerLayout

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {   // Called when an item in the NavigationView is selected.
                if (item.getItemId() == R.id.show) {    // Handle the selected item based on its ID
                    Toast.makeText(activity, "Zeige Fahrten Liste", Toast.LENGTH_SHORT).show();  // Show a Toast message for the Account item
                    try {
                        showList();
                    } catch (InterruptedException e) {
                        //throw new RuntimeException(e);
                        Log.d("Exception", "Show List Exception! mess=" + e.getMessage());
                    }
                }
                if (item.getItemId() == R.id.extra) {
                    Toast.makeText(activity, "Extra Optionen", Toast.LENGTH_SHORT).show();    // Show a Toast message for the Settings item
                    try {
                        showExtra();
                    } catch (InterruptedException e) {
                        Log.d("Exception", "Show Extra Exception! mess=" + e.getMessage());
                    }
                }
                if (item.getItemId() == R.id.input) {
                    Toast.makeText(activity, "Eingabe von Daten", Toast.LENGTH_SHORT).show();    // Show a Toast message for the Settings item
                    showInput();
                }
                if (item.getItemId() == R.id.help) {
                    Toast.makeText(activity, "Hier bekommst du noch keine Hilfe", Toast.LENGTH_SHORT).show();    // Show a Toast message for the Logout item
                    showHelp();
                }
                drawerLayout.closeDrawers();    // Close the drawer after selection
                return true;    // Indicate that the item selection has been handled
            }
        });

        ((ComponentActivity) activity).getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {    // Add a callback to handle the back button press
            @Override
            public void handleOnBackPressed() {  // Called when the back button is pressed.
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {   // Check if the drawer is open
                    drawerLayout.closeDrawer(GravityCompat.START);  // Close the drawer if it's open
                } else {
                    activity.finish();   // Finish the activity if the drawer is closed
                }
            }
        });
    }

    private void showExtra() throws InterruptedException {
        Log.d("Menu","Show Extra");
        Intent extra = new Intent(activity.getApplicationContext(), ExtraActivity.class);
        activity.startActivity(extra);
    }
    private void showList() throws InterruptedException {
        Log.d("Menu","Show List");
        Intent list = new Intent(activity.getApplicationContext(), ListActivity.class);
        activity.startActivity(list);
    }
    private void showHelp() {
        Log.d("Menu","Show Help");
        Intent help = new Intent(activity.getApplicationContext(), HelpActivity.class);
        activity.startActivity(help);
    }
    private void showInput() {
        Log.d("Menu","Show Input");
        Intent main = new Intent(activity.getApplicationContext(), MainActivity.class);
        activity.startActivity(main);
    }
}
