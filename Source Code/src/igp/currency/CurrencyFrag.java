package igp.currency;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.akisoft.slidingmenu.R;

public class CurrencyFrag extends Fragment implements OnItemSelectedListener, OnClickListener {
	//Internet-Access without thread
	static{
	     StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();  
	StrictMode.setThreadPolicy(policy);  
	}
/*
------------------------------------------------------------------------
To add a currency, look up your currency @ http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml
	1. Add it to Source_Language array
	2. Edit getSymbol method of currentExchangeRates.class
	3. Add a png to @drawable with exactly the same name as your currency e.g. usd.png
	4. Done!

------------------------------------------------------------------------
Â©Thimo Eder
*/

Spinner spinnerFirstCurrency;
Spinner spinnerSecondCurrency;

Button calculateButton;
Button clearButton;
ImageButton rateButton;
ImageButton switchButton;

static EditText firstCurrency;
EditText secondCurrency;

currentExchangeRates loader;
public static boolean inet=true;
DecimalFormat f;
DecimalFormatSymbols dfs = new DecimalFormatSymbols();

ListView lastCalcView;
ArrayList<String> lastCalcs;
ArrayAdapter<String> adapterLastCalculates;



 public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {


	View view = inflater.inflate(R.layout.activity_currency, container, false);
   	 getActivity().setTitle("Currency");
	
   	 //Prevent Java from adding a comma instead of a dot. Comma will lead to NumberFormatException
   	f= new DecimalFormat("#0.00");
   	DecimalFormatSymbols custom=new DecimalFormatSymbols();
   	custom.setDecimalSeparator('.');
   	f.setDecimalFormatSymbols(custom);

	System.out.println("onCreate");
	spinnerFirstCurrency = (Spinner) view.findViewById(R.id.spinnerFirstCurrency);
	spinnerSecondCurrency = (Spinner)view.findViewById(R.id.spinnerSecondCurrency);
	
	//Setting up entries for Spinners
	ArrayAdapter<CharSequence> adapterFirst = ArrayAdapter.createFromResource(getActivity(), R.array.Source_Currency, android.R.layout.simple_spinner_item);
	adapterFirst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spinnerFirstCurrency.setAdapter(adapterFirst);
	spinnerFirstCurrency.setOnItemSelectedListener(this);
	
	ArrayAdapter<CharSequence> adapterSecond = ArrayAdapter.createFromResource(getActivity(), R.array.Source_Currency, android.R.layout.simple_spinner_item);
	adapterSecond.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spinnerSecondCurrency.setAdapter(adapterSecond);
	spinnerSecondCurrency.setSelection(1);
	spinnerSecondCurrency.setOnItemSelectedListener(this);
	
	calculateButton = (Button) view.findViewById(R.id.calculateButton);
	clearButton = (Button) view.findViewById(R.id.clearButton);
	rateButton = (ImageButton) view.findViewById(R.id.rateButton);
	switchButton = (ImageButton) view.findViewById(R.id.switchButton);
	
	//Setting ClickListener
	calculateButton.setOnClickListener(this);
	clearButton.setOnClickListener(this);
	rateButton.setOnClickListener(this);
	switchButton.setOnClickListener(this);
	
	firstCurrency = (EditText) view.findViewById(R.id.inputFirstCurrency);
	secondCurrency = (EditText) view.findViewById(R.id.inputSecondCurrency);
	
	firstCurrency.requestFocus();
	
	lastCalcView = (ListView) view.findViewById(R.id.lastCalcView);
	lastCalcs = new ArrayList<String>();
	adapterLastCalculates = new ArrayAdapter<String>(
            getActivity(), 
            R.layout.calc_row,
            lastCalcs );
	
	lastCalcView.setAdapter(adapterLastCalculates);
	
	loader = new currentExchangeRates();

	//Load current exchange rates
	// currently @ Main
	//Saving HashMap
	
		new currencyLoad().execute();
		


	
	
	
	
	return view;

		
}
 class currencyLoad extends AsyncTask<String,String,String>
 {

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub
		//if(isConnected())
			load();
		return null;
	}
	
