
# Fairphone 5


- Power: Länger drücken um Gerät auszuschalten.
- Power - Volume Up - Länger drücken um ins Boot-Menue zu gelangen.

    Wie kann ich "root" für das Gerät einschalten?

## Gerät Fairphone 5 verbinden

Ich möchte mein Smartphone 'Fairphone 5' mit Android Studio verbinden. Damit
ich auch auf die Dateien zugreifen kann?

https://developer.android.com/studio/run/device?hl=de

  
    $ cd ~/Android/Sdk/platform-tools
    $ ./adb devices

    chuck@android:~/Android/Sdk/platform-tools$ ./adb devices
    List of devices attached
    b4777fda	device

Der Zugriff auf die Verzeichnisse ist jetzt wie beim Emulator möglich. Leider
kann ich nicht ins Verzeichnis /data/data/com.example.fahrtenbuch/ wechseln, es
erscheint der Text "run-as: package not debuggable:". Ich muss wohl die Debugging-APK
installieren, damit ich auf die Verzeichnisse zugreifen kann.



## Zugriff Einstellungen

Das Gerät meldet sich, wenn die USB Verbindung hergestellt wurde. In den 'Meldungen' des
Smartphones sind dann die Einträge 'USB-Debugging' und 'USB-Dateiübertragung' vorhanden.
Es muss in beide Einträge gegangen werden um sie zu aktivieren.

Es muss dann die 'Dateiübertragung' aktiviert werden. Auch die Verbindung zum 'USB-Debugging'
muss aktiviert werden.

## USB-Debugging aktivieren

1. Einstellungen - System - Entwickler-Optionen
2. USB-Debugging auf 'ON' stellen.

## Entwickler-Optionen aktivieren

1. Einstellungen - Über das Telefon - Build-Nummer
2. Sieben (7) mal auf die 'Build-Nummer' klicken.
3. PIN eingeben.
4. Entwickler-Optionen sind freigeschaltet.


## Apps

https://www.gpxsee.org/?from=AppAgg.com
https://f-droid.org/packages/net.gsantner.markor/

## Zugriff Smartphone

Mein Smartphone wurde auf Debian 12 nicht gemountet, ich habe "gmtp" mit Synaptic installiert.
Funktioniert trotzdem nicht.
"gvfs-backends" und "gvfs-fuse" installiert. Nach einem Reboot war das Smartphone automatisch zu sehen.




