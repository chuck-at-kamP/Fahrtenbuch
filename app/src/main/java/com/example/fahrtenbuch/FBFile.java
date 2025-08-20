package com.example.fahrtenbuch;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FBFile {

    protected String filename;
    protected Context context;

    public static final String COMMA_DELIMITER = ";";
    public static final String COLON_DELIMITER = "=";
    FBFile(String file, Context ctxt) {
        filename = file;
        context = ctxt;
        File fb = new File(context.getFilesDir(), filename);
        if (! fb.exists()) {
            try {
                boolean ret = fb.createNewFile();
                String path = fb.getAbsolutePath();
                Log.d("FBFile()", "File created! file=" + filename + ", path=" + path);
            } catch(IOException e) {
                String message = e.getMessage();
                Log.d("FBFile()", "Exception " + message);
            }
        }
        else {
            String path = fb.getAbsolutePath();
            Log.d("FBFile()", "File available! file=" + filename + ", path=" + path);
        }
    }

    public List<String> getFilesDir() {
        List<String> list = new ArrayList<String>();
        File f1 = context.getFilesDir();
        list.add(f1.getName());
        list.add(f1.getPath());
        list.add(f1.getAbsolutePath());
        try {
            list.add(f1.getCanonicalPath());
        } catch (IOException e) {
            Log.w("FBFile getDirs()", "Exception " + e.getMessage());
        }
        return list;
    }

    public List<String> getDirs() {
        List<String> list = new ArrayList<String>();
        String f1 = context.getFilesDir().getAbsolutePath();
        String f2 = context.getCacheDir().getAbsolutePath();
        String f3 = context.getPackageCodePath();
        String f4 = context.getPackageResourcePath();
        String f5 = context.getCodeCacheDir().getAbsolutePath();
        String f6 = context.getDataDir().getAbsolutePath();
        String f7 = context.getDir("testf7",Context.MODE_APPEND).getAbsolutePath();
        String f8 = context.getExternalCacheDir().getAbsolutePath();
        String f9 = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        String f10 = context.getFileStreamPath("fahrtenbuch.csv").getAbsolutePath();
        String f11 = context.getNoBackupFilesDir().getAbsolutePath();
        String f12 = context.getObbDir().getAbsolutePath();
        list.add("Files Dir: " + f1);
        list.add("Cache Dir: " + f2);
        list.add("Package Code Path: " + f3);
        list.add("Package Resource Path: " + f4);
        list.add("Code Cache Dir: " + f5);
        list.add("Data Dir: " + f6);
        list.add("Dir: " + f7);
        list.add("External Cache Dir: " + f8);
        list.add("External Files Dir: " + f9);
        list.add("File Stream Path: " + f10);
        list.add("No Backup Files Dir: " + f11);
        list.add("Obb Dir: " + f12);
        return list;
    }
    public List<String> getFiles() {
        List<String> list = new ArrayList<String>();
        File f1 = context.getFilesDir();
        String[] files = f1.list();
        for(String file:files) {
            list.add(file);
        }
        return list;
    }
    public String saveList(List<Map<String,String>> data) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE|Context.MODE_APPEND);
            for(int i = 0; i < data.size(); i++) {
                Map<String,String> map = data.get(i);
                StringBuilder row = new StringBuilder();
                for (Map.Entry<String,String> entry : map.entrySet()) {
                    String value = entry.getValue();
                    value.replace("\n","|");
                    row.append(entry.getKey()).append(COLON_DELIMITER).append(entry.getValue()).append(COMMA_DELIMITER);
                }
                if (row.toString().endsWith(COMMA_DELIMITER)) {
                    row = new StringBuilder(row.substring(0, row.length() - 1) + "\n");
                }
                fileOutputStream.write(row.toString().getBytes());
            }
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage();
            return message;
        }
        return "OK";
    }

    public String save(Map<String,String> map) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE|Context.MODE_APPEND);
            StringBuilder row = new StringBuilder();
            for (Map.Entry<String,String> entry : map.entrySet()) {
                String value = entry.getValue();
                value.replace("\n","|");
                row.append(entry.getKey()).append(COLON_DELIMITER).append(entry.getValue()).append(COMMA_DELIMITER);
            }
            if (row.toString().endsWith(COMMA_DELIMITER)) {
                row = new StringBuilder(row.substring(0, row.length() - 1) + "\n");
            }
            fileOutputStream.write(row.toString().getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage();
            return message;
        }
        return "OK";
    }
    public void load(List<Map<String,String>> data) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = context.openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            // FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                Map<String,String> map = new HashMap<>();
                for (String value : values) {
                    String[] arr = value.split(COLON_DELIMITER);
                    String key = arr[0];
                    String val = "-";
                    if(arr.length==2) val = arr[1];
                    map.put(key, val);
                }
                data.add(map);
            }
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
