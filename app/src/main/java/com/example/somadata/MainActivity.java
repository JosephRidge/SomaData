package com.example.somadata;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;

import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
    public class MainActivity extends AppCompatActivity {

        LineChart lineChart ;
        TextView  trix,humidity, soil_moisture, temperature;
        ArrayList<Entry>yDATA,ydata,yData;



        private static final String LOG_TAG;
        static {
            LOG_TAG = MainActivity.class.getSimpleName();
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference humid = database.getReference("Member");///il travaille

        DatabaseReference humido =  database.getReference("payload/-LplVF-Tt8knbu4T09Kg/metadata/data_rate");////il trava ille

        @Override

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            humidity = findViewById(R.id.Humidityrecord);
            temperature = findViewById(R.id.tempraturerecord);
            soil_moisture =  findViewById(R.id.inputedoutput);

            lineChart = (LineChart) findViewById(R.id.lineChart);

            Log.d(LOG_TAG,"Before attaching the listener ");
            DatabaseReference classy = database.getReference().child("payload");
            classy.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   Integer i=0;
               // NOTE!!  THE ARRAY SHOULD BE INTIALIZED OUTSIDE THE LOOP
                   yDATA = new ArrayList<>(); //Initialize the graph
                   ydata =new ArrayList<>();
                   yData = new ArrayList<>();
                   for (DataSnapshot ds:dataSnapshot.getChildren()) {
                       i = i+1;

                       Object x = ds.child("payload_fields/humidity").getValue();  //get value
                       Object y = ds.child("payload_fields/temperature").getValue();//get value
                       Object z = ds.child("payload_fields/soilMoisture").getValue();//get value
                       String hm = String.valueOf(x);//CONVERSION TO STRING THEN FLOAT
                       String temp = String.valueOf(y);
                       String soilM = String.valueOf(z);

                       Float hum = Float.parseFloat(hm);
                       Float tempe= Float.parseFloat(temp);
                       Float soilmoi = Float.parseFloat(soilM);

                        //LOG OUTPUT OF DATASNAPSHOT

                       Log.d(LOG_TAG, "Humidity: " + x);//confirm its success by logging it
                       Log.d(LOG_TAG, "Temperature: " + y);//confirm its success by logging it
                       Log.d(LOG_TAG, "SoilMositure: " + z + "\n");//confirm its success by logging it
                       Log.d(LOG_TAG, "HMIS PRINTED :  " + hm); //printing it out to log cat --TESTING PURPOSES
                       Log.d(LOG_TAG, "HUMIDITY RESULT" + hum); //printing it out to the logcat

                       yDATA.add(new Entry(i, hum)); //ADD VALUES TO GRAPH
                       ydata.add(new Entry(i, tempe));
                       yData.add(new Entry(i, soilmoi));
                      }

                        final LineDataSet lineDataSet1 = new LineDataSet(yDATA, "Hum.");
                        LineData data = new LineData(lineDataSet1);

                        lineChart.setData(data);

                        lineChart.notifyDataSetChanged();
                        lineChart.invalidate();

                        lineDataSet1.setDrawCircles(false);
                        lineDataSet1.setColors(Color.BLUE);}

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {
                   Log.w(LOG_TAG,"loaddata failed",databaseError.toException()); //incase of error



               }
           });

            //TESTING PURPOSES
            trix = (TextView)findViewById(R.id.TimeOfRecord) ;
            humido.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String Val = dataSnapshot.getValue(String.class);
                    trix.setText(String.valueOf(Val));
                    Log.d(LOG_TAG ,"Value is: "+Val);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });//texting purposes
        }
    }


