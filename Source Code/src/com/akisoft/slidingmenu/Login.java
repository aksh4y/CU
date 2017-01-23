package com.akisoft.slidingmenu;




import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.akisoft.connect.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.akisoft.slidingmenu.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.akisoft.connect.*;
public class Login extends Activity {
	

    private ProgressDialog pDialog;
	// url to get all products list
	
	public Login(){}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		login();
		/*Button log=(Button) findViewById(R.id.button1);    
        log.setOnClickListener(new View.OnClickListener() {
    	   public void onClick(View v) {
				login();
			}
		});*/
        
    }
	
	
	 public void login()
	 {
		
		/* EditText userid=(EditText) findViewById(R.id.editText1);
	    	EditText pass=(EditText) findViewById(R.id.editText2);
	    String uid=userid.getText().toString();
	    	String pwd=pass.getText().toString();*/
	    	Intent i=null;

	    	DatabaseHandler db=new DatabaseHandler(getApplicationContext());
	    	if(db.checkLogin().toString().trim().matches("Success"))
	    	{
	    		if(db.checkActivation().toString().trim().matches("Success"))
            		i=new Intent(Login.this,MainActivity.class);
	    		else
	    		{
	    			new CheckActivationStatus().execute();
                    
	    		}
	    	}
	    	else
	    		i=new Intent(Login.this,RegisterActivity.class);
	    	if(i!=null)
	    	{
	    		startActivity(i);
	    		finish();
	    	}
	   }
	 
 class CheckActivationStatus extends AsyncTask<String, String, String> {
	 int flag=0;
  	
	 protected void onPreExecute() {
	         super.onPreExecute();
	         pDialog = new ProgressDialog(Login.this);
	         pDialog.setMessage("Checking Account Details. Please wait...");
	         pDialog.setIndeterminate(false);
	         pDialog.setCancelable(true);
	         pDialog.show();
	     }
		 
		 protected String doInBackground(String... args) {
			
		    JSONParser jsonParser = new JSONParser();
		    String url_check_activation="Enter activation URL";
			TelephonyManager tm;                                            
            tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            final String uid;
            String TAG_SUCCESS = "success";
            String TAG_PRODUCT="product";
            String TAG_STAT="status";
            int success;
		    uid = tm.getDeviceId();
		    List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("uid", uid));
            
            try
            {
            	 

    	    	DatabaseHandler db=new DatabaseHandler(getApplicationContext());
    	    	JSONObject json = jsonParser.makeHttpRequest(
                    url_check_activation, "GET", params);
           success = json.getInt(TAG_SUCCESS);
           
           		if (success == 1) {
                // successfully received product details
                
            	JSONArray statObj = json
                        .getJSONArray(TAG_PRODUCT); // JSON Array
            	
                // get first row object from JSON Array
                JSONObject res = statObj.getJSONObject(0);
                String result=res.getString(TAG_STAT);
                if(result.trim().matches("activated"))
                {
                	db.Activate(uid);
                	flag=1;
                }
                
                else
                	flag=2;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                finish();
            }
	                
	 
	            return null;
	        }
		 
	            
		 
		 protected void onPostExecute(String file_url) {
	            // dismiss the dialog once got all details
	            pDialog.dismiss();
	            if(flag==1)
	            {
	            	Toast.makeText(getApplicationContext(), "Activation Successful. Please Restart The Application.", Toast.LENGTH_LONG).show();
	            	finish();
	            }
	            else if(flag==2)
	            {
	            	Toast.makeText(getApplicationContext(), "Please Activate Your Account From Your Specified Email ID. For Further Details Contact some email", Toast.LENGTH_LONG).show();	
	            	finish();
	            }
		 }
	 }
}

