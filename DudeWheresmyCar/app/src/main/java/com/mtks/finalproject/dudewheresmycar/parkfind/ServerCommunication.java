package com.mtks.finalproject.dudewheresmycar.parkfind;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mtks.finalproject.dudewheresmycar.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ServerCommunication extends AppCompatActivity {
    final String ServerURL = "http://lowcost-env.bvpbcd4kkp.us-east-1.elasticbeanstalk.com/";
    final String locName = "_locName";
    final String stat = "_status";
    final String lat = "_latitude";
    final String lon = "_longitude";
    final String locat = "_location";

    ListView listView;
    Button back_button_1;

    JSONParser jsonParser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_communication);

        listView = (ListView) findViewById(R.id.view_list);
        listView.setAdapter(new ArrayAdapter<String>(
                        this,
                        R.layout.activity_single_list_layout,
                        R.id.single_list_view,
                        new ArrayList<String>()
                )
        );

        back_button_1 = (Button) findViewById(R.id.back_button_1);
        back_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

        String currentAction = this.getIntent().getAction();
        switch (currentAction){
            case "get":
                new ServerConnect().execute(new severParams(
                        "GET",
                        null,
                        null,
                        0,
                        0,
                        ServerURL + "listParking",
                        "get"
                ));
                break;
            case "post":
                String[] data = new String[4];
                data[0] = this.getIntent().getExtras().getString("locatioN_Name");
                data[1] = this.getIntent().getExtras().getString("status");
                data[2] = this.getIntent().getExtras().getString("latitude");
                data[3] = this.getIntent().getExtras().getString("longitude");

                new ServerConnect().execute(new severParams(
                        "POST",
                        data[0],
                        data[1],
                        Double.parseDouble(data[2]),
                        Double.parseDouble(data[3]),
                        ServerURL + "addParking",
                        "post"
                ));
                break;

            case "put":
                String[] Data = new String[2];
                Data[0] = this.getIntent().getExtras().getString("Location_Name");
                Data[1] = this.getIntent().getExtras().getString("Status");

                new ServerConnect().execute(new severParams(
                        "PUT",
                        Data[0],
                        Data[1],
                        0,
                        0,
                        ServerURL + "updateParking",
                        "put"
                ));
                break;
        }
    }

    private class ServerConnect extends AsyncTask<severParams, String, String> {

        private HttpURLConnection httpURLConnection;
        private ArrayAdapter<String> adapter;
        private ArrayList<ParkingLocation> parkingLocations;
        private JSONParser jsonParser;
        private String resp = "";
        private String finalResult;
        private String urlOutput = "[{\"_locName\":\"Lot P General Permit Parking - George Mason University\",\"_status\":\"true\",\"_location\":{\"_latitude\":\"38.8349123\",\"_longitude\":\"-77.3162701\"}},{\"_locName\":\"Lot P General Permit Parking - George Mason University\",\"_status\":\"true\",\"_location\":{\"_latitude\":\"38.8349123\",\"_longitude\":\"-77.3162701\"}},{\"_locName\":\"Garage B\",\"_status\":\"true\",\"_location\":{\"_latitude\":\"38.8462196\",\"_longitude\":\"-77.31092319999999\"}}]";

        protected void onPreExecute() {
            adapter = (ArrayAdapter<String>) listView.getAdapter();
            jsonParser = new JSONParser();
            parkingLocations = new ArrayList<>(10);
        }

        @Override
        protected String doInBackground(severParams... params) {

            switch (params[0].getAction()) {
                case "get":
                    URL url;
                    try {
                        Log.d("ServerCommun get", params[0].getUrl());
                        url = new URL(params[0].getUrl());
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod(params[0].getMethod());

                        if (httpURLConnection.getResponseCode() == 200) {
                            InputStream respBody = new BufferedInputStream(httpURLConnection.getInputStream());
                            resp = readStream(respBody);
                            finalResult = "Successfully received data";
                        } else {
                            finalResult = "Cannot display at this time";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        httpURLConnection.disconnect();
                    }
                    break;

                case "post":
                    try {
                        Log.d("ServerCommun post", params[0].getUrl()
                                + "?tag=" + params[0].getNameTag().replace(" ", "%20")
                                + "&stat=" + params[0].getStatus()
                                + "&lat=" + Double.toString(params[0].getLatitude())
                                + "&long=" + Double.toString(params[0].getLongitude()));
                        url = new URL(params[0].getUrl()
                                + "?tag=" + params[0].getNameTag().replace(" ", "%20")
                                + "&stat=" + params[0].getStatus()
                                + "&lat=" + Double.toString(params[0].getLatitude())
                                + "&long=" + Double.toString(params[0].getLongitude()));
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod(params[0].getMethod());

                        if (httpURLConnection.getResponseCode() == 201) {
                            finalResult = "Successfully Added";
                        } else {
                            finalResult = "Could not add";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        httpURLConnection.disconnect();
                    }
                    break;

                case "put":
                    try {
                        Log.d("ServerCommun put", params[0].getUrl()
                                + "?tag=" + params[0].getNameTag()
                                + "&stat=" + params[0].getStatus());
                        url = new URL(params[0].getUrl()
                                + "?tag=" + params[0].getNameTag()
                                + "&stat=" + params[0].getStatus());
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod(params[0].getMethod());

                        if (httpURLConnection.getResponseCode() == 200) {
                            finalResult = "Successfully updated";
                        } else {
                            finalResult = "Could not update";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        httpURLConnection.disconnect();
                    }
                    break;
            }
            Log.d("ServerCommunications", resp);
            publishProgress(urlOutput);
            return finalResult;
        }

        protected void onProgressUpdate(String... output) {
            JSONArray jsonArray = jsonParser.getJSONArray(output[0]);
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    parkingLocations.add(
                            new ParkingLocation(
                                    jsonArray.getJSONObject(i).getString(locName),
                                    Boolean.parseBoolean(jsonArray.getJSONObject(i).getString(stat)),
                                    jsonArray.getJSONObject(i).getJSONObject(locat).getDouble(lat),
                                    jsonArray.getJSONObject(i).getJSONObject(locat).getDouble(lon)
                            )
                    );

                    adapter.add(jsonArray.getJSONObject(i).getString(locName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String[] data = new String[4];
                    data[0] = parkingLocations.get(position).getLocName();
                    data[1] = parkingLocations.get(position).getIsEmpty().toString();
                    data[2] = Double.toString(parkingLocations.get(position).getLatitude());
                    data[3] = Double.toString(parkingLocations.get(position).getLongitude());

                    Intent intent = new Intent(ServerCommunication.this, DisplayParkingLocations.class);
                    intent.putExtra("data", data);
                    startActivityForResult(intent, 1);
                }
            });
        }

        protected void onPostExecute(String result) {
            Toast.makeText(ServerCommunication.this, result, Toast.LENGTH_SHORT).show();
        }

        private String readStream(InputStream respBody) throws IOException {
            StringBuilder sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new InputStreamReader(respBody), 1000);
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                sb.append(line);
            }
            respBody.close();
            return sb.toString();
        }
    }
}
