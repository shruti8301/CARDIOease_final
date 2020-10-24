package com.example.cardioease;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //todo: Declare AutoCompleteTextView , Buttons and Spinners(Dropdowns)
    EditText age, bloodpressure, cholestrol, maxheartrate, stdepression, maxvessels;
    Spinner spinner, spinner3, spinner4;
    Button button_diagnose;
    int genV;
    int chestPainV;
    int  bloodsugar;
    int eindV;
   int  stslopeV;
    int  thV;

    //todo : String arrays holds spinner values
    String[] chestPain = {"Typical Angina", "Atypical Angina", "Non-Anginal Pain", "Asymptomatic"};
    String[] stSegment = {"Upsloping", "Flat", "Downsloping"};
    String[] thalassemia = {"Normal", "Fixed Defect", "Reversable Defect"};
    String chestPainValue = "None";
    String stSegmentValue = "None";
    String thalassemiaValue = "None";


    ProgressDialog progressDialog;

    //todo: gender radio buttons
    RadioGroup radioGroup, radioGroup1, radioGroup2;
    RadioButton radioButton, radioButton1, radioButton2;


    public void hitAPICall(String gender, String chestPainType, String bloodSugar, String inducedAngina, String stSlope, String thalassemia) {

        if (gender.equalsIgnoreCase("Male"))
            genV = 1;
        else if (gender.equalsIgnoreCase("Female"))
            genV = 0;

        if (chestPainType.equalsIgnoreCase("Typical Angina"))
            chestPainV = 1;
        else if (chestPainType.equalsIgnoreCase("Atypical Angina"))
            chestPainV = 2;
        else if (chestPainType.equalsIgnoreCase("Asymptomatic"))
            chestPainV = 3;
        else if (chestPainType.equalsIgnoreCase("Non-Anginal Pain"))
            chestPainV = 4;

        if (bloodSugar.equalsIgnoreCase("YES"))
            bloodsugar = 1;
        else if (bloodSugar.equalsIgnoreCase("NO"))
            bloodsugar = 0;

        if (inducedAngina.equalsIgnoreCase("YES"))
            eindV = 1;
        else if (inducedAngina.equalsIgnoreCase("NO"))
            eindV = 0;

        if (stSlope.equalsIgnoreCase("Flat"))
            stslopeV = 1;
        else if (stSlope.equalsIgnoreCase("Upsloping"))
            stslopeV = 2;
        else if (stSlope.equalsIgnoreCase("Downsloping"))
            stslopeV = 3;

        if (thalassemia.equalsIgnoreCase("Normal"))
            thV = 1;
        else if (thalassemia.equalsIgnoreCase("Fixed Defect"))
            thV = 2;
        else if (thalassemia.equalsIgnoreCase("Reversable Defect"))
            thV = 3;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        progressDialog = new ProgressDialog(this);

        findIds();
        setAdapters();

        //todo : on button diagnose click
        button_diagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t1 = age.getText().toString();
                final int g1 = Integer.parseInt(t1);

                String t4 = bloodpressure.getText().toString();
                final int g4 = Integer.parseInt(t4);

                String t5 = cholestrol.getText().toString();
                final int g5 = Integer.parseInt(t5);


                String t7 = maxheartrate.getText().toString();
                final int g7 = Integer.parseInt(t7);



                String t9 = stdepression.getText().toString();
                final int g9 = Integer.parseInt(t9);



                String t11 = maxvessels.getText().toString();
                final int g11 = Integer.parseInt(t11);


                progressDialog.setMessage("Processing!");
                progressDialog.show();
                int id = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(id);
                int id1 = radioGroup1.getCheckedRadioButtonId();
                radioButton1 = findViewById(id1);
                int id2 = radioGroup2.getCheckedRadioButtonId();
                radioButton2 = findViewById(id2);

                if ((bloodpressure.getText().toString().length() != 0) & (cholestrol.getText().toString().length() != 0) & (maxheartrate.getText().toString().length() != 0) & (stdepression.getText().toString().length() != 0) & (maxvessels.getText().toString().length() != 0)) {

                    //todo: API call function 'hitAPICall()' is called here
                    //public void Diagnose_btn(){

                    hitAPICall(radioButton.toString(), chestPainValue, radioButton1.toString(), radioButton2.toString(), stSegmentValue, thalassemiaValue);
                    // try {
                    //String encodedage = URLEncoder.encode(age.getText().toString(), "UTF-8");
                    DownloadTask task = new DownloadTask();
                    task.execute("https://heart-disease-pred-api.herokuapp.com/api/" + g1 + "/" + genV + "/" +
                            chestPainV + "/" + g4 + "/" + g5 + "/" + bloodsugar + "/" + g7 + "/" + eindV +
                            "/" + g9 + "/" + stslopeV + "/" + g11 + "/" + thV);

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter required fields", Toast.LENGTH_SHORT).show();
                }
                //} catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Couldn't find result!", Toast.LENGTH_LONG);
//                }

            }
        });
    }


    private void findIds() {
        age = findViewById(R.id.age);
        bloodpressure = findViewById(R.id.bloodpressure);
        cholestrol = findViewById(R.id.cholestrol);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);
        maxheartrate = findViewById(R.id.maxheartrate);
        stdepression = findViewById(R.id.stdepression);
        maxvessels = findViewById(R.id.maxvessels);
        spinner = findViewById(R.id.spinner);
        spinner3 = findViewById(R.id.spinner3);
        spinner4 = findViewById(R.id.spinner4);
        button_diagnose = findViewById(R.id.Diagnose_btn);

    }

    private void setAdapters() {
        ArrayAdapter<String> arrayAdaptercp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, chestPain);
        arrayAdaptercp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdaptercp);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapterea = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stSegment);
        arrayAdapterea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(arrayAdapterea);
        spinner3.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapterst = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, thalassemia);
        arrayAdapterst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(arrayAdapterst);
        spinner4.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner:
                chestPainValue = chestPain[i];
                break;
            case R.id.spinner3:
                stSegmentValue = stSegment[i];
                break;
            case R.id.spinner4:
                thalassemiaValue = thalassemia[i];
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Couldn't find Result", Toast.LENGTH_LONG);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                String message = "";


                JSONObject jsonObject = new JSONObject(result);

                String HeartInfo = jsonObject.getString("result");

                Log.i("Content", HeartInfo);

                message = HeartInfo;
                if (message != "") {
                    AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
                    alertDialog.setTitle("Heart Prediction");
                    alertDialog.setMessage(message);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    progressDialog.dismiss();


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Couldn't Find The Result!", Toast.LENGTH_LONG);
                }


            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Couldn't Find Result!", Toast.LENGTH_LONG);
            }


        }
    }

}

