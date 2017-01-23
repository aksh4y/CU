package com.akisoft.slidingmenu;

import igp.notepad.NoteWriter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

public class NewWidgetNoteActivity extends Activity{

	 public void onCreate(Bundle state) {
		    super.onCreate(state);
		    setContentView(R.layout.dialog);
		    //setContentView(R.layout.dialog);
		    System.out.println("Creating NewWidgetNote");
		    
		    
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle(getResources().getString(R.string.newTitle));
			alert.setMessage(getResources().getString(R.string.newTitleDescription));

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			  String value = input.getText().toString();
			  // Do something with value!
			 
			 if(value.equals(""))
			 {
				 Toast toast = Toast.makeText(getApplicationContext(), "Error: Name allready exists",
						   Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 60);
				toast.show();
			
				
				
			 }
			 else
			  launchIntent(value);
			  }
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});

			alert.show();
		   
		  }
	 
	 protected void launchIntent(String value) {
			// TODO Auto-generated method stub
	    	 Intent intent = new Intent(this, NoteWriter.class);
	    	 intent.putExtra("title", value);
			startActivity(intent);
		}
}
