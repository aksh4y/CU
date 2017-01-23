package igp.currency;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.akisoft.slidingmenu.R;

public class Rates extends Activity {

	ListView rateView;
	ListView currencyView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rates);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);

		currencyView = (ListView) findViewById(R.id.currencyView);
		
		String[] data = getResources().getStringArray(R.array.Source_Currency);
		currentExchangeRates loader = new currentExchangeRates();
		ArrayList<String> rates = loader.getList(this);

		currencyView.setAdapter(new RateArrayAdapter(this,data));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.rates, menu);
		return true;
	}

}
