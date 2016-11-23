package com.nitk.gpsarea;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.widget.TextView;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    LocationManager lm;
    LocationListener locationListener;
    static int R1 = 6371;
    static int i = 0;
    double latorigin;
    double lonorigin;
    double lat;
    double lon;
    double latold;
    double lonold;
    static SimpleDateFormat s = new SimpleDateFormat("ss");
    static String format;
    static String keytypeud;
    static int flag = 0;
    static final long MIN_TIME = 1 * 1 * 100; // 1 minute
    TextView dist;
    TextView X;
    TextView Y;
    Time time = new Time();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time.setToNow();
        System.out.println("time: " + time.hour + ":" + time.minute + ":" + time.second);

        dist = (TextView) findViewById(R.id.textView1);
        X = (TextView) findViewById(R.id.textView3);
        Y = (TextView) findViewById(R.id.textView4);
        // writedata("sachin","vernekar");

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        Button display = (Button) findViewById(R.id.button1);
        display.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                i = 1;

            }
        });
        Button display2 = (Button) findViewById(R.id.button2);
        display2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                i = 0;

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // ---request for location updates---
        // Criteria criteria = new Criteria();
        // criteria.setPowerRequirement(Criteria.POWER_LOW);
        // criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // criteria.setAltitudeRequired(true);
        // criteria.setBearingRequired(false);
        // criteria.setCostAllowed(false);
        // String provider = lm.getBestProvider(criteria, true);
        /*
         * lm.requestLocationUpdates( provider, MIN_TIME, 0, locationListener);
         * }
         */
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, 0, locationListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        // ---remove the location listener---
        lm.removeUpdates(locationListener);
    }

    private class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location loc) {
            if (loc != null) {

                lat = loc.getLatitude();
                lon = loc.getLongitude();
                time.setToNow();

                Toast.makeText(getBaseContext(), "Location changed : Lat: " + lat + " Lng: " + lon, Toast.LENGTH_SHORT)
                        .show();

                // p = new GeoPoint(
                // (int) (loc.getLatitude() * 1E6),
                // (int) (loc.getLongitude() * 1E6));
                // mc.animateTo(p);
                // mc.setZoom(18);

                // radians = degrees * PI / 180

                if (i == 1) {

                    String finalval1 = time.hour + ":" + time.minute;
                    String finalval2 = ":" + time.second;
                    writedata(finalval1, finalval2);
                    latorigin = lat;
                    lonorigin = lon;
                    latold = lat;
                    lonold = lon;
                    finalval1 = latorigin + ":" + lonorigin + "; ";
                    finalval2 = 0.0 + ":" + 0.0 + "; " + 0.0;
                    dist.setText(String.valueOf(0.0));
                    X.setText(String.valueOf(0.0));
                    Y.setText(String.valueOf(0.0));
                    writedata(finalval1, finalval2);
                    i = 2;
                } else if ((i == 2) && (lat != latold || lon != lonold)) {
                    latold = lat;
                    lonold = lon;

                    String finalval1 = lat + ":" + lon + "; ";

                    double dLat = Math.abs((lat - latorigin) * (Math.PI) / 180);
                    double dLon = Math.abs((lon - lonorigin) * (Math.PI) / 180);
                    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(0) * Math.sin(0)
                            * Math.cos(latorigin * (Math.PI) / 180) * Math.cos(Math.abs(lat * (Math.PI) / 180));
                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    double d1 = R1 * c * 1000;
                    a = Math.sin(0) * Math.sin(0)
                            + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(Math.abs(latorigin * (Math.PI) / 180))
                                    * Math.cos(Math.abs(lat * (Math.PI) / 180));
                    c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    double d2 = R1 * c * 1000;

                    a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                            + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(Math.abs(latorigin * (Math.PI) / 180))
                                    * Math.cos(Math.abs(lat * (Math.PI) / 180));
                    c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    double distance = R1 * c * 1000;

                    dist.setText(String.valueOf(distance));
                    X.setText(String.valueOf(d1));
                    Y.setText(String.valueOf(d2));

                    String finalval2 =
                            d1 + ":" + d2 + "; " + distance + "; " + time.hour + ":" + time.minute + ":" + time.second;
                    writedata(finalval1, finalval2);

                }

            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static void writedata(String data1, String data2) {

        PrintWriter csvWriter;
        try {
            File direct = new File(Environment.getExternalStorageDirectory() + "/GPSFiles");

            if (!direct.exists()) {
                if (direct.mkdir())
                    ; // directory is created;
            }
            format = (s.format(new Date()));
            String filename = "gpslatlon.csv";
            File file = new File(direct, filename);
            if (!file.exists()) {
                file = new File(direct, filename);
            }
            csvWriter = new PrintWriter(new FileWriter(file, true));

            csvWriter.print(data1);
            csvWriter.append('\t');
            csvWriter.print(data2);
            csvWriter.append('\n');

            csvWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
