package com.akisoft.slidingmenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFrag extends SupportMapFragment implements OnMapLongClickListener, OnMapClickListener, LocationListener{
    
    private GoogleMap map;// = getMap();
    private LocationManager lm;
    ArrayList<LatLng> points=new ArrayList<LatLng>();
    AutoCompleteTextView input;
    PlacesTask placesTask;
    ParserTask parserTask;
   // Button srch=(Button) getActivity().findViewById(R.id.search);
  //  private static final long MIN_TIME=400;
   // private static final float MIN_DISTANCE=1000;
    
    @Override
	public void onSaveInstanceState(Bundle outState) {
        // Adding the pointList arraylist to Bundle
        outState.putParcelableArrayList("points", points);
 
        // Saving the bundle
        super.onSaveInstanceState(outState);
    }
    public void onActivityCreated(Bundle savedInstanceState)
    {
         
   
          super.onActivityCreated(savedInstanceState);
          setRetainInstance(true);
          getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
     map = getMap();
    // map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
     map.setOnMapClickListener(this);
     map.setOnMapLongClickListener(this);
     map.setMyLocationEnabled(true);
     map.getUiSettings().setZoomControlsEnabled(false);
     LatLng loc=myLocation();
     map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
     
     FragmentManager fm = getFragmentManager();
     FragmentTransaction ft = fm.beginTransaction();

     fm.beginTransaction();
     Fragment fragTwo = new MapSearch(map);
     ft.add(R.id.frame_container, fragTwo);
     ft.commit();
    

     if(savedInstanceState!=null){
         if(savedInstanceState.containsKey("points")){
             points = savedInstanceState.getParcelableArrayList("points");
             if(points!=null){
                 for(int i=0;i<points.size();i++){
                     drawMarker(points.get(i));
                 }
             }
         }
     }
     
    // input = (AutoCompleteTextView) getActivity().findViewById(R.id.input);
     /*srch.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			Toast.makeText(getActivity(), input.getText().toString(), Toast.LENGTH_SHORT).show();
			
		}
    	 
     });*/
     /*input.setThreshold(1);

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
     });*/

    	        
    }
     
     private void drawMarker(LatLng point){
         // Creating an instance of MarkerOptions
         MarkerOptions markerOptions = new MarkerOptions();
  
         // Setting latitude and longitude for the marker
         markerOptions.position(point);
  
         // Setting a title for this marker
         markerOptions.title("Lat:"+point.latitude+","+"Lng:"+point.longitude);
  
         // Adding marker on the Google Map
         map.addMarker(markerOptions);
     }
     
     
     
	@Override
	public void onMapLongClick(LatLng point) {
		map.clear();
		 getMyLocationAddress(point);
		 MapFunctions mt=new MapFunctions();
		 		 	LatLng s=myLocation();
	        LatLng source=s;
	       /* map.addMarker(new MarkerOptions()
  	        .position(s)
  	        .title("Source")       
  	        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));*/
	       // DatabaseHandler db=new DatabaseHandler(getActivity().getApplicationContext());
	        points.add(point);
		 mt.drawRoute(map,getActivity(), source,point,true, "en");
		 
		// Toast.makeText(getActivity().getApplicationContext(),a,Toast.LENGTH_SHORT).show();
			}
	
    


	public void getMyLocationAddress(LatLng point) {
        
        Geocoder geocoder= new Geocoder(getActivity(), Locale.ENGLISH);
         
        try {
               
              //Place your latitude and longitude
              List<Address> addresses = geocoder.getFromLocation(point.latitude,point.longitude, 1);
              
              if(addresses != null) {
               
                  Address fetchedAddress = addresses.get(0);
                  StringBuilder strAddress = new StringBuilder();
                
                  for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                        strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                  }
                  map.addMarker(new MarkerOptions()
      	        .position(point)
      	        .title("Destination!")       
      	        .snippet(strAddress.toString())
      	        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                  
               
              }
               
              else
                  Log.e("Error","No location found..!");
          
        } 
        catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
                 Toast.makeText(getActivity().getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
        }
    }
	@Override
	public void onMapClick(LatLng point) {
		

	}
	
	public LatLng myLocation()
	{
		lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        

        Criteria criteria = new Criteria();

       String best = lm.getBestProvider(criteria, true);
        Log.d("best provider", best);

        Location location = lm.getLastKnownLocation(best);
        LatLng pos=new LatLng(location.getLatitude(),location.getLongitude());	
        return pos;
	}
	
	public void onMapDoubleClick(Location location)
	{
		
		LatLng latlong=new LatLng(location.getLatitude(),location.getLongitude());
		CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(latlong, 10);
		map.animateCamera(cameraUpdate);
		lm.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		LatLng latlong=new LatLng(location.getLatitude(),location.getLongitude());
		CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(latlong, 10);
		map.animateCamera(cameraUpdate);
		lm.removeUpdates(this);
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
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

	
	private class PlacesTask extends AsyncTask<String, Void, String>{

		   @Override
		   protected String doInBackground(String... place) {
		       // For storing data from web service
		       String data = "";

		       // Obtain browser key from https://code.google.com/apis/console
		       String key = "key= Enter your key";

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
//		 public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
//		        inflater.inflate(R.menu.map_menu, menu);
//		        menu.add("Help");
//		        super.onCreateOptionsMenu(menu, inflater);
//		    }
}