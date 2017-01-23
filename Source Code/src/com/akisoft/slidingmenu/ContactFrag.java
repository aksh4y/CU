package com.akisoft.slidingmenu;


import com.akisoft.slidingmenu.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.graphics.PorterDuff;

public class ContactFrag extends android.support.v4.app.ListFragment {
	
	ListView exv;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
	
	
	View view = inflater.inflate(R.layout.contactfragment, container, false);
   	 getActivity().setTitle("Contacts");
   	 
  
	ContactsAdapter adapter = new ContactsAdapter(getActivity());
	setListAdapter(adapter);
	//ExpandableList is expanded at startup
	
	
	
	
	
   	 
   	 
   	 return view;
	}
	
	public class ContactsAdapter extends BaseAdapter implements OnClickListener{
		
		private Context context;
		String [] parentList;
		String[] address;
		String[] number;
		
		TextView addressView;
		TextView numberView;
		TextView parentView;
		ImageButton callButton;
		ImageButton mapButton;
		
		public ContactsAdapter()
		{
			
		}
		
		public ContactsAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
			address = context.getResources().getStringArray(R.array.address);
			number = context.getResources().getStringArray(R.array.Numbers);
			parentList= context.getResources().getStringArray(R.array.names);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			 return number.length;
		}

		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			return number[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			View _view;
			if(position == 1 || position == 2 || position ==3 || position==4)
			{
				_view = (View)View.inflate(context, R.layout.contactdataviewsmall, null);
				numberView = (TextView) _view.findViewById(R.id.numberView);
				numberView.setText(number[position]);
				parentView = (TextView) _view.findViewById(R.id.parentView);
				parentView.setText(parentList[position]);
				callButton = (ImageButton) _view.findViewById(R.id.callButton2);
				callButton.setOnClickListener(this);
			}
			else
			{
			_view = (View)View.inflate(context, R.layout.contactdataview, null);
			addressView = (TextView) _view.findViewById(R.id.adressField);
			addressView.setText(address[position]);
			numberView = (TextView) _view.findViewById(R.id.numberField);
			numberView.setText(number[position]);
			parentView = (TextView) _view.findViewById(R.id.parentView);
			parentView.setText(parentList[position]);
			
			callButton = (ImageButton) _view.findViewById(R.id.callButton);
			callButton.setOnClickListener(this);
			
			mapButton = (ImageButton) _view.findViewById(R.id.mapButton);
			mapButton.setOnClickListener(this);
			
			}
			
			// TODO Auto-generated method stub
			return _view;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final int position = getListView().getPositionForView(v);
			if(v.getId() == R.id.callButton || v.getId() == R.id.callButton2)
			{
				Intent intent = new Intent(Intent.ACTION_DIAL);
			    intent.setData(Uri.parse("tel: "+number[position]));
			    context.startActivity(intent);
			}
		}

	
}}
