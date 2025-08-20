package com.example.fahrtenbuch;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name="gpx")
public class GPX {

    @Order
    @Attribute
    String version = "1.1";
    @Attribute
    @Order
    // String creator = "OSMTracker for Android™ - https://github.com/labexp/osmtracker-android";
    // String creator = "Fahrtenbuch";
    String creator = "OsmAnd~";
    @Order
    @Attribute
    String xmlns = "http://www.topografix.com/GPX/1/1";

    // @Attribute(name="xmlns:xsi")
    // String xsi = "http://www.w3.org/2001/XMLSchema-instance";
    // @Attribute(name="xsi:schemaLocation")
    // String schema = "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd";

    @Element
    Metadata metadata;
    @Element
    Trk trk;

    GPX(String filename) {
        metadata = new Metadata(filename);
        trk      = new Trk("Fahrtenbuch Track", new TrkSeg());

    }
    GPX(Metadata m, Trk t) {
        metadata = m;
        trk = t;
    }
    public void add(String t, String la, String lo) {
        TrkPt p = new GPX.TrkPt(t,la,lo);
        trk.get().add(p);
    }
    public void add(String t, String la, String lo, String acc, String alt) {
        TrkPt p = new GPX.TrkPt(acc,t,alt,la,lo);
        trk.get().add(p);
    }

    public static class Metadata {
        @Element
        String name;
        // @Element
        // String keywords = "osmtracker";
        Metadata(String n) {
            name = n;
        }
    }
    public static class Trk {
        @Element
        String name;
        @Element
        TrkSeg trkseg;
        Trk(String n, TrkSeg ts) {
            name = n;
            trkseg = ts;
        }
        TrkSeg get() {
            return trkseg;
        }
    }
    public static class TrkSeg {

        @ElementList(entry="trkpt", inline=true)
        List<GPX.TrkPt> list;
        TrkSeg() {
            list = new ArrayList<GPX.TrkPt>();
        }
        void add(GPX.TrkPt p) {
            list.add(p);
        }
    }
    public static class TrkPt {
        @Attribute
        String lat;
        @Attribute
        String lon;
        @Element(required=false)
        String ele;
        @Element
        String time;
        @Element(required=false)
        String hdop;
        TrkPt(String t, String la, String lo) {
            time = t;
            lat = la;
            lon = lo;
        }
        TrkPt(String e, String t, String h, String la, String lo) {
            ele = e;
            time = t;
            hdop = h;
            lat = la;
            lon = lo;
        }
    }
}

/* Example GPX File from OSMAND
  ---------------------------------------------------------------------------
  1 <?xml version='1.0' encoding='UTF-8' standalone='yes' ?>
  2 <gpx version="1.1" creator="OsmAnd~" xmlns="http://www.topografix.com/GPX/1/1"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd">
  3   <metadata>
  4     <name>2025-08-02_16-31_Sat</name>
  5   </metadata>
  6   <trk>
  7     <trkseg>
  8       <trkpt lat="53.5584572" lon="9.9544654">
  9         <ele>65.546</ele>
 10         <time>2025-08-02T14:31:24Z</time>
 11         <hdop>6.788</hdop>
 12       </trkpt>
...
 88       <trkpt lat="53.5457565" lon="9.9575933">
 89         <ele>47.741</ele>
 90         <time>2025-08-02T15:11:02Z</time>
 91         <hdop>3.79</hdop>
 92       </trkpt>
 93     </trkseg>
 94   </trk>
 95 </gpx>
   ---------------------------------------------------------------------------

 */
