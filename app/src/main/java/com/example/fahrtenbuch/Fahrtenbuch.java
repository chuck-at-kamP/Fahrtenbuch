package com.example.fahrtenbuch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Fahrtenbuch {

    // UI Elements -----------------------------
    private Button startButton, clearButton, stopButton, saveButton;
    private RadioButton radioFahrtButton, radioLadenButton;
    private RadioGroup radioGroup;
    private TextView actDate, actCity, actLocation;
    private TextView cellTypeBeg, cellTimeBeg, cellLatBeg, cellLonBeg, cellCityBeg, cellAddrBeg;
    private TextView cellTypeEnd, cellTimeEnd, cellLatEnd, cellLonEnd, cellCityEnd, cellAddrEnd;
    private TextView cellKm, statusTrack, cellDiff, cellLength;
    private EditText editKmBeg, editKmEnd;
    MainActivity context;

    // Fahrtenbuch Data ----------------------------------
    String filename = "Fahrtenbuch.csv";
    FBFile file;
    List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    Map<String, String> map = new HashMap<>();
    boolean started = false;    // only "Save" when "started" and "stopped"
    boolean stopped = false;
    String lastSaved = null;
    enum Answer { YES, NO, ERROR, NONE };
    static Answer choice;
    String radioString;

    // Location Data ------------------------------------
    String filename_track;
    GPX gpx;
    FBTrack track;
    FBFile file_track;
    FBLocation fbLocation;
    LocationManager locationManager;
    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    // ---------- Timer
    private Timer timer;
    private LocalDateTime startDate;
    private long duration = 0;

    Fahrtenbuch(MainActivity ctx) {
        context = ctx;
        file = new FBFile(filename, context); // Data
        fbLocation = new FBLocation(context); // Location
        data.add(map);
        initializeUI();
        initializeListener();
        radioString = "Fahrt";
    }
    public void initializeUI() {
        // ----------------- Widgets -------------------
        startButton = context.findViewById(R.id.button4);
        clearButton = context.findViewById(R.id.button_clear);
        stopButton = context.findViewById(R.id.button6);
        saveButton = context.findViewById(R.id.button);
        radioFahrtButton = context.findViewById(R.id.radioButton3);
        radioLadenButton = context.findViewById(R.id.radioButton4);
        radioGroup = (RadioGroup) context.findViewById(R.id.radioGroup);
        actDate = context.findViewById(R.id.actDate);
        actCity = context.findViewById(R.id.actCity);
        actLocation = context.findViewById(R.id.actLocation);

        editKmBeg = context.findViewById(R.id.ele_2_2);
        editKmEnd = context.findViewById(R.id.ele_2_3);

        cellKm = context.findViewById(R.id.ele_2_1);

        cellTypeBeg = context.findViewById(R.id.ele_0_2);
        cellTimeBeg = context.findViewById(R.id.ele_1_2);
        cellLatBeg = context.findViewById(R.id.ele_3_2);
        cellLonBeg = context.findViewById(R.id.ele_4_2);
        cellCityBeg = context.findViewById(R.id.ele_5_2);
        cellAddrBeg = context.findViewById(R.id.ele_6_2);
        statusTrack = context.findViewById(R.id.ele_track_file);

        cellTypeEnd = context.findViewById(R.id.ele_0_3);
        cellTimeEnd = context.findViewById(R.id.ele_1_3);
        cellLatEnd = context.findViewById(R.id.ele_3_3);
        cellLonEnd = context.findViewById(R.id.ele_4_3);
        cellCityEnd = context.findViewById(R.id.ele_5_3);
        cellAddrEnd = context.findViewById(R.id.ele_6_3);
        cellDiff = context.findViewById(R.id.ele_1_4);
        cellLength = context.findViewById(R.id.ele_2_4);

        stopButton.setEnabled(false);
        clearButton.setEnabled(false);
        saveButton.setEnabled(false);
        radioFahrtButton.setChecked(true);
        editKmBeg.getBackground().setColorFilter(Color.parseColor("#0000FF"), PorterDuff.Mode.SRC_ATOP);
    }

    public void initializeListener() {
        fbLocation.setListener(new FBLocation.FBLocationListener() {
            @Override
            public void locationChangedStart(double lat, double lon, float acc, double alt) {
                Log.d("Fahrtenbuch locationChangedStart()","Got Location!");
                String locationString = String.format("%f/%f (%f/%f)", lat,lon,acc,alt);
                actLocation.setText(locationString);
                String[] address = fbLocation.getAddress(lon,lat);
                actCity.setText(address[2]);

                cellLatBeg.setText(String.valueOf(lat));
                cellLonBeg.setText(String.valueOf(lon));
                cellCityBeg.setText(address[0]);
                cellAddrBeg.setText(address[2]);

                map.put("beg_city", address[0]);
                map.put("beg_loc", address[1]);
                map.put("beg_long", String.valueOf(lon));
                map.put("beg_lat", String.valueOf(lat));

                // ---------------- Set Track ------------------------------------
                if(radioString.equals("Fahrt")) {
                    String[] currentDateString = Util.getDate();
                    gpx.add(currentDateString[1], String.valueOf(lat), String.valueOf(lon),String.valueOf(acc),String.valueOf(alt));
                }
            }
            @Override
            public void locationChangedStop(double lat, double lon, float acc, double alt) {
                Log.d("Fahrtenbuch locationChangedStop()", "Got Location!");
                String locationString = String.format("%f/%f (%f/%f)", lat,lon,acc,alt);
                actLocation.setText(locationString);
                String[] address = fbLocation.getAddress(lon,lat);
                actCity.setText(address[2]);

                cellLatEnd.setText(String.valueOf(String.valueOf(lat)));
                cellLonEnd.setText(String.valueOf(String.valueOf(lon)));
                cellCityEnd.setText(address[0]);
                cellAddrEnd.setText(address[2]);

                map.put("end_city", address[0]);
                map.put("end_loc", address[1]);
                map.put("end_long", String.valueOf(String.valueOf(lon)));
                map.put("end_lat", String.valueOf(String.valueOf(lat)));
                // ---------------- Set Track ------------------------------------
                if(radioString.equals("Fahrt")) {
                    String[] currentDateString = Util.getDate();
                    gpx.add(currentDateString[1], String.valueOf(lat), String.valueOf(lon),String.valueOf(acc),String.valueOf(alt));
                }
            }
            @Override
            public void locationChangedTimer(double lat, double lon, float acc, double alt) {
                Log.d("Fahrtenbuch locationChangedTimer()", "Got Location!");
                String locationString = String.format("%f/%f (%f/%f)", lat,lon,acc,alt);
                actLocation.setText(locationString);
                String[] address = fbLocation.getAddress(lon,lat);
                actCity.setText(address[2]);
                // ---------------- Set Track ------------------------------------
                String[] currentDateString = Util.getDate();
                gpx.add(currentDateString[1],String.valueOf(lat),String.valueOf(lon),String.valueOf(acc),String.valueOf(alt));
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button", "Save Button clicked");
                startButton.setEnabled(true);
                clearButton.setEnabled(false);
                stopButton.setEnabled(false);
                saveButton.setEnabled(false);

                String radioString = readRadioButton(R.id.ele_0_3);

                file.save(map);

                if(radioString.equals("Fahrt")) {
                    track.writeGpx(gpx);
                    message("File saved! track=" + filename_track);
                }
                else {
                    message("File saved!");
                }
                // ------------ Say Saved ---------------
                String beg = map.get("beg_time");
                lastSaved = beg;
                actDate.setText("Saved " + radioString);
                actCity.setText(beg);
                if(radioString.equals("Fahrt")) {
                    String trk = map.get("track");
                    actLocation.setText(trk);
                }
                if(radioString.equals("Laden")) {
                    actLocation.setText("");
                }
                // ------------ Clear fields ---------------
                map.clear();
                clearBegFields(radioString);
                clearEndFields(radioString);
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button", "Start Button clicked");

               // -------------- KM-Stand von Hand in Editor Feld eingeben ----------------
                String beginKm = editKmBeg.getText().toString().trim();
                if(Util.isNumeric(beginKm)) {
                    int beginKmInt = Integer.valueOf(beginKm);
                    if (beginKmInt >= 0) {
                        Log.d("Edit Text", "Text Km=" + beginKm);
                        editKmBeg.getBackground().setColorFilter(Color.parseColor("#00ff00"), PorterDuff.Mode.SRC_ATOP);
                        editKmEnd.getBackground().setColorFilter(Color.parseColor("#0000ff"), PorterDuff.Mode.SRC_ATOP);
                    }
                } else {
                    editKmBeg.getBackground().setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.SRC_ATOP);
                    if(radioString.equals("Fahrt")) alert("Setze zuerst den KM-Stand!");
                    else if(radioString.equals("Laden")) alert("Setze zuerst den Zählerstand in kWh!");
                    return;
                }

                startButton.setEnabled(false);
                clearButton.setEnabled(false);
                stopButton.setEnabled(true);
                saveButton.setEnabled(false);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startDate = LocalDateTime.now();
                }
                String[] currentDateString = Util.getDate();
                cellTimeBeg.setText(currentDateString[0]);
                actDate.setText(currentDateString[0]);

                fbLocation.getCurrentLocation("start"); // Trigger Location

                if(radioString.equals("Fahrt")) {
                    // -------------- Location Track  ------------------
                    filename_track = Util.createFilename(currentDateString[0]);
                    // message("Track to file! file=" + filename_track);
                    Log.d("FB", "Track to file! file=" + filename_track);
                    gpx = new GPX(filename_track);
                    track = new FBTrack(filename_track, context);
                    statusTrack.setText(filename_track);

                    // ------------------- Start Timer -------------------------
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() { TimerMethod();} }, 0, 60000);
                }
                // --------------- Daten speichern ----------------------
                map.put("beg_time", currentDateString[0]);
                map.put("beg_km", beginKm);
                map.put("type", radioString);
                if(radioString.equals("Fahrt")) {
                    map.put("track", filename_track);
                }
                started = true;
                message("Aufzeichnung gestartet!"); // Meldung
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Fahrtenbuch stopButton()", "Stop Button clicked");

                // -------------- KM-Stand von Hand in Editor Feld eingeben ----------------
                String endKm = editKmEnd.getText().toString().trim();
                int endKmInt = 0;
                if(Util.isNumeric(endKm)) {
                    endKmInt = Integer.valueOf(endKm);
                    if (endKmInt >= 0) {
                        Log.d("Edit Text", "Text Km=" + endKm);
                        editKmEnd.getBackground().setColorFilter(Color.parseColor("#00ff00"), PorterDuff.Mode.SRC_ATOP);
                    }
                } else {
                    editKmEnd.getBackground().setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.SRC_ATOP);
                    if(radioString.equals("Fahrt")) alert("Setze zuerst den KM-Stand!");
                    else if(radioString.equals("Laden")) alert("Setze zuerst den Zählerstand in kWh!");
                    return;
                }
                startButton.setEnabled(false);
                stopButton.setEnabled(false);
                clearButton.setEnabled(true);
                saveButton.setEnabled(true);

                // ----------------- Get Time ---------------------------
                LocalDateTime act = null;
                long diff = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    act = LocalDateTime.now();
                    diff = Math.abs(act.toEpochSecond(ZoneOffset.of("Z")) - startDate.toEpochSecond(ZoneOffset.of("Z")));
                }
                duration = diff;
                String timeString = Util.timestamp(diff);
                actDate.setText(timeString);
                String[] currentDateString = Util.getDate();

                cellTimeEnd.setText(currentDateString[0]);
                cellDiff.setText(Util.timestamp(duration));

                // ----------------- Get Lon/Lat -------------------
                fbLocation.getCurrentLocation("stop");

                if(radioString.equals("Fahrt")) {
                    timer.cancel();
                }
                // ----------------------- Set Data -----------------
                map.put("end_time", currentDateString[0]);
                map.put("end_km", endKm);
                map.put("duration", Util.timestamp(duration));

                int begKmInt = Integer.valueOf(map.get("beg_km"));
                int lengthInt = endKmInt - begKmInt;
                String length = String.valueOf(lengthInt);
                map.put("distance", length);
                cellLength.setText(length);

                stopped = true;
                message("Aufzeichnung beendet!");

                if(started && stopped) {
                    saveButton.setEnabled(true);
                }
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Fahrtenbuch clearButton()", "Clear Button clicked");
                radioString = readRadioButton(R.id.ele_0_3);
                map.clear();
                clearBegFields(radioString);
                clearEndFields(radioString);
                Log.d("Fahrtenbuch clearButton()","Data cleared!");
                message("Data cleared!");
                startButton.setEnabled(true);
                clearButton.setEnabled(false);
                stopButton.setEnabled(false);
                saveButton.setEnabled(false);
            }
        });
        radioFahrtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button", "Radio Fahrt Button clicked");
                message("Type 'Fahrt' ausgewählt!");
                radioString = "Fahrt";
                clearBegFields("Fahrt");
                clearEndFields("Fahrt");
                cellTypeBeg.setText("Fahrt");
                cellTypeEnd.setText("Fahrt");
                cellKm.setText("km");
                editKmBeg.setText("");
                editKmEnd.setText("");
                editKmBeg.setHint("km-Stand");
                editKmEnd.setHint("km-Stand");
            }
        });
        radioLadenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button", "Radio Laden Button clicked");
                message("Type 'Laden' ausgewählt!");
                radioString = "Laden";
                clearBegFields("Laden");
                clearEndFields("Laden");
                cellTypeBeg.setText("Laden");
                cellTypeEnd.setText("Laden");
                cellKm.setText("kWh");
                editKmBeg.setText("");
                editKmEnd.setText("");
                editKmBeg.setHint("Stromzähler-Stand");
                editKmEnd.setHint("Stromzähler-Stand");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(context.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private String readRadioButton(int id) {
        // -------------- Radio Button auslesen ----------------------
        final String radioString =
                ((RadioButton) context.findViewById(radioGroup.getCheckedRadioButtonId()))
                        .getText().toString();
        TextView cell = context.findViewById(id);
        cell.setText(radioString);
        return radioString;
    }
    private void clearBegFields(String radioString) {
        String empty = "---";
        cellTypeBeg.setText(empty);
        cellTimeBeg.setText(empty);
        cellLatBeg.setText(empty);
        cellLonBeg.setText(empty);
        cellCityBeg.setText(empty);
        cellAddrBeg.setText(empty);
        statusTrack.setText(empty);
        if(radioString.equals("Fahrt")) {
            cellKm.setText("km");
            editKmBeg.setText("");
            editKmBeg.setHint("km-Stand");
        }
        else if(radioString.equals("Laden")) {
            cellKm.setText("kWh");
            editKmBeg.setText("");
            editKmBeg.setHint("Zählerstand");
        }
        // actDate.setText("Start Time");
        // actLocation.setText("Location");
        // actCity.setText("Location");
    }

    private void clearEndFields(String radioString) {
        String empty = "---";
        cellTypeEnd.setText(empty);
        cellTimeEnd.setText(empty);
        cellLatEnd.setText(empty);
        cellLonEnd.setText(empty);
        cellDiff.setText(empty);
        cellLength.setText(empty);
        cellCityEnd.setText(empty);
        cellAddrEnd.setText(empty);
        if(radioString.equals("Fahrt")) {
            editKmEnd.setText("");
            editKmEnd.setHint("km-Stand");
        }
        else if(radioString.equals("Laden")) {
            editKmEnd.setText("");
            editKmEnd.setHint("Stromzähler-Stand");
        }
    }

    private void TimerMethod() {
        context.runOnUiThread(Timer_Tick);
    }
    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            LocalDateTime act = null; // Get Time
            long diff = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                act = LocalDateTime.now();
                diff = Math.abs(act.toEpochSecond(ZoneOffset.of("Z")) - startDate.toEpochSecond(ZoneOffset.of("Z")));
            }
            duration = diff;
            String timeString = Util.timestamp(diff);
            actDate.setText(timeString);
            fbLocation.getCurrentLocation("timer"); // Trigger Location
        }
    };

    private void alert(String txt) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(txt);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d("Fahrtenbuch alert()","Listener Yes");
                dialog.cancel();
            } });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void message(String txt) {
        Toast.makeText(context,txt,Toast.LENGTH_SHORT).show();
    }
}