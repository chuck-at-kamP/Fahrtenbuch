package com.example.fahrtenbuch;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;

import java.util.HashMap;

public class FBDetail {
    Activity activity;

    TextView actDate, actLocation, type, type1, type2, begTime, endTime, resTime, begKm, endKm, resKm, track, begLon, endLon, begLat, endLat, begCity, endCity, begLoc, endLoc;

    FBDetail(Activity ctx) {
        activity = ctx;
        initializeViews();

        Bundle extras = activity.getIntent().getExtras();
        if (extras != null) {
            HashMap<String, String> map = (HashMap) extras.getSerializable("map");
            String typ = map.get("type");
            Log.d("FBDetail()","Details for " + typ);
            type.setText(typ);
            type1.setText(typ);
            type2.setText(typ);
            actDate.setText(map.get("beg_time"));
            actLocation.setText(map.get("end_city"));
            begTime.setText(map.get("beg_time"));
            endTime.setText(map.get("end_time"));
            resTime.setText(map.get("duration"));
            begKm.setText(map.get("beg_km"));
            endKm.setText(map.get("end_km"));
            resKm.setText(map.get("distance"));
            if(typ.equals("Laden")) {
                TextView cellTitleKm = activity.findViewById(R.id.ele_2_1);
                TextView cellTitleTrack = activity.findViewById(R.id.ele_track);
                cellTitleKm.setText("kWh");
                cellTitleTrack.setText("Laden");
                track.setText("Laden");
                track.setClickable(false);
                track.setBackgroundColor(Color.argb(255, 200, 200, 200));;
            }
            else if(typ.equals("Fahrt")) {
                track.setText(map.get("track"));
            }
            begLon.setText(map.get("beg_long"));
            endLon.setText(map.get("end_long"));
            begLat.setText(map.get("beg_lat"));
            endLat.setText(map.get("end_lat"));
            begCity.setText(map.get("beg_city"));
            endCity.setText(map.get("end_city"));
            String beg_loc_tmp = map.get("beg_loc");
            String end_loc_tmp = map.get("end_loc");
            String beg_loc = beg_loc_tmp.replace(",", "\n");
            String end_loc = end_loc_tmp.replace(",", "\n");
            begLoc.setText(beg_loc);
            endLoc.setText(end_loc);

            if(typ.equals("Fahrt")) {
                track.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        trackClicked(v, activity);
                    }
                });
            }
        }
    }

    private void initializeViews() {
        actDate = activity.findViewById(R.id.actDate);
        actLocation = activity.findViewById(R.id.actLocation);
        type = activity.findViewById(R.id.ele_0_2);
        type1 = activity.findViewById(R.id.ele_0_3);
        type2 = activity.findViewById(R.id.ele_0_4);
        begTime = activity.findViewById(R.id.ele_1_2);
        endTime = activity.findViewById(R.id.ele_1_3);
        resTime = activity.findViewById(R.id.ele_1_4);
        begKm = activity.findViewById(R.id.ele_2_2);
        endKm = activity.findViewById(R.id.ele_2_3);
        resKm = activity.findViewById(R.id.ele_2_4);
        track = activity.findViewById(R.id.ele_track_file);
        begLon = activity.findViewById(R.id.ele_3_2);
        endLon = activity.findViewById(R.id.ele_3_3);
        begLat = activity.findViewById(R.id.ele_4_2);
        endLat = activity.findViewById(R.id.ele_4_3);
        begCity = activity.findViewById(R.id.ele_5_2);
        endCity = activity.findViewById(R.id.ele_5_3);
        begLoc = activity.findViewById(R.id.ele_6_2);
        endLoc = activity.findViewById(R.id.ele_6_3);
    }

    public void trackClicked(View v, Activity activity) {
        Log.d("FBDetail trackClicked()", "Track Clicked!");
        String filename = String.valueOf(((TextView) v).getText());

        String path = "content:///data/data/com.example.fahrtenbuch/files/";
        String file = path + filename;
        Uri track = Uri.parse(file);
        Log.d("FBDetail trackClicked()", "Track open! file=" + file);
        // FBTrack example = new FBTrack(filename, activity);

        try {
            popupMenu(v,filename);

        } catch (Exception e) {
            String mess = e.getMessage();
            Log.d("FBDetail trackClicked()", "Intent Exception occured! mess=" + mess);
            e.printStackTrace();
        }
        Log.d("FBDetail trackClicked()", "Track clicked! file=" + file);
    }

    public void popupMenu(View view, String filename) {
        PopupMenu popupMenu = new PopupMenu(activity, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_external, popupMenu.getMenu());  // Inflating popup menu from popup_menu.xml file

        // Handling menu item click events
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            String title = String.valueOf(menuItem.getTitle());
            Toast.makeText(activity, "You Clicked " + title, Toast.LENGTH_SHORT).show();

            if(title.equals("Editor Markor")) startEditor(filename);
            else if(title.equals("Map GPXSee")) startGpxSee(filename);
            else if(title.equals("Map OSMand")) startOsmand(filename);
            else if(title.equals("Map OSMTracker")) startOsmtracker(filename);
            return true;
        });
        popupMenu.show(); // Showing the popup menu
    }

    public void startEditor(String filename) {
        FBExternal ext = new FBExternal(activity);
        ext.openMarkorEditor(filename); // OK

        // ext.printAppsActionView();
        // ext.openEditor(track); // NOK
        // ext.openMapView(filename);
    }
    public void startGpxSee(String filename) {
        FBExternal ext = new FBExternal(activity);
        ext.openMapView(filename,"org.gpxsee.gpxsee"); // OK
    }
    public void startOsmand(String filename) {
        FBExternal ext = new FBExternal(activity);
        ext.openMapView(filename,"net.osmand.plus"); // OK
    }
    public void startOsmtracker(String filename) {
        FBExternal ext = new FBExternal(activity);
        ext.openMapView(filename,"net.osmtracker"); // OK
    }
}
