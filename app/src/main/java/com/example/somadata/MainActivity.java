package com.example.somadata;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

//date
import java.time.format.*;

import android.util.Log;
import java.text.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


import java.util.Date;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {

        LineChart lineChart ;
        TextView  time, a,b,c,humiditygraph, temperaturegraph, soilmoisturegraph;
        ArrayList<Entry>yDATA,ydata,yData,mydata;
        Button H,SM,T;
    Date convertedDate;
    private DrawerLayout mDrawer;
         private Toolbar toolbar;// toolbar
         private NavigationView nvDrawer;

         private ActionBarDrawerToggle drawerToggle ; //the drawer activity

        private static final String LOG_TAG;
        static {
            LOG_TAG = MainActivity.class.getSimpleName();
        }
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            //DatabaseReference humid = database.getReference("Member");///il travaille

    @Override
            protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayvalues();
        //date_time();
        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);//part of the drawer

        lineChart = (LineChart) findViewById(R.id.lineChart);//linechart

        //for the graph
        humiditygraph = (TextView) findViewById(R.id.graphtype);
        temperaturegraph = (TextView) findViewById(R.id.graphtype);
        soilmoisturegraph = (TextView) findViewById(R.id.graphtype);

        //buttons
        H = (Button) findViewById(R.id.humid);
        SM = (Button) findViewById(R.id.SoilMoist);
        T = (Button) findViewById(R.id.temp);

                /*H.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datvalues();
                    }
            });*/
        //for testing log
        Log.d(LOG_TAG, "Before attaching the listener ");

        //start fetching the data from firebase under child=payload
        H.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference classy = database.getReference().child("payload");
                classy.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer i = 0;

                        // NOTE!!  THE ARRAY SHOULD BE INTIALIZED OUTSIDE THE LOOP
                        yDATA = new ArrayList<>(); //Initialize the graph

                        //loop
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            i = i + 1;
                            Object x = ds.child("payload_fields/humidity").getValue();  //get value of object
                            //CONVERSION TO STRING THEN FLOAT
                            String hm = String.valueOf(x);
                           // humidity.setText(hm);
                            Float hum = Float.parseFloat(hm);

                            Log.d(LOG_TAG, "Humidity: " + x);//confirm its success by logging it
                            Log.d(LOG_TAG, "HMIS PRINTED :  " + hm); //printing it out to log cat --TESTING PURPOSES
                            Log.d(LOG_TAG, "HUMIDITY RESULT" + hum); //printing it out to the logcat

                            yDATA.add(new Entry(i, hum)); //ADD VALUES TO GRAPH

                        }

                        final LineDataSet lineDataSet1 = new LineDataSet(yDATA, "Hum.");
                        LineData data = new LineData(lineDataSet1);
                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                        // lineChart.XAxis.mAxisMinimum =100;
                        lineChart.setData(data);
                        lineDataSet1.setDrawValues(false);//remove point labesls

                        lineChart.notifyDataSetChanged();
                        xAxis.setLabelCount(10);
                        lineChart.invalidate();

                        lineDataSet1.setDrawCircles(true);
                        lineDataSet1.setCircleColor(Color.BLUE);

                        lineDataSet1.setDrawFilled(true);
                        lineDataSet1.setFillColor(Color.BLUE);
                        lineDataSet1.setColors(Color.BLUE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(LOG_TAG, "load data failed", databaseError.toException()); //incase of error

                    }

                });
                humiditygraph.setText(" Humidity Graph  ");
            }
        });
        T.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference classy = database.getReference().child("payload");
                classy.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer i = 0;
                        // NOTE!!  THE ARRAY SHOULD BE INTIALIZED OUTSIDE THE LOOP
                        ydata = new ArrayList<>(); //Initialize the graph
                        //XAxis = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            i = i + 1;

                            Object y = ds.child("payload_fields/temperature").getValue();//get value

                            String temp = String.valueOf(y);
                           // temperature.setText(temp);//prints out the value in the textwiew widget


                            Float tempe = Float.parseFloat(temp);

                            Log.d(LOG_TAG, "Temperature: " + y);//confirm its success by logging it
                            ydata.add(new Entry(i, tempe));
                        }

                        final LineDataSet lineDataSet2 = new LineDataSet(ydata, "Temp.");
                        LineData data = new LineData(lineDataSet2);

                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                        lineChart.setData(data);
                        lineChart.notifyDataSetChanged();

                        lineDataSet2.setDrawValues(false);
                        lineDataSet2.setDrawCircles(true);


                        lineDataSet2.setFillColor(Color.RED);
                        lineDataSet2.setDrawFilled(true);
                        lineDataSet2.setCircleColors(Color.RED);
                        lineDataSet2.setColors(Color.RED);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(LOG_TAG, "Operation Failed", databaseError.toException()); //incase of error

                    }

                });
                temperaturegraph.setText("Temperature Graph");
            }

        });

        SM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference classy = database.getReference().child("payload");
                classy.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer i = 0;
                        // NOTE!!  THE ARRAY SHOULD BE INTIALIZED OUTSIDE THE LOOP
                        yData = new ArrayList<>();

                       // soil_moisture = (TextView) findViewById(R.id.CurrentSoilMoisturevalue);

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            i = i + 1;
                            Object z = ds.child("payload_fields/soilMoisture").getValue();//get value
                            String soilM = String.valueOf(z);

                            Float soilmoist = Float.parseFloat(soilM);//conversion to float from string
                            Log.d(LOG_TAG, "SoilMositure: " + z + "\n");//confirm its success by logging it
                            yData.add(new Entry(i, soilmoist));//plots it/adds it to the graph
                        }
                        final LineDataSet lineDataSet3 = new LineDataSet(yData, "SM.");
                        LineData data = new LineData(lineDataSet3);
                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        lineChart.setData(data);
                        lineChart.notifyDataSetChanged();
                        lineChart.invalidate();
                        lineDataSet3.setDrawFilled(true);

                        lineDataSet3.setFillColor(Color.DKGRAY);
                        lineDataSet3.setCircleColors(Color.DKGRAY);
                        lineDataSet3.setDrawValues(false);
                        lineDataSet3.setDrawCircles(true);
                        lineDataSet3.setColors(Color.DKGRAY);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(LOG_TAG, "load data failed", databaseError.toException()); //incase of error

                    }
                });
                soilmoisturegraph.setText("Soil Moisture Graph");
            }
        });


    }
    public void displayvalues() {
        a = (TextView) findViewById(R.id.CurrentHumidityvalue);
        b = (TextView) findViewById(R.id.CurrentTempvalue);
        c = (TextView) findViewById(R.id.CurrentSoilMoisturevalue);
        DatabaseReference display = database.getReference("payload");

        display.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Integer i = 0;
                Log.d(LOG_TAG, "Watu Kujeni muone!! Tunaanza....");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    i = i++;
                    Object aii = ds.child("payload_fields/humidity").getValue(Object.class);
                    Object bee = ds.child("payload_fields/temperature").getValue(Object.class);
                    Object cee = ds.child("payload_fields/soilMoisture").getValue(Object.class);


                    String valu = String.valueOf(bee);
                    String vue = String.valueOf(aii);
                    String value = String.valueOf(cee);
                    a.setText(vue + " %");
                    b.setText(valu + " °C");
                    c.setText(value);
                    Log.d(LOG_TAG, "START:!!!!!!\n");
                    Log.d(LOG_TAG, "and the y r.....  " + aii + " " + "    " + valu + "     " + cee + "  ");
                }
                Log.d(LOG_TAG, "STOP :!!!!!!\n");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}// textview results
/*
    public void date_time (){

        time = (TextView)findViewById(R.id.lastseenval) ;
        DatabaseReference hour = database.getReference().child("payload");
        hour.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG,"Getting time over here... ");
                Integer i = 0;

                        for (DataSnapshot ds:dataSnapshot.getChildren()) {

                          //  Object d = ds.child("metadata/time").getValue(Object.class);
                    //       String dateT = String.valueOf(d);
                            TimeZone tz = TimeZone.getTimeZone("UTC");
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
                           //df.setTimeZone(tz);
                            String nowAsISO = df.format(ds.child("metadata/time").getValue(Object.class));

// 'date' has been successfully parsed
                            //date string
                            //LocalDate date= LocalDate.parse(dateT ,DateTimeFormatter.BASIC_ISO_DATE);

//
                         //   Log.d(LOG_TAG, "\n\n\nTimestamp :" + " " + d);//works
                         //    Log.d(LOG_TAG,"\nTime of generation: "+"  "+dateT);
                            Log.d(LOG_TAG,"\nTime of generation: "+"  "+nowAsISO);
                             //time.setText(dateT);

                        }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


