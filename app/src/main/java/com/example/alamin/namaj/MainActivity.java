package com.example.alamin.namaj;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
public class MainActivity extends AppCompatActivity {

    Calendar current;
    Spinner spinner;
    TextView timezone,txtCurrentTime,txtTimeZoneTime;
    long millSeconds;
    ArrayAdapter<String> idAdapter;
    SimpleDateFormat sdf;
    Date resultDate;
    TextToSpeech t1;//for talking
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner =(Spinner)findViewById(R.id.spinner);
        timezone=(TextView)findViewById(R.id.timezone);
        txtCurrentTime=(TextView)findViewById(R.id.txtCurrentTime);
        txtTimeZoneTime=(TextView)findViewById(R.id.txtTimeZoneTime);
        String[] idArray= TimeZone.getAvailableIDs();
        sdf=new SimpleDateFormat("EEEE,dd MM yyyy HH:mm:ss");
        idAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,idArray);
        idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(idAdapter);
        getGMTtime();


        //for talking
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    t1.setLanguage(Locale.UK);
                }
            }
        });




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getGMTtime();
                String selectedID=(String)(parent.getItemAtPosition(position));
                TimeZone timeZone = TimeZone.getTimeZone(selectedID);
                String TimeZoneName=timeZone.getDisplayName();
                int TimeZoneOffset = timeZone.getRawOffset()/(60*1000);
                int hrs=TimeZoneOffset / 60;
                int mins=TimeZoneOffset % 60;
                millSeconds=millSeconds+timeZone.getRawOffset();
                resultDate=new Date(millSeconds);
                System.out.println(sdf.format(resultDate));
                timezone.setText(TimeZoneName+":GMT "+hrs+" : "+mins);
                txtTimeZoneTime.setText(""+sdf.format(resultDate));
                millSeconds=0;


                String toSpeak="Select the time zone for your specific area.......";    //| for talking
                t1.speak(toSpeak,TextToSpeech.QUEUE_FLUSH,null);                        //|



            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void getGMTtime(){
        current=Calendar.getInstance();
        txtCurrentTime.setText(""+current.getTime());
        millSeconds = current.getTimeInMillis();
        TimeZone tzCurrent = current.getTimeZone();
        int offset = tzCurrent.getRawOffset();
        if(tzCurrent.inDaylightTime(new Date())){
            offset = offset+tzCurrent.getDSTSavings();
        }
        millSeconds = millSeconds - offset;
        resultDate = new Date (millSeconds);
        System.out.println(sdf.format(resultDate));

    }

    ////speaking
    public void onPause(){
        if(t1!=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }





}


