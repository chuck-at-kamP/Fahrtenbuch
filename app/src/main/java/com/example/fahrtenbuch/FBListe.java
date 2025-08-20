package com.example.fahrtenbuch;

import static android.view.View.VISIBLE;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.*;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.res.ResourcesCompat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FBListe {

    private TableLayout layout;
    Activity activity;
    String filename = "Fahrtenbuch.csv";
    FBFile file;
    List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    FBListe(Activity ctx) {
        activity = ctx;

        layout = activity.findViewById(R.id.table);
        if(layout == null) {
            Log.w("FBListe()","Layout Liste is null!");
            return;
        }

        // ----------------- Data ----------------------
        file = new FBFile(filename, activity);
        file.load(data);
        message("List loaded! nr=" + data.size());
        Log.d("FBListe()", "List loaded! nr=" + data.size());

        for(int i = 0; i < data.size(); i++) {
            Map<String, String> map = data.get(i);
            Log.v("FBListe()", "(" + i + ") Data: " + map.toString());
            String type = map.get("type");
            String time = map.get("beg_time");
            String city = map.get("end_city");
            String dur  = map.get("duration");
            String dist = map.get("distance");
            Log.v("FBListe()", "(" + i + ") List entry: " + time + ", " + dur + ", " + dist + ", " + city + ", " + type);
            TableRow row = createRow(String.valueOf(i),time,type,city,dur,dist);
            if(row == null) {
                Log.w("FBListe()","(" + i + ") Row is null!");
                continue;
            }
            if(type.equals("Fahrt")) {
                row.setBackgroundColor(0xFFFFFFFF);
            }
            else if(type.equals("Laden")) {
                row.setBackgroundColor(0xFFA9FFA9);
            }

            Log.v("FBListe()", "(" + i + ") Add row to layout! time=" + time + ", city=" + city + ", dur=" + dur + ", dist=" + dist);
            layout.addView(row);

        }
        Log.d("FBListe()", "Inflate List list=" + layout.toString());
        layout.setBackgroundColor(0xFFDDDDDD);
        layout.invalidate();
    }


    public void listEntryClicked(View v, Activity activity) {

        String text = String.valueOf(((TextView) v).getText());
        Integer index = Integer.valueOf(text);
        Log.d("FBListe listEntryClicked()", "List Entry Clicked. index=" + index);

        Map<String,String> entry = data.get(index);
        String city = entry.get("end_city");

        Intent details = new Intent(activity.getApplicationContext(), DetailActivity.class);
        details.putExtra("map",(Serializable) entry);
        activity.startActivity(details);

        Log.d("FBListe listEntryClicked()", "List Entry clicked. index=" + text + ", city=" + city);
    }


    private TableRow createRow(String nr, String time, String type, String city, String dur, String len) {
        TableRow row = new TableRow(activity);
        TextView col0 = createText(nr, 3);
        TextView col1 = createText(time,2);
        TextView col2 = createText(type,1);
        TextView col3 = createText(city, 1);
        TextView col4 = createText(dur, 1);
        TextView col5 = createText(len, 1);
        row.addView(col0);
        row.addView(col1);
        row.addView(col2);
        row.addView(col3);
        row.addView(col4);
        row.addView(col5);
        Log.v("FBListe createRow()", "(" + nr + ") LinearLayout layout=" + row.toString());
        return row;
    }

    private TextView createText(String txt,int style) {
        if(!Util.verifyString(txt)) { txt = "-"; }
        int set_style;
        int weight = 0;
        if(style == 1) {
            set_style = R.style.ListTextStyle1;
            weight = 1;
        }
        else if(style == 2) {
            set_style = R.style.ListTextStyle2;
            weight = 2;
        }
        else if(style == 3) {
            set_style = R.style.TableRowTextStyleTitle;
            weight = 3;
        }
        else {
            set_style = R.style.ListTextStyle1;
            weight = 1;
        }
        TextView tv = new TextView(new ContextThemeWrapper(activity, set_style));
        if(weight==3) {
            // tv.setId(R.id.list0);
            Log.d("FBListe createText()","Set Link to details.");
            tv.setClickable(true);
            tv.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listEntryClicked(v,activity);
                }
            });
        }
        Log.v("FBListe createText()", "Text " + txt);
        tv.setText(txt);
        return tv;
    }
    private void message(String txt) {
        Toast.makeText(activity,txt,Toast.LENGTH_SHORT).show();
    }
}
