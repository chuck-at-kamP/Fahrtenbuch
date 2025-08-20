package com.example.fahrtenbuch;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class FBExtra {

    Activity activity;
    private LinearLayout layout;
    private Button bLocation, bBackup, bRestore, bIntent, bFilesDir, bDirs, bFiles;
    private TextView tStatus;

    FBLocation fbLocation;

    FBExtra(Activity ctx) {
        activity = ctx;

        layout = activity.findViewById(R.id.extraLayout);
        bLocation = activity.findViewById(R.id.button_extra_location);
        bBackup   = activity.findViewById(R.id.button_extra_backup);
        bRestore  = activity.findViewById(R.id.button_extra_restore);
        bIntent   = activity.findViewById(R.id.button_extra_intent);
        bFilesDir = activity.findViewById(R.id.button_extra_files_dir);
        bDirs     = activity.findViewById(R.id.button_extra_dirs);
        bFiles    = activity.findViewById(R.id.button_extra_files);
        bIntent   = activity.findViewById(R.id.button_extra_intent);
        tStatus   = activity.findViewById(R.id.extra_status);

        fbLocation = new FBLocation(activity);

        bLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FBExtra buttonLocation()", "Location Button clicked");
                double[] location = fbLocation.getCurrentLocation2();
                String[] address = fbLocation.getAddress(location[0],location[1]);
                String result = "Location " + String.valueOf(location[0]) + "/" + String.valueOf(location[1]) + "\n";
                result += "City: " + address[0] + "\nAddress 1: " + address[1] + "\nAddress 2: " + address[2] + "\n";
                tStatus.setText(result);
            }
        });
        bBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FBExtra buttonBackup()", "Backup Button clicked");
                FBSave fbSave = new FBSave(activity,"Fahrtenbuch.csv");
                String[] res = fbSave.backup();
                if(res == null) {
                    tStatus.setText("File copy failed! file=" + "Fahrtenbuch.csv");
                    return;
                }
                tStatus.setText("Backup\nfrom: " + res[0] + "\nto  : " + res[1]);
            }
        });
        bRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FBExtra buttonLocation()", "Location Button clicked");
                FBSave fbSave = new FBSave(activity,"Fahrtenbuch.csv");
                String[] res = fbSave.restore();
                tStatus.setText("Restore\nfrom: " + res[0] + "\nto  : " + res[1]);
            }
        });
        bIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FBExtra buttonLocation()", "Location Button clicked");
                tStatus.setText("Not implemented yet!");
            }
        });
        bFilesDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FBExtra buttonFilesDir()", "Files Dir Button clicked");
                FBFile fbFile = new FBFile("test",activity);
                List<String> list = fbFile.getFilesDir();
                String listString = String.join("\n", list);
                tStatus.setText(listString);
            }
        });
        bDirs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FBExtra buttonDirs()", "Dirs Button clicked");
                FBFile fbFile = new FBFile("test",activity);
                List<String> list = fbFile.getDirs();
                String listString = String.join("\n", list);
                tStatus.setText(listString);
            }
        });
        bFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FBExtra buttonFiles()", "Files Button clicked");
                FBFile fbFile = new FBFile("test",activity);
                List<String> list = fbFile.getFiles();
                String listString = String.join("\n", list);
                tStatus.setText(listString);
            }
        });
    }

    private void message(String txt) {
        Toast.makeText(activity,txt,Toast.LENGTH_SHORT).show();
    }
}
