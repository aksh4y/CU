package com.akisoft.slidingmenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

	public class ListFrag extends Fragment { 
		public ListFrag(){}
		final String[] items = new String[] { "This", "Could", "Be",
	            "The", "Schedule", "Later", "On", "Or",
	       "Maybe", "Something Else" };

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 
	        View rootView = inflater.inflate(R.layout.list, container, false);
	        ListView list = (ListView)rootView.findViewById(R.id.listView1);
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
	        list.setAdapter(adapter);
	        list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View V,
						int position, long id) {
					Toast.makeText(getActivity(), "Item Clicked:"+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
					
				}
	        	
	        });
	        return rootView;
	    }

	    
	   
	}