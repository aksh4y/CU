/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Advanced Android Development_
    http://commonsware.com/AdvAndroid
*/

   
package com.akisoft.slidingmenu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class LoremViewsFactory implements RemoteViewsService.RemoteViewsFactory {
  private static final String[] items={"Thimo", "Eder", "ist",
                                        "sit", "amet", "consectetuer",
                                        "adipiscing", "elit", "morbi",
                                        "vel", "ligula", "vitae",
                                        "arcu", "aliquet", "mollis",
                                        "etiam", "vel", "erat",
                                        "placerat", "ante",
                                        "porttitor", "sodales",
                                        "pellentesque", "augue",
                                        "purus"};
  private ArrayList<String> noteTitles;
  private Context ctxt=null;
  private int appWidgetId;

  public LoremViewsFactory(Context ctxt, Intent intent) {
      this.ctxt=ctxt;
      appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                      AppWidgetManager.INVALID_APPWIDGET_ID);
      stringLoader();
      

  }
  
  @Override
  public void onCreate() {
    // no-op
  }
  
  @Override
  public void onDestroy() {
    // no-op
  }

  @Override
  public int getCount() {
    return(noteTitles.size());
  }

  @Override
  public RemoteViews getViewAt(int position) {
    RemoteViews row=new RemoteViews(ctxt.getPackageName(),
                                     R.layout.row);
    
    row.setTextViewText(android.R.id.text1, noteTitles.get(position));

    Intent i=new Intent();
    Bundle extras=new Bundle();
    
    extras.putString(WidgetProvider.EXTRA_WORD, noteTitles.get(position));
    i.putExtras(extras);
    row.setOnClickFillInIntent(android.R.id.text1, i);
   

    return(row);
  }

  @Override
  public RemoteViews getLoadingView() {
    return(null);
  }
  
  @Override
  public int getViewTypeCount() {
    return(1);
  }

  @Override
  public long getItemId(int position) {
    return(position);
  }

  @Override
  public boolean hasStableIds() {
    return(true);
  }

  @Override
  public void onDataSetChanged() {
    // no-op
  }
  
  public void stringLoader()
  {
	  noteTitles = new ArrayList<String>();
		
		try{
		
		FileInputStream input = ctxt.openFileInput("lines.txt"); // Open input stream
		DataInputStream din = new DataInputStream(input);
		int sz = din.readInt(); // Read line count
		for (int i=0;i<sz;i++) { // Read lines
		   String line = din.readUTF();
		   noteTitles.add(line);
		}
		din.close();
		System.out.println("Strings geladen");
		}
		
		
		catch(FileNotFoundException e)
		{
			try {
				
					
			
				   //Modes: MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITABLE
				   FileOutputStream output = ctxt.openFileOutput("lines.txt",ctxt.MODE_PRIVATE);
				   DataOutputStream dout = new DataOutputStream(output);
				   dout.writeInt(noteTitles.size()); // Save line count
				   for(String line : noteTitles) // Save lines
				      dout.writeUTF(line);
				   dout.flush(); // Flush stream ...
				   dout.close(); // ... and close.
				}
				catch (IOException exc) { exc.printStackTrace(); }
		}
		
	catch(IOException e)
	{
		
	}
		

  }
}