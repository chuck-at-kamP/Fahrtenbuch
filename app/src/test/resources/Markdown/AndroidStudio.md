

# Benutzung Android Studio

## Android Studio UI

Create Icons for your project: **File - New - Image Asset**

## Extra Project Docs

Folder created for extra files: /app/src/test/resources/
I want to have a place to put some extra project files, that should not be published with APK file.
i.e. tutorial, or osmand/osmtracker example files.

## Plugins

Installation Plugins: **File - Settings - Plugins:**
    Markdown Editor installiert.

## Manifest.xml

### Intent-Filter

Es werden für jede "Activity" ein Icon installiert, also habe ich 4 Icons für Fahrtenbuch installiert.
Ich muss aus der Manifest den intent-filter entfernen und nur noch für "MainActivity" drin lassen.

    In build.gradle.kts (:app) habe ich folgendes aufgenommen, um die APK mit anderem Namen zu haben.
    base.archivesName = "fahrtenbuch-$versionName"

## Device Explorer

Find file:
* "View - Tool Window - Device Explorer"
* "data/data/com.example.fahrtenbuch/files/Fahrtenbuch.csv"

Im Device Explorer kann man "Files Save ..." (mit Download Icon) machen, dann können Dateien
auf dem Host Rechner (Debian) gespeichert werden. Ich habe die mit APK Extractor erstellte GPXSee.apk
heruntergeladen. Diese kann ich aber leider nicht auf meinem Fairphone installieren.  

## APK erstellen

Ich habe um ein "Signed APK" zu bauen ein Key anlegen müssen. Dazu wurde im
Verzeichnis AndroidStudioProjects/ die Datei Android-Fahrtenbuch-Key.jks angelegt.
Als Passwort habe ich "fAhrtenbuch-79" verwendet.

    Leider ist der Dateiname "app-release.apk". Den muss ich wohl irgendwie vorher ändern.
    Datei liegt unter:
    /home/chuck/AndroidStudioProjects/Fahrtenbuch/app/release/

    Leider musste ich den Namen von Hand nachträglich ändern. Einträge in der build-gradle
    haben noch nicht geholfen.

    Hier ist etwas zum Build erklärt:
    https://developer.android.com/build?hl=de

## Logging

Logging auf Android: Logcat

    https://www.android-user.de/dem-android-log-auf-die-finger-geschaut/
    https://www.tutorialspoint.com/how-to-enable-disable-log-levels-in-android
    https://developer.android.com/studio/debug/dev-options?hl=de

## GPX Dateien

Im Internet prüfen ob die GPX Dateien syntaktisch richtig sind:
https://www.gpxanalyzer.com/tools/gpx-validator.html

## Git

(20.08.2025) Installation von Git über Synaptic auf Debian 12.

Lokales Git aufsetzen: **Version Control - Create Git Repository ...**
    Verzeichnis: /home/chuck/AndroidStudioProjects/Fahrtenbuch


## Gradle

Ich möchte eigentlich eine Git Version in meinen Build einbauen, dass die APK die Version aus
Git beinhaltet. Leider verstehe ich Gradle nicht. Bei mir zeigen kopierte Script in build.gradle.kts
nur rot an.

Ich habe mir ein versioning.gradle Script angelegt, kann es aber in build.gradle.kts nicht nutzen.
gradle/versioning.gradle angelegt.


