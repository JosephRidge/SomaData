package com.example.somadata;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    LineChart lineChart ;
    TextView result,tempresult, humidityresult;
    ArrayList<Entry>yDATA,ydata;
    TextView HUMIDITY;///NEW!!

    private static final String LOG_TAG;


    static {
            LOG_TAG = MainActivity.class.getSimpleName();
        }


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference humido =  database.getReference().child("TechShamba/-LpYVdWqieWjr2XXr-rQ/hardware_serial");////RETURNS INTEGER VALUE ,IT WORKS IN RESPONSE

        DatabaseReference tempgraph = database.getReference("TechShamba");


        DatabaseReference humid = database.getReference("Member");///ALLOWS PLOTTING OF THE AGE GRAPH
        DatabaseReference mygrapgh = database.getReference("TechShamba/payload");
        lineChart = (LineChart) findViewById(R.id.lineChart);
        humidityresult = (TextView) findViewById(R.id.Humidityrecord);


   humid.addValueEventListener(new ValueEventListener() { //plots a graph of all ages collected
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                yDATA = new ArrayList<>();
                float i = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    i = i + 1;
                    String SV = Objects.requireNonNull(ds.child("age").getValue()).toString();//member/age
                    float SensorValue = Float.parseFloat(SV);//parser
                    yDATA.add(new Entry(i, SensorValue));
                }
                final LineDataSet lineDataSet1 = new LineDataSet( yDATA, "age" );
                LineData data = new LineData(lineDataSet1);
                lineChart.setData(data);
                lineChart.notifyDataSetChanged();
                lineChart.invalidate();

                lineDataSet1.setDrawCircles(false);
                lineDataSet1.setColors(Color.RED);
 //ss
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LOG_TAG,"Errr!!! ");
            }
        });

   result = (TextView)findViewById(R.id.TimeOfRecord);

        humido.addValueEventListener(new ValueEventListener() {//plotted well
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Val = dataSnapshot.getValue(String.class);
                result.setText(String.valueOf(Val));
                Log.d(LOG_TAG ,"Value is: "+Val);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LOG_TAG ,"ERRR!! ");

            }
        });
    }}
