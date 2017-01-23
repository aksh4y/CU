package com.akisoft.slidingmenu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.akisoft.slidingmenu.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {
	
	public HomeFragment(){}
	
	@Override
	public void onResume() {
	    super.onResume();
	    // Set title
	    getActivity().setTitle("Home");
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
      /*  Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        */
        String date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
        TextView dateView=(TextView) rootView.findViewById(R.id.dateView);
        TextView day=(TextView) rootView.findViewById(R.id.day);
        TextView time=(TextView) rootView.findViewById(R.id.time);
        time.setSingleLine(false);
        dateView.setText(date);
        date=new SimpleDateFormat("EEE").format(new Date());
        day.setText(date);
        date=new SimpleDateFormat("hh:mm").format(new Date());
        time.setText(date);
        date=new SimpleDateFormat("a").format(new Date());
        time.setText(time.getText().toString()+"\n"+date);
        
        return rootView;
    }
}
