package com.example.fahrtenbuch;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.xmlpull.v1.XmlSerializer;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class FBTrack {
    Activity activity;
    String filename;

    FBTrack(String f, Activity ctx) {
        filename = f;
        activity = ctx;
        Log.d("FBTrack", "Create Track. filename=" + filename);

        // Was will ich hier machen?
        // 1.) Richtigen Track erzeugen. (/Documents/fahrtenbuch/*)
        // 1a) Track nach OSM Tracker /Documents/osmtracker/*
        // 1b) Track nach OSMAND /?
        // 2.) Beispiel GPX Track erzeugen. (/data/data/com.example.fahrtenbuch/files/2025-08-16_09-56-18.pgx)
        // 3.) Beispiel Serialize Track erzeugen. (/data/data/com.example.fahrtenbuch/files/2025-08-16_09-56-18.pgx)
        //

        // 1.) Richtigen Track erzeugen. (/Documents/fahrtenbuch/*)

        // writeGpx(gpx) // OK, wird aber nicht im Konstruktor aufgerufen, sondern nach "Speichern".

        // 2.) Beispiel GPX Track erzeugen.
        //     /data/data/com.example.fahrtenbuch/files/2025-08-16_09-56-18.pgx
        //     - createxample2()
        //       - GPX gpx = example2()
        //       - createXml(GPX gpx)
        //       - boolean createFile(String filename, String xml)

        // createExample2(filename); // OK

        // 3.) Beispiel Serialize Track erzeugen.
        //     /data/data/com.example.fahrtenbuch/files/2025-08-16_09-56-18.pgx
        //     - createExample1(String filename); and
        //       - example1(FileOutputStream fileos);

        // createExample1(filename); // NOK (Datei ist leer)

        // 4.) Ausser Konkurenz:
        //     - String getPathOsmtracker();
        //     - String getPathMedia();
        //     - boolean createFolderOsmtracker(String filename);
        //       - createFolder();
        //     - boolean createFolder(String foler);
        //     - boolean createGpxFile(String folder, String filename, String xml);
    }



    // #######################################################################
    // Create GPX files for Fahrtenbuch: /Documents/fahrtenbuch/2025-08-15_17-30-10.gpx
    //

    /**
     * Create GPX in Fahrtenbuch-App folder.
     *
     * @param gpx
     * @return
     */
    public boolean writeGpx(GPX gpx) {
        String xml = createXml(gpx);

        boolean ret1 = createFile(filename,xml);
        if(ret1) Log.i("FBTrack writeGpx()","Private File created! file=" + filename);
        else Log.w("FBTrack writeGpx()","Private File not created! file=" + filename);

        boolean ret2 = createPublicFile(filename,xml);
        if(ret2) Log.i("FBTrack writeGpx()","Public File created! file=" + filename);
        else Log.w("FBTrack writeGpx()","Public File not created! file=" + filename);

        boolean ret = ret1 & ret2;
        return ret;
    }

    public boolean createExample2(String filename) {
        GPX gpx = example2();
        String xml = createXml(gpx);
        boolean ret = createFile(filename, xml);
        return ret;
    }

    public String createXml(GPX gpx) {
        Log.d("FBTrack createXml()", "GPX obj=" + gpx.toString());
        String xml = null;
        try {
            Serializer serializer = new Persister();
            Writer writer = new StringWriter();
            writer.flush();
            serializer.write(gpx, writer);
            xml = writer.toString();
            xml = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>\n" + xml;
            Log.d("FBTrack createXml()", "GPX XML xml=" + xml);
        } catch (Exception e) {
            Log.e("FBTrack createXml()", "Could not create XML! mess=" + e.getMessage());
        }
        return xml;
    }

    /**
     * Create file in public folder /Documents/fahrtenbuch
     *
     * After installing app "fahrtenbuch" on my device, I did not got access to my track files.
     * So I want additionally store them in a public folder.
     *
     * @param filename
     * @return
     */
    public boolean createPublicFile(String filename, String xml) {
        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/fahrtenbuch";
            File dir = new File(path);
            dir.mkdirs();
            String full_filename = path + File.separator + filename;
            File file = new File(full_filename);

            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(xml.getBytes());
            fos.close();
        }
        catch (Exception e) {
            Log.e("FBTrack createGpxFile()","XML file exception! mess=" + e.getMessage());
            e.printStackTrace();
            return false;
        }
        Log.d("FBTrack createPublicFile()", "XML file created.");
        return true;
    }

    // #######################################################################
    // Create GPX files: Can create GPX file, but can not read GPX file with other app.
    //
    // OSMAND: /storage/emulated/0/Android/data/net.osmand.plus/files
    //     eingestellt auf /sdcard/Documents/osmand/
    // NOK: Leider werden Dateien nicht gespeichert.

    /**
     * OSM Tracker stores the files in /Documents/osmtracker.
     *
     * Physical path is /sdcard/storage/0/Documents/osmtracker.
     *
     * Unfortunately when I put files in this folder, OSMTracker does not recognizes them
     * and I found no possibility to open the Track files.
     *
     * @return "/sdcard/storage/0/Documents/osmtracker
     */
    public String getPathOsmtracker() {
        String path_osmtracker = "";
        try {
            File file2   = Environment.getExternalStorageDirectory();
            String path2 = file2.getAbsolutePath();
            path_osmtracker = path2 + "/Documents/osmtracker";
            Log.d("FBTrack", "getPathOsmTracker() Path OSM Tracker: " + path2);
        } catch (Exception e) {
            Log.e("FBTrack", "getPathOsmTracker() Cant get Path! mess=" + e.getMessage());
        }
        return path_osmtracker;
    }

    /**
     * This was a trial to find path names on android.
     *
     * The path structure on android ist not understandable.
     *
     * @return
     */
    public String getPathMedia() {
        String path = null;
        try {
            File folder = activity.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
            path = folder.getAbsolutePath();
            Log.d("FBTrack getPathMedia()", "Media Path: " + path);
        } catch (Exception e) {
            Log.e("FBTrack getPathMedia()", "Cant get Path! mess=" + e.getMessage());
        }
        return path;
    }

    /**
     * Creates a path like OSMTracker stores its GPX tracks.
     *
     * Path for OSMTracker files: /Documents/osmtracker/2025-08-15_17-30-10/2025-08-15_17-30-10.gpx
     *
     * Creates Path /Documents/osmtracker/2025-08-15_17-30-10/ from filename 2025-08-15_17-30-10.gpx
     *
     * @param filename
     * @return
     */
    public boolean createFolderOsmtracker(String filename) {
        String pathOsmtracker = getPathOsmtracker();
        String newFoldername  = filename.replace(".gpx","");
        String folder = pathOsmtracker + "/" + newFoldername;
        boolean ret = createFolder(folder);
        if(ret == true) Log.d("FBTrack", "OSMTracker Folder created! folder=" + folder);
        return ret;
    }

    /**
     * Creates a folder.
     *
     *
     * @param folder
     * @return
     */
    public boolean createFolder(String folder) {
        File dir = new File(folder);
        if(!dir.exists()) {
            dir.mkdir();
            if (!dir.exists()) {
                Log.e("FBTrack createFolder()", "Folder could not be created! folder=" + dir.getAbsolutePath());
                return false;
            } else {
                Log.d("FBTrack createFolder()", "Folder created! folder=" + dir.getAbsolutePath());
            }
        } else {
            Log.w("FBTrack createFolder()","Folder exists already! folder=" + folder);
        }
        return true;
    }

    /**
     * Writes XML to folder/file.
     *
     * @param folder
     * @param filename
     * @param xml
     * @return
     */
    public boolean createGpxFile(String folder, String filename, String xml) {
        Log.d("FBTrack createGpxFile()","XML File, In folder=" + folder + ", file=" + filename);
        File file = new File(folder,filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            fos.write(xml.getBytes());
            fos.close();
        }
        catch (Exception e) {
            Log.e("FBTrack createGpxFile()","XML file exception! mess=" + e.getMessage());
            e.printStackTrace();
            return false;
        }
        Log.d("FBTrack createGpxFile()", "XML file created. file=" + fos.toString());
        return true;
    }

    // #######################################################################
    // Create example GPX files:
    //

    public GPX example2() {
        GPX.Metadata m = new GPX.Metadata(filename);
        Log.d("FBTrack example2()", "Object GPX.Metadata obj=" + m.toString());
        GPX.TrkPt p1 = new GPX.TrkPt("65.546","2025-08-02T14:31:24Z","6.788", "53.5584572", "9.9544654");
        Log.d("FBTrack example2()", "Object GPX.TrkPt obj=" + p1.toString());
        GPX.TrkPt p2 = new GPX.TrkPt("47.741","2025-08-02T15:11:02Z","3.79","53.5457565","9.9575933");
        Log.d("FBTrack example2()", "Object GPX.TrkPt obj=" + p1.toString());
        GPX.TrkSeg s = new GPX.TrkSeg();
        Log.d("FBTrack example2()", "Object GPX.TrkSeg obj=" + s.toString());
        if(s != null) {
            s.add(p1);
            Log.d("FBTrack example2()", "Object GPX.TrkSeg added.");
            s.add(p2);
            Log.d("FBTrack example2()", "Object GPX.TrkSeg added.");
        } else {
            Log.e("FBTrack example2()", "Create GPX Object, TrkSeg is null");
        }
        GPX.Trk t = new GPX.Trk("Fahrtenbuch Track",s);
        Log.d("FBTrack example2()", "Object GPX.TrkSeg obj=" + t.toString());
        GPX gpx = new GPX(m,t);
        Log.d("FBTrack example2()", "Object GPX.TrkSeg obj=" + gpx.toString());
        return gpx;
    }

    /**
     * Creates file in app folder /data/data/com.example.fahrtenbuch/files/
     * 2025-08-16_09-56-31.gpx
     *
     * @param filename
     * @param xml
     * @return
     */
    public boolean createFile(String filename, String xml) {
        FileOutputStream fileos = null;
        try {
            fileos = activity.openFileOutput(filename, Context.MODE_PRIVATE | Context.MODE_APPEND);
            fileos.write(xml.getBytes());
            fileos.close();
        } catch (FileNotFoundException e) {
            Log.e("FBTrack createFile()", "File not found! mess=" + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            Log.e("FBTrack createFile()", "IO Exception occured! mess=" + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            Log.e("FBTrack createFile()","Exception mess=" + e.getMessage());
            e.printStackTrace();
            return false;
        }
        Log.d("FBTrack createFile()", "File created. file=" + filename + ", obj=" + fileos.toString());
        return true;
    }
    public boolean createExample1(String filename) {
        FileOutputStream fileos = null;
        try {
            fileos = activity.openFileOutput(filename, Context.MODE_PRIVATE | Context.MODE_APPEND);
            example1(fileos);
            // fileos.write(xml.getBytes());

        } catch (FileNotFoundException e) {
            Log.e("FBTrack createExample1()", "File not found! mess=" + e.getMessage());
            e.printStackTrace();
        }  catch (IOException e) {
            Log.e("FBTrack createExample1()", "IO Exception occured! mess=" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("FBTrack createExample1()","Exception mess=" + e.getMessage());
            e.printStackTrace();
        }
        Log.d("FBTrack createExample1()", "File created. file=" + fileos.toString());
        return true;
    }
    public void example1(FileOutputStream fileos) {
        XmlSerializer serializer = Xml.newSerializer();
        try{
            serializer.setOutput(fileos, "UTF-8");
            serializer.startDocument(null, true);

            serializer.setFeature("http://www.topografix.com/GPX/1/1", true);
            serializer.setFeature("http://www.w3.org/2001/XMLSchema-instance", true);
            serializer.setFeature("http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd", true);

            serializer.startTag(null, "metadata");
            serializer.startTag(null, "name");
            serializer.text(filename);
            serializer.endTag(null, "name");
            serializer.endTag(null, "metadata");

            serializer.startTag(null, "trk");
            serializer.startTag(null, "trkseg");

            serializer.startTag(null, "trkpt");
            serializer.attribute(null, "lat", "53.5584572");
            serializer.attribute(null, "lon", "9.9544654");
            serializer.startTag(null, "ele");
            serializer.text("65.546");
            serializer.endTag(null,"ele");
            serializer.startTag(null, "time");
            serializer.text("2025-08-02T14:31:24Z");
            serializer.endTag(null,"time");
            serializer.startTag(null, "hdop");
            serializer.text("6.788");
            serializer.endTag(null,"hdop");
            serializer.endTag(null,"trkpt");

            serializer.startTag(null, "trkpt");
            serializer.attribute(null, "lat", "53.5457565");
            serializer.attribute(null, "lon", "9.9575933");
            serializer.startTag(null, "ele");
            serializer.text("47.741");
            serializer.endTag(null,"ele");
            serializer.startTag(null, "time");
            serializer.text("2025-08-02T15:11:02Z");
            serializer.endTag(null,"time");
            serializer.startTag(null, "hdop");
            serializer.text("3.79");
            serializer.endTag(null,"hdop");
            serializer.endTag(null,"trkpt");

            serializer.endTag(null,"trkseg");
            serializer.endTag(null,"trk");

            serializer.endDocument();
            Log.e("FBTrack","XML created! xml=" + serializer.toString());
            serializer.flush();
            fileos.close();

        } catch(IOException e) {
            Log.e("FBTrack","Exception occured! mess=" + e.getMessage());
        }
    }
}
