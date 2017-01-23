package igp.currency;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.akisoft.slidingmenu.R;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class currentExchangeRates {

	static HashMap<String, Double> hm = new HashMap<String,Double>();
	static ArrayList<Map<String, Double>> list = new ArrayList<Map<String,Double>>();
	public static void loadCurrentRates() throws IOException
	{
	try{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String url = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse( new URL(url).openStream()) ;
		NodeList entries = document.getElementsByTagName("Cube");
		int count = entries.getLength();
		
		

		for(int a= 2; a<count; a++)
		{
			Node entry =entries.item(a);
			
			NamedNodeMap map = entry.getAttributes();
			
			//Saving every rate into a HashMap
			hm.put(map.getNamedItem("currency").getNodeValue(), Double.parseDouble(map.getNamedItem("rate").getNodeValue()));
			
			
			
			
		}
		
		
	
	}
	catch(ParserConfigurationException e)
	{
		e.printStackTrace();
	}
	catch(MalformedURLException e)
	{
		e.printStackTrace();
	}
	catch(SAXException e)
	{
		e.printStackTrace();
	}
	
	

	}
	
	public ArrayList<String> getList(Context context)
	{
		ArrayList<String> rates = new ArrayList<String>();
		String[] currencies = context.getResources().getStringArray(R.array.Source_Currency);
		rates.add("1");
		for(int i = 1; i<currencies.length;i++)
		{
		
		rates.add(hm.get(currencies[i]).toString());
		
	
		}
		
		
		return rates;
	}
	
	public static double getRate(String name)
	{
		return hm.get(name);
	}
	
	public static String getSymbol(int i)
	{
		switch(i){
		case 0:
			return "€";
		case 1:
			return "$";
		case 2:
			return "£";
		case 3:
			return "₹";
		case 4:
			return "¥";
		default:
			return "";
		
		}
	}
	
	public static HashMap<String,Double> getMap()
	{
		return hm;
	}
	public static void  setMap(HashMap<String,Double> map)
	{
		hm = map;
	}
	
}