	public boolean isConnected(){
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
	
	public void load()
	 {
		 inet=true;
			//load current Currencies
			
			try {
				currentExchangeRates.loadCurrentRates();
				
				//Save loaded currencies
				File file = new File(getActivity().getDir("data", getActivity().MODE_PRIVATE), "map");    
				ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
				outputStream.writeObject(currentExchangeRates.getMap());
				outputStream.flush();
				outputStream.close();
			}
			
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				inet = false;
				
				//Load old currencies
				
				File file = new File(getActivity().getDir("data", getActivity().MODE_PRIVATE), "map");   
			    FileInputStream f;
				try
				{
					f = new FileInputStream(file);
				
					ObjectInputStream s = new ObjectInputStream(f);
					HashMap<String, Double> fileObj2 = (HashMap<String, Double>) s.readObject();
					s.close();
					currentExchangeRates.setMap(fileObj2);
			    	
				} 
				catch (FileNotFoundException e1)
				{
					try{
						 AssetManager assetManager = getActivity().getAssets();
						
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						
						DocumentBuilder builder = factory.newDocumentBuilder();
						Document document = builder.parse( assetManager.open("eurofxref-daily.xml"));
						System.out.println("Default XML geöffnet");
						NodeList entries = document.getElementsByTagName("Cube");
						int count = entries.getLength();
						HashMap<String, Double> hm = new HashMap<String,Double>();
						

						for(int a= 2; a<count; a++)
						{
							Node entry =entries.item(a);
							
							NamedNodeMap map = entry.getAttributes();
							 
							
							//Saving every rate into a HashMap
							hm.put(map.getNamedItem("currency").getNodeValue(), Double.parseDouble(map.getNamedItem("rate").getNodeValue()));
							
							
							
							
						}
						System.out.println(hm.get("GBP"));
						currentExchangeRates.setMap(hm);
					
					}
					catch(ParserConfigurationException ed)
					{
						e.printStackTrace();
					}
					catch(MalformedURLException ed)
					{
						e.printStackTrace();
					}
					catch(SAXException ed)
					{
						e.printStackTrace();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					
					
				}


				catch (IOException e3)
				{
					e3.printStackTrace();
				}
				
				catch(ClassNotFoundException e2)
				{
					e2.printStackTrace();
				}
				System.out.println("Map Wiederherstgestellt");
	 }

	 }

	 
	 
 }
 


public void onClick(View v) {
	// TODO Auto-generated method stub


	if (v.getId() == R.id.calculateButton)
	{
		Double firstDouble = 0.0;
		Double result = 0.0;
		
		try{
			firstDouble = Double.parseDouble(firstCurrency.getText().toString());
		}
		//Catch exception if field contains e.g. $â‚¬
		catch(NumberFormatException e)
		{
	  
			if(firstCurrency.getText().toString().equals(""))
			{
				firstCurrency.setText("0");
			}
			else
			for	(int i = 0; i<spinnerFirstCurrency.getCount();i++)
			{
				//Get rid of $ or â‚¬, will cause NumberFormatException
				if(firstCurrency.getText().toString().contains(loader.getSymbol(i)))
				{
					
					String firstCurrencyText = firstCurrency.getText().toString().replace(loader.getSymbol(i), "");
					System.out.println(firstCurrencyText);
					firstCurrency.setText(firstCurrencyText);
					break;
				}
		
			} //End For
			
			try{
				firstDouble = Double.parseDouble(firstCurrency.getText().toString().trim());
			}
			catch(NumberFormatException ed)
			{
				Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Type in a valid value",
						   Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 60);
				toast.show();
				clearButton.performClick();
			}
		
			
		} //End Catch
	
		String calcText;
		//Calculation - if for â‚¬
		if(spinnerFirstCurrency.getSelectedItemPosition() == 0 && spinnerSecondCurrency.getSelectedItemPosition() == 0)
		{
			result = firstDouble;
		}
		else
		if(spinnerFirstCurrency.getSelectedItemPosition() == 0){
		
			result = firstDouble * loader.getRate(spinnerSecondCurrency.getSelectedItem().toString());
		
		}
		//into â‚¬
		else
			if(spinnerSecondCurrency.getSelectedItemPosition() == 0)
		{
			result = firstDouble / loader.getRate(spinnerFirstCurrency.getSelectedItem().toString());
		
		}
			else
			{
				result = firstDouble / loader.getRate(spinnerFirstCurrency.getSelectedItem().toString());
				result = result * loader.getRate(spinnerSecondCurrency.getSelectedItem().toString());
			}
		
		if(loader.getSymbol(spinnerFirstCurrency.getSelectedItemPosition()).equals("$") || loader.getSymbol(spinnerFirstCurrency.getSelectedItemPosition()).equals("₹"))
		{
			firstCurrency.setText(loader.getSymbol(spinnerFirstCurrency.getSelectedItemPosition())+" "+ f.format(firstDouble) );
			calcText = loader.getSymbol(spinnerFirstCurrency.getSelectedItemPosition())+" "+ f.format(firstDouble) + " → ";
		}
		else
		{
			firstCurrency.setText(f.format(firstDouble.doubleValue()) +" " + loader.getSymbol(spinnerFirstCurrency.getSelectedItemPosition()));
			calcText = f.format(firstDouble) +" " + loader.getSymbol(spinnerFirstCurrency.getSelectedItemPosition()) + " → ";
		}
		
		if(loader.getSymbol(spinnerSecondCurrency.getSelectedItemPosition()).equals("$") || loader.getSymbol(spinnerSecondCurrency.getSelectedItemPosition()).equals("₹") || loader.getSymbol(spinnerSecondCurrency.getSelectedItemPosition()).equals("¥"))
		{
			secondCurrency.setText(loader.getSymbol(spinnerSecondCurrency.getSelectedItemPosition())+ " "+ f.format(result).toString());
			calcText= calcText + loader.getSymbol(spinnerSecondCurrency.getSelectedItemPosition())+ " "+ f.format(result).toString();
		}
		else
		{
			secondCurrency.setText(f.format(result).toString() +" "+ loader.getSymbol(spinnerSecondCurrency.getSelectedItemPosition()));
			calcText = calcText + f.format(result).toString() +" "+ loader.getSymbol(spinnerSecondCurrency.getSelectedItemPosition());
		}
		if(lastCalcs.size() <= 5)
		lastCalcs.add(0, calcText);
		else
		{
			lastCalcs.remove(lastCalcs.size()-1);
			lastCalcs.add(0,calcText);
		}
		adapterLastCalculates.notifyDataSetChanged();
		
	}

