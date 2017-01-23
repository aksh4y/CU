package com.akisoft.slidingmenu;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Directions extends FragmentActivity {
		
	Button mBtnFind;
	GoogleMap mMap;
	EditText etPlace;
	
    private static final String TAG="MainActivity";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.directions);
		Log.v(TAG+"onCreate", "onCreate call");
		// Getting reference to the find button
		//mBtnFind = (Button) findViewById(R.id.btn_show);
		
		// Getting reference to the SupportMapFragment
		
		SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map); 
		
		// Getting reference to the Google Map
		mMap = mapFragment.getMap();
		
		
		 //MapFragment mapfragment =(MapFragment) getFragmentManager().findFragmentById(R.id.map);
		// mMap=mapfragment.getMap();
		// Getting reference to EditText
		//etPlace = (EditText) findViewById(R.id.et_place);
		
		// Setting click event listener for the find button
		mBtnFind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				// Getting the place entered 
				
				Log.v(TAG+"onClick", "onClick call");
				String location = etPlace.getText().toString();
				
				if(location==null || location.equals("")){
					Toast.makeText(getBaseContext(), "No Place is entered", Toast.LENGTH_SHORT).show();
					return;
				}
				
				String url = "https://maps.googleapis.com/maps/api/geocode/json?";					
			
				try {
					// encoding special characters like space in the user input place
					location = URLEncoder.encode(location, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				String address = "address=" + location;
				
				String sensor = "sensor=false";
				
				
				// url , from where the geocoding data is fetched
				url = url + address + "&" + sensor;
				Log.v(TAG+"onClick", "url is: "+url);
			//	String modifiedURL= url.toString().replace(" ", "%20");

				// Instantiating DownloadTask to get places from Google Geocoding service
				// in a non-ui thread
				DownloadTask downloadTask = new DownloadTask();
				
				// Start downloading the geocoding places
				downloadTask.execute(url);
				
				
				
			}
		});
		
	}
	
	private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);


                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }

                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }

        return data;
        
	}

	
	 /** A class, to download Places from Geocoding webservice */
    private class DownloadTask extends AsyncTask<String, Integer, String>{

            String data = null;

            // Invoked by execute() method of this object
            @Override
            protected String doInBackground(String... url) {
                    try{                    		
                            data = downloadUrl(url[0]);
                    }catch(Exception e){
                             Log.d("Background Task",e.toString());
                    }
                    return data;
            }

            // Executed after the complete execution of doInBackground() method
            @Override
            protected void onPostExecute(String result){
            		
            		ParserTask parserTask = new ParserTask();
                    parserTask.execute(result);
            }

    }

  
	class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

		JSONObject jObject;
		
		
		@Override
		protected List<HashMap<String,String>> doInBackground(String... jsonData) {
		
			List<HashMap<String, String>> places = null;			
			GeocodeJSONParser parser = new GeocodeJSONParser();
        
	        try{
	        	jObject = new JSONObject(jsonData[0]);
	        	
	            /** Getting the parsed data as a an ArrayList */
	            places = parser.parse(jObject);
	            
	        }catch(Exception e){
	                Log.d("Exception",e.toString());
	        }
	        return places;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(List<HashMap<String,String>> list){			
			
			// Clears all the existing markers			
			mMap.clear();
			
			for(int i=0;i<list.size();i++){
			
				// Creating a marker
	            MarkerOptions markerOptions = new MarkerOptions();
	            HashMap<String, String> hmPlace = list.get(i);
	
	            double lat = Double.parseDouble(hmPlace.get("lat"));	            
	            double lng = Double.parseDouble(hmPlace.get("lng"));
	           
	            String name = hmPlace.get("formatted_address");
	            LatLng latLng = new LatLng(lat, lng);
	            markerOptions.position(latLng);
	            markerOptions.title(name);

	            mMap.addMarker(markerOptions);    
	            
	            // Locate the first location
                if(i==0)
                	mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }            
		}
	}
	
}

