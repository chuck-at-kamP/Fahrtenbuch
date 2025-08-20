package com.example.fahrtenbuch;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FBExternal {

    Activity activity;
    FBExternal(Activity ctx) {
        activity = ctx;
    }

    public void openGoogleMaps() {
        openApp(activity, "com.google.android.maps.mytracks");
    }
    public void openOsmtracker1() {
        Intent intent = osmTracker1();
        activity.startActivity(intent);
    }
    public void openOsmtracker2() {
        Intent intent = osmTracker2();
        activity.startActivity(intent);
    }

    public void printAppsActionView() {
        /*
        Intent intent1 = intentList(Intent.ACTION_VIEW,null);
        get(intent1);
        Intent intent2 = intentList(Intent.ACTION_MAIN, null);
        get(intent2);
        Intent intent3 = intentList(Intent.ACTION_MAIN, Intent.CATEGORY_LAUNCHER);
        get(intent3);
        Intent intent4 = intentList(Intent.ACTION_MAIN, Intent.CATEGORY_BROWSABLE);
        get(intent4);
        */
        Intent intent1 = intentList(Intent.ACTION_EDIT,null);
        get(intent1);
    }

    public void openEditor(Uri content) {
        // Intent intent = intent("org.billthefarmer.notes","Notes",Intent.ACTION_MAIN,Intent.CATEGORY_LAUNCHER);
        // Intent intent = intent(content);
        getActivity("org.billthefarmer.notes");
        // Intent intent = intent("org.billthefarmer.notes","Editor"); // Nicht frei gegeben.
        Intent intent = intent("org.billthefarmer.notes","Notes");
        intent.setData(content);
        activity.startActivity(intent);
    }

    /**
     * The external app for osmTracker1() is not working, I cannot execute it.
     *
     * @return
     */
    public Intent osmTracker1() {
        // Intent osmTracker1 = new Intent(Intent.ACTION_VIEW, track);
        String packageName = "net.openid.appauth";
        String className = "RedirectUriReceiverActivity";
        Intent osmTracker1 = new Intent();
        osmTracker1.setClassName(packageName, packageName + "." + className);
        osmTracker1.setAction(Intent.ACTION_VIEW);
        osmTracker1.addCategory(Intent.CATEGORY_DEFAULT);
        // osmTracker1.addCategory(Intent.CATEGORY_BROWSABLE);
        // osmTracker1.setData(track);
        return osmTracker1;
    }
    // --------------------------------------------------------------
    // android:name="net.openid.appauth.RedirectUriReceiverActivity"
    // android.intent.action.VIEW
    // android.intent.category.DEFAULT | BROWSABLE
    // data = android:scheme="osmtracker"
    // --------------------------------------------------------------

    /**
     * The external app for osmTracker2() is called, but is not doing anything senseful.
     *
     * @return
     */
    public Intent osmTracker2() {
        String packageName = "net.osmtracker";
        String className = "activity.TrackManager";
        Intent osmTracker2 = new Intent();
        osmTracker2.setClassName(packageName, packageName + "." + className);
        osmTracker2.setAction(Intent.ACTION_MAIN);
        osmTracker2.addCategory(Intent.CATEGORY_LAUNCHER);
        // osmTracker2.setData(track);
        return osmTracker2;
    }
    // --------------------------------------------------------------
    // android:name="net.osmtracker.activity.TrackManager"
    // android.intent.action.MAIN
    // android.intent.category.LAUNCHER
    // --------------------------------------------------------------

    /**
     * The available packages are printed to log.
     *
     * @param intent
     */
    public void get(Intent intent) {
        Log.d("FBExternal get()", "Get Packages");
        final PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> packages = packageManager.queryIntentActivities(intent,0);
        Log.d("FBExternal get()", "#############################");
        for(ResolveInfo res : packages){
            String package_name = res.activityInfo.packageName;
            ApplicationInfo info = res.activityInfo.applicationInfo;
            String name = res.activityInfo.name;
            IntentFilter f = res.filter;
            String scheme = "", type = "";
            if(f != null) {
                scheme = res.filter.getDataScheme(0);
                type = res.filter.getDataType(0);
                Iterator<String> it = res.filter.actionsIterator();
                while(it.hasNext()) {
                    Log.d("FBExternal get()","\tAction=" + it.next());
                }
            }
            String ai = info.toString();
            String allTags = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                String[] tags = res.activityInfo.attributionTags;
                for(String tag: tags) {
                    allTags += " " + tag;
                }
            }
            Log.d("FBExternal get()", "Name=" + name + ", Scheme=" + scheme + ", type=" + type +", Package=" + package_name + ", ai=" + ai + ", tags=" + allTags);
        }
        Log.d("FBExternal get()", "#############################");
    }

    public void getActivity(String pack) {
        try {
            PackageInfo pi = activity.getPackageManager().getPackageInfo(pack, PackageManager.GET_ACTIVITIES);
            ArrayList<ActivityInfo> mActivities = new ArrayList<ActivityInfo>(Arrays.asList(pi.activities)); //mActivities  is the arraylist of activity

            //removing same activity from list in which we are doing all this
            String ourName = getClass().getName();
            for (int i = 0; i < mActivities.size(); ++i) {
                ActivityInfo info = mActivities.get(i);
                Log.d("FBExternal getActivity()","Activity info=" + info.name);
            }
        } catch (PackageManager.NameNotFoundException e) { // Do nothing. Adapter will be empty.
        }
    }

    public Intent intent(Uri content) {
        Intent intent = new Intent(Intent.ACTION_EDIT,content);
        // intent.setClassName(pack, pack + "." + className);
        Log.d("FBExternal intent()","Intent Query Uri=" + content);
        return intent;
    }
    public Intent intent(String pack, String className) {
        Intent intent = new Intent();
        intent.setClassName(pack, pack + "." + className);
        Log.d("FBExternal intent()","Intent Query package=" + pack + ", className=" + className);
        return intent;
    }
    public Intent intent(String pack, String className, String action, String category) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setClassName(pack, pack + "." + className);
        Log.d("FBExternal intent()","Intent Query action=" + action);
        if(category != null) {
            intent.addCategory(category);
            Log.d("FBExternal intent()","Intent Query category=" + category);
        }
        return intent;
    }
    public Intent intentList(String action, String category) {
        Intent intent = new Intent();
        intent.setAction(action);
        Log.d("FBExternal intentList()","Intent Query action=" + action);
        if(category != null) {
            intent.addCategory(category);
            Log.d("FBExternal intentList()","Intent Query category=" + category);
        }
        return intent;
    }
    public boolean intentLauncher(String packageName) {
        PackageManager manager = activity.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                throw new ActivityNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            return true;
        } catch (ActivityNotFoundException e) {
            Log.e("FBExternal intentLauncher()", "Activity not found! mess=" + e.getMessage());
            return false;
        }
    }
    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                throw new ActivityNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (ActivityNotFoundException e) {
            Log.e("FBExternal openApp()", "Activity not found! mess=" + e.getMessage());
            return false;
        }
    }

    public void openMapView(String filename,String pack) {
        File gpxFile = new File(activity.getFilesDir(), filename);

        // Datei-URI
        Uri fileUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", gpxFile);

        // Intent erstellen
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/gpx+xml");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Hier kannst du eine spezielle Map-App angeben, die GPX unterstützt (z. B. OSMAnd)
        // intent.setPackage("net.osmand.plus");
        intent.setPackage(pack);

        if(pack.equals("net.osmtracker")) {
            intent.setClassName(pack, pack + "." + "activity.TrackManager");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        }

        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Log.e("FBExternal openMapView()", "Die App konnte die GPX-Datei nicht öffnen.");
        }
    }

    public void openMarkorEditor(String filename) {
        File file = new File(activity.getFilesDir(), filename);
        // URI für die Datei erhalten
        Uri fileUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);

        // Intent erstellen, um die Datei mit Markor zu öffnen
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "text/plain");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Markor direkt ansprechen (optional)
        intent.setPackage("net.gsantner.markor");

        // Aktivität starten
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            Log.e("FBExternal openMarkorEditor()", "Markor nicht installiert oder Fehler: " + e.getMessage());
        }
    }

    public void example3() {
        // Textdatei erstellen
        File file = new File(activity.getFilesDir(), "beispiel.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            String text = "Hallo, dies ist ein Test für Markor!";
            fos.write(text.getBytes());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // URI für die Datei erhalten
        Uri fileUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);

        // Intent erstellen, um die Datei mit Markor zu öffnen
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "text/plain");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Markor direkt ansprechen (optional)
        intent.setPackage("net.gsantner.markor");

        // Aktivität starten
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            Log.e("MainActivity", "Markor nicht installiert oder Fehler: " + e.getMessage());
        }
    }

}
