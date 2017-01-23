package igp.currency;



 
import java.util.ArrayList;
import java.util.Locale;

import com.akisoft.slidingmenu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class RateArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
	private  ArrayList<String> rates;
	
	currentExchangeRates loader = new currentExchangeRates();
 
	public RateArrayAdapter(Context context, String[] values) {
		super(context, R.layout.single_row, values);
		this.context = context;
		this.values = values;
		
		rates = loader.getList(context);
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		
		View rowView = inflater.inflate(R.layout.single_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.textView2);
		TextView rateView = (TextView) rowView.findViewById(R.id.rateText);
		rateView.setText(rates.get(position)+" "+loader.getSymbol(position));
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);
		textView.setText(values[position]);
 
		// Change icon based on name
		 String currency = values[position];

		if(values[position].equals("€uro"))
			imageView.setImageResource(R.drawable.euro);
		else
		{
		 
		int imageId = context.getResources().getIdentifier(currency.toLowerCase(Locale.getDefault()), "drawable", context.getPackageName());
		imageView.setImageResource(imageId);
		
		}
 
		return rowView;
	}
}