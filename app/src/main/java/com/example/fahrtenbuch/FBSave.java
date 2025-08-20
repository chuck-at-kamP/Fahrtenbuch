package com.example.fahrtenbuch;

import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FBSave {

    Activity activity;
    String filename;

    FBSave(Activity ctx, String name) {
        activity = ctx;
        filename = name;
    }

    public String[] backup() {

        // --- Destination File
        String path = createBackupFolder();
        String full_filename = path + File.separator + filename;
        File dst = new File(full_filename);

        // --- Source File
        File pathIn = null;
        try {
            pathIn = activity.getFilesDir().getCanonicalFile();
        } catch (IOException e) {
            Log.w("FBSave backup()", "Exception " + e.getMessage());
        }
        File src = new File(pathIn,filename);
        if(src.exists()) {
            Log.d("FBSave backup()","Input file exists! file=" + src.getPath());
        } else {
            Log.w("FBSave backup()","Input file not exists! file=" + src.getPath());
        }
        if(src.canRead()) {
            Log.d("FBSave backup()", "Input file is readable! file=" + src.getPath());
        } else {
            Log.w("FBSave backup()","Input file not readable! file=" + src.getPath());
        }

        // --- Creat Backup
        boolean ret = copy(src,dst);
        if(!ret) {
            Log.w("FBSave backup()","File copy failed!");
            return null;
        }
        String[] result = {src.getPath(),dst.getPath()};
        return result;
    }
    public String[] restore() {
        // --- Source File /Documents/fahrtenbuch/backup/fahrtenbuch.csv
        String fileIn = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/fahrtenbuch/backup/" + filename;
        File src = new File(fileIn);

        // --- Dest File /data/data/com.example.fahrtenbuch/file/fahrtenbuch.csv
        String pathOut = null;
        try {
             pathOut = activity.getFilesDir().getCanonicalPath();
        } catch (IOException e) {
            Log.w("FBSave restore()", "Exception " + e.getMessage());
        }
        File dst = new File(pathOut,filename);
        if(src.exists()) {
            boolean ret = copy(src,dst);
            if(!ret) {
                Log.w("FBSave restore()", "File copy failed!");
                return null;
            }
        } else {
            Log.w("FBSave restore()","File does not exist! file=" + src.getPath());
            return null;
        }
        String[] result = {src.getPath(),dst.getPath()};
        return result;
    }
    private boolean copy(File src, File dst) {
        try {
            FileInputStream inStream = new FileInputStream(src);
            FileOutputStream outStream = new FileOutputStream(dst);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
            return true;
        } catch(Exception e) {
            message("Backup failed! file=" + dst.getPath());
            Log.e("FBSave copy()","Exception during Backup! mess=" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    /*
    Ich werde erst einmal nur 1 Backup erzeugen. Das Backup muss dann eventuell von Hand woanders hin kopiert werden.
    Ziel könnte es sein, ein Backup mit Datum (Verzeichnis) zu versehen und mehrere Backups anzulegen.
    Jetzt ist das erst einmal zu kompliziert. Ich müßte für das Restore dann auch ein Verzeichnis zum zurück spielen
    auswählen können.
     */
    private String createBackupFolder() {
        File dir = null;
        String path = null;
        try {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/fahrtenbuch/backup";
            dir = new File(path);
            if(dir.exists()) {
                Log.d("FBSave createBackupFolder()", "Backup Folder available.");
            } else {
                message("Backup Folder " + dir.getPath() + " created!");
                dir.mkdirs();
            }
        }
        catch (Exception e) {
            Log.e("FBSave createBackupFolder()","Create Backup Folder! mess=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        Log.d("FBSave createBackupFolder()", "Backup Folder created.");

        return path;
    }
    private void message(String txt) {
        Toast.makeText(activity,txt,Toast.LENGTH_SHORT).show();
    }
}