	else
		if(v.getId() == R.id.clearButton)
		{
			firstCurrency.setText("");
			secondCurrency.setText("");
		}
	
		else
			if(v.getId() == R.id.rateButton)
			{
				Intent intent = new Intent(getActivity(), Rates.class);
				startActivity(intent);
			  
			}
			else
				if(v.getId() == R.id.switchButton)
				{
					spinnerFirstCurrency.setOnItemSelectedListener(null);
					
					spinnerSecondCurrency.setOnItemSelectedListener(null);
					
					String firstInput = firstCurrency.getText().toString();
					String secondInput = secondCurrency.getText().toString();
					int firstSelected = spinnerFirstCurrency.getSelectedItemPosition();
					int secondSelected = spinnerSecondCurrency.getSelectedItemPosition(); 
					//Changing
					firstCurrency.setText(secondInput);
					secondCurrency.setText(firstInput);
					spinnerFirstCurrency.setSelection(secondSelected,false);
					spinnerSecondCurrency.setSelection(firstSelected,false);
					
					//to prevent that android sets the OnItemSelectListener on "this" before Spinners have chaned
					try {
						Thread.sleep(500);
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					spinnerFirstCurrency.setOnItemSelectedListener(this);
					spinnerSecondCurrency.setOnItemSelectedListener(this);
				
				
					}
				} // End onClick


			


@Override
public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
		long arg3) {
	
	firstCurrency.setHint(spinnerFirstCurrency.getSelectedItem().toString());
	secondCurrency.setHint(spinnerSecondCurrency.getSelectedItem().toString());
	
	if(firstCurrency.getText().toString().equals(""))
			{
		
		
			}
	
	else
	calculateButton.performClick();
}
	public void onResume()
	{
		super.onResume();
		/*System.out.println(MainActivity.inet);
		
		if(MainActivity.inet == false)
		{
			Toast toast = Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection at Startup! You will work with old exchange rates",
					   Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 0, 60);
			toast.show();
			
		
		}*/
	}
	
	
	
	



@Override
public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub
	
}








public static void closeKeyboard(Context c) {
    InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
    mgr.hideSoftInputFromWindow(firstCurrency.getWindowToken(), 0);
 
}

public static void openKeyboard(Context c)
{
	 InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
	 mgr.showSoftInput(firstCurrency, 0);
	 
}

public void setRetainInstance ()
{
	super.setRetainInstance(true);
}



}
