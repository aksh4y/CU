package com.akisoft.slidingmenu;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.akisoft.connect.JSONParser;
import com.akisoft.slidingmenu.R;

import android.app.Activity;
//import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
//import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.widget.DrawerLayout;
//import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//import android.widget.ListView;

public class Register extends Activity {

	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	EditText text1;
	EditText text2;
	EditText text3;
	// url to create new product
	private static String url_register_new = "Your PHP registration URL";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	public Register(){}
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register); 
		
        text1=(EditText) findViewById(R.id.editText1);
        text2=(EditText) findViewById(R.id.editText2);
        text3=(EditText) findViewById(R.id.editText3);
        Button reg=(Button) findViewById(R.id.button1);
        reg.setOnClickListener(new View.OnClickListener() {
        	
			@Override
			public void onClick(View view) {
				
			    //Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_SHORT).show();
				InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                           InputMethodManager.HIDE_NOT_ALWAYS);
				if(!text1.getText().toString().matches("") && !text2.getText().toString().matches("")
						&& text3.getText().toString().indexOf("@")>0 && text3.getText().toString().indexOf(".")>0)
				{
					boolean conn= new registerNew().isConnected();
				if(conn==true)
					new registerNew().execute();
				else
				{
				Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
			    }
				}
				else
					Toast.makeText(getApplicationContext(), "Please Fill In All The Details And Check Your Email ID", Toast.LENGTH_SHORT).show();
				/*		String userid=text1.getText().toString();
				String pass=text2.getText().toString();
				if (userid.trim().length() > 0 && pass.trim().length()>0) {
					// database handler
					DatabaseHandler db = new DatabaseHandler(
							getActivity().getApplicationContext());

					// inserting new user into database
					db.insertUser(userid,pass);
					Toast.makeText(getActivity().getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
					// making input filed text to blank
					text1.setText("");
					text2.setText("");
					Fragment fragment=null;
					fragment=new HomeFragment();
					android.app.FragmentManager fragmentManager = getFragmentManager();
				    fragmentManager.beginTransaction()
				    .replace(R.id.frame_container, fragment).commit();
				 
				            
					 
					 
					//Intent myIntent = new Intent(getActivity(),HomeFragment.class);
				    //startActivity(myIntent);
					//getActivity();
					//getActivity().setResult(Activity.RESULT_OK,myIntent);
					//getActivity().finish();
					// Hiding the keyboard
					InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(text1.getWindowToken(), 0);

					// loading spinner with newly added data
					//loadSpinnerData();
				} else {
					Toast.makeText(getActivity().getApplicationContext(), "Please enter userid and password",
							Toast.LENGTH_SHORT).show();
				}
*/
			}
		});
               
    }
	 

	class registerNew extends AsyncTask<String, String, String> {
		
    	@Override
    	protected String doInBackground(String... args) {
    		String userid=text1.getText().toString();
			String pass=text2.getText().toString();
			String email=text3.getText().toString();
			TelephonyManager tm;                                            
            tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            final String uid;
		    uid = tm.getDeviceId();
			//Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_SHORT).show();
		    //if (userid.trim().length() > 0 && pass.trim().length()>0) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("uid",uid));
				params.add(new BasicNameValuePair("email",email));
				params.add(new BasicNameValuePair("userid", userid));
				params.add(new BasicNameValuePair("pass", pass));
				params.add(new BasicNameValuePair("stat","pending"));
				
				// getting JSON Object
				try {
				JSONObject json = jsonParser.makeHttpRequest(url_register_new,
						"POST", params);
				
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						// successfully created product
						//Entry in local DB
						
						DatabaseHandler db = new DatabaseHandler(getApplicationContext());

						// inserting new user into database
						db.insertUser(uid,userid,pass,"pending");
						
						//Main Screen
						Intent i = new Intent(Register.this, Login.class);
						startActivity(i);
						finish();
						//getActivity().getActionBar().setTitle("Home");
						   
						//Fragment fragment=null;
						//fragment=new HomeFragment();
						//android.app.FragmentManager fragmentManager = getFragmentManager();
					    //fragmentManager.beginTransaction()
					    //.replace(R.id.frame_container, fragment).commit();
					    // closing this screen
					    //getActivity().finish();
					} 
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "Failed. . .", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			//}
			//else
				//Toast.makeText(getActivity().getApplicationContext(), "Enter All Details!", Toast.LENGTH_SHORT).show();
    		
    			
				return null;
    	}
    	
    	public boolean isConnected(){
            ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
              if (connectivity != null) 
              {
                  NetworkInfo[] info = connectivity.getAllNetworkInfo();
                  if (info != null) 
                      for (int i = 0; i < info.length; i++) 
                          if (info[i].getState() == NetworkInfo.State.CONNECTED)
                          {
                              return true;
                          }
     
              }
              return false;
        }
    	 

    	protected void onPreExecute() {
    		super.onPreExecute();
    		pDialog = new ProgressDialog(Register.this);
    		pDialog.setMessage("Registering..");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(true);
    		pDialog.show();
    	}
    	protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
    		pDialog.dismiss();
		}

    }
}


