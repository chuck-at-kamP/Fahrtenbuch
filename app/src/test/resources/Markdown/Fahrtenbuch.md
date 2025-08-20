

# Fahrtenbuch App

## Einleitung

Mein Problem: Ich konnte nicht zwischen meinen Fenstern umschalten!
- Fenster1: Start einer Fahrt.
- Fenster2: Anzeige aller Fahrten und Ladevorgänge.

Zuerst hatte ich mit "Fenster1" Als "MainActivity" angefangen zu programmieren.
Dann wollte ich alle Fahrten anzeigen können, war aber nicht in der Lage vernünftig
zwischen den Fenstern umzuschalten.

Ich habe rumprobiert mit setContentView() und Fragments. Hat leider beides nicht
funktioniert. Dann bin ich auf "Activity" und "Intent" gestoßen. Dies scheinen
wichtige und grundlegende Konzepte bei der Android Programmierung zu sein. Irgendwie
muss ich dieses Wissen wieder abrufbar dokumentieren.

Deswegen die Erweiterung auf Javadoc. Hier möchte ich mit meiner ersten Anwendung
dann auch mein Wissen und entsprechende Links dokumentieren.

JAVADOC
https://de.wikipedia.org/wiki/Javadoc
https://www.baeldung.com/javadoc

File: module-info.java
https://dev.java/learn/modules/intro/

## GoogleMaps oder OSM

Bisher habe ich mit Geocoder Zugriff auf die Google Maps API, ich möchte vielleicht OSM nutzen:
https://github.com/osmapi/osmapi-java



## Intent and Activity

Activity und Intent: (SEHR GUT um Intents zu verstehen)
https://www.android-user.de/mit-android-intents-auf-du-und-du/

Developer Seite zu Intents
https://developer.android.com/guide/components/intents-filters?hl=de

Hier sind Beispiele zu Intents
https://www.geeksforgeeks.org/android/implicit-and-explicit-intents-in-android-with-examples/


Intent on OSM Tracker:
https://github.com/labexp/osmtracker-android/blob/master/app/src/main/AndroidManifest.xml

Activity defined in OSM Tracker:

     * <activity
     *     android:name="net.openid.appauth.RedirectUriReceiverActivity"
     *     tools:node="replace" android:exported="true">
     *     <intent-filter>
     *         <action android:name="android.intent.action.VIEW"/>
     *         <category android:name="android.intent.category.DEFAULT"/>
     *         <category android:name="android.intent.category.BROWSABLE"/>
     *         <data
     *             android:scheme="osmtracker"/>
     *     </intent-filter>
     * </activity>
     * <activity
     *     android:name=".activity.TrackManager"
     *     android:theme="@style/AppTheme"
     *     android:exported="true">
     *     <intent-filter>
     *          <action android:name="android.intent.action.MAIN" />
     *          <category android:name="android.intent.category.LAUNCHER" />
     *     </intent-filter>
     * </activity>
     *
Ich habe das Repository hinzugefügt: https://mvnrepository.com

https://mvnrepository.com/artifact/org.simpleframework/simple-xml

1.) Ansicht auf "Project" - app/build.gradle.kts:
   // https://mvnrepository.com/artifact/org.simpleframework/simple-xml
   implementation("org.simpleframework:simple-xml:2.7.1")
2.) Sync Project: erscheint, wenn man Datei app/build.gradle.kts geöffnet hat.
3.) import org.simpleframework.*;



## Beispiel Listener

################################################################################################################################
Hier ist einge gute Erklärung wie Listener programmiert werden, hoffentlich kann ich das auf mein Location-Problem umsetzen.
https://github.com/codepath/android_guides/wiki/Creating-Custom-Listeners

        public class MyParentActivity extends Activity {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                MyCustomObject object = new MyCustomObject();
                object.setCustomObjectListener(new MyCustomObject.MyCustomObjectListener() {
                    @Override
                    public void onObjectReady(String title) {}
                    @Override
                    public void onDataLoaded(SomeData data) {}
                });
            }
        }
        
        public class MyCustomObject {
            public interface MyCustomObjectListener {
                public void onObjectReady(String title);
                public void onDataLoaded(SomeData data);
            }
            private MyCustomObjectListener listener;
            public MyCustomObject() {
                this.listener = null;
                loadDataAsync();
            }
            public void setCustomObjectListener(MyCustomObjectListener listener) {
                this.listener = listener;
            }
            public void loadDataAsync() {
                AsyncHttpClient client = new AsyncHttpClient();
                client.get("https://mycustomapi.com/data/get.json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        SomeData data = SomeData.processData(response.get("data"));
                        if (listener != null)
                            listener.onDataLoaded(data); // <---- fire listener here
                        }
                });
            }
        }

=======================================================================================================
Jetzt habe ich versucht das Beispiel  oben auf meinen speziellen Fall umzusetzen. Und es scheint zu funktionieren,
obwohl es nicht einfach ist zu verstehen. Das muss ich jetzt nur noch tatsächlich implementieren.
=======================================================================================================

        public class Fahrtenbuch extends Activity {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                FBLocation object = new FBLocation();
                object.setListener(new FBLocation.FBLocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {}
                });
            }
        }
        
        public class FBLocation {
            public interface LocationListener {               // Gibt es schon: class FBLocationListener implements LocationListener
            public void onLocationChanged(Location location);
        }
        private FBLocationListener listener;
        public FBLocation() {
            this.listener = null;
            getCurrentLocation();
        }
        public void setListener(FBLocationListener listener) {
            this.listener = listener;
        }
        public void getCurrentLocation() {
            (LocationManager.getCurrentLocation)
            @Override
            public void accept(Location location) {
                ...
                if (listener != null)
                    listener.onLocationChanged(location); // <---- fire listener here
                }
            });
        }

################################################################################################################################



