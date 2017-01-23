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

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("ValidFragment")
public class MapSearch extends Fragment{
	PlacesTask placesTask;
	ParserTask parserTask;
	AutoCompleteTextView input;
	String location;
	GoogleMap map;
	
	public MapSearch(GoogleMap map)
	{
		this.map=map;
	}
	/*@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    if(!(activity instanceof MainActivity))
	        throw new ClassCastException(activity.toString()
	                + " must be of type MainFragmentActivity");
	    parent = (MainActivity) activity;
	    this.mMap = parent.getMap();
	}*/
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
	
		
	View view = inflater.inflate(R.layout.map_search, container, false);
//	srch=(Button) view.findViewById(R.id.search);
	input=(AutoCompleteTextView)view.findViewById(R.id.input);
	
	//srch.setOnClickListener(new OnClickListener() {
		
	//input.setThreshold(1);
	input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
	    @Override
	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
	            Search();
	            return true;
	        }
	        return false;
	    }

		
	});
	
	input.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			placesTask = new PlacesTask();
	           placesTask.execute(s.toString());
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	});
   	 return view;
	}

	
	public void Search() {
		// TODO Auto-generated method stub
		input.clearFocus();
		location=input.getText().toString();
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
		if(location.equals("")|| location.equals(null))
			Toast.makeText(getActivity(), "Enter Address!", Toast.LENGTH_SHORT).show();
		else
		{


			String url = "https://maps.googleapis.com/maps/api/geocode/json?";					
			
			try {
				// encoding special characters like space in the user input place
				location = URLEncoder.encode(location, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			String address = "address=" + location;
			
			String sensor = "sensor=true";
			
			
			// url , from where the geocoding data is fetched
			url = url + address + "&" + sensor;
			//Log.v(TAG+"onClick", "url is: "+url);
		//	String modifiedURL= url.toString().replace(" ", "%20");

			// Instantiating DownloadTask to get places from Google Geocoding service
			// in a non-ui thread
			DownloadTask downloadTask = new DownloadTask();
			
			// Start downloading the geocoding places
			downloadTask.execute(url);
		
		}
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

		       StringBuilder sb = new StringBuilder();

		       String line = "";
		       while( ( line = br.readLine()) != null){
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
	

	//Fetches all places from GooglePlaces AutoComplete Web Service
	private class PlacesTask extends AsyncTask<String, Void, String>{

	   @Override
	   protected String doInBackground(String... place) {
	       // For storing data from web service
	       String data = "";

	       // Obtain browser key from https://code.google.com/apis/console
	       String key = "key=Enter your key";

	       String input="";

	       try {
	           input = "input=" + URLEncoder.encode(place[0], "utf-8");
	       } catch (UnsupportedEncodingException e1) {
	           e1.printStackTrace();
	       }

	       // place type to be searched
	       String types = "types=geocode";

	       // Sensor enabled
	       String sensor = "sensor=false";

	       // Building the parameters to the web service
	       String parameters = input+"&"+types+"&"+sensor+"&"+key;

	       // Output format
	       String output = "json";

	       // Building the url to the web service
	       String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

	       try{
	           // Fetching the data from we service
	           data = downloadUrl(url);
	       }catch(Exception e){
	           Log.d("Background Task",e.toString());
	       }
	       return data;
	   }

	   @Override
	   protected void onPostExecute(String result) {
	       super.onPostExecute(result);

	       // Creating ParserTask
	       parserTask = new ParserTask();

	       // Starting Parsing the JSON string returned by Web Service
	       parserTask.execute(result);
	   }
	}
	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

	   JSONObject jObject;

	   @Override
	   protected List<HashMap<String, String>> doInBackground(String... jsonData) {

	       List<HashMap<String, String>> places = null;

	       PlaceJSONParser placeJsonParser = new PlaceJSONParser();

	       try{
	           jObject = new JSONObject(jsonData[0]);

	           // Getting the parsed data as a List construct
	           places = placeJsonParser.parse(jObject);

	       }catch(Exception e){
	           Log.d("Exception",e.toString());
	       }
	       return places;
	   }

	   @Override
	   protected void onPostExecute(List<HashMap<String, String>> result) {

	       String[] from = new String[] { "description"};
	       int[] to = new int[] { android.R.id.text1 };

	       // Creating a SimpleAdapter for the AutoCompleteTextView
	       SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

	       // Setting the adapter
	       input.setAdapter(adapter);
	   }
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
            		
            		ParserTasks parserTask = new ParserTasks();
                    parserTask.execute(result);
            }

    }

    class ParserTasks extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

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
			map.clear();
			
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

	            map.addMarker(markerOptions);    
	            
	            // Locate the first location
                if(i==0)
                	;
               	map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }            
		}
	}
    
   
   
	
}
