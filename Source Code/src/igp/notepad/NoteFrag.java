package igp.notepad;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akisoft.slidingmenu.R;

public class NoteFrag extends android.support.v4.app.ListFragment {
	
	ListView noteMenuListView;
	NoteAdapter adapter;
	ArrayList<String> noteTitles;
	
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	
	}

	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
			 
			 View view = inflater.inflate(R.layout.activity_note_menu, container, false);
			 getActivity().setTitle("Notes");
			 adapter = new NoteAdapter(getActivity());
			 
			 setListAdapter(adapter);
			
			
			 return view;
}
	 public void onResume()
	 {
		 super.onResume();
		 adapter.stringLoader();
		 adapter.notifyDataSetChanged();
		
		
	 }
	 
	 public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
	        inflater.inflate(R.menu.note, menu);
	        
	        super.onCreateOptionsMenu(menu, inflater);
	    }
	 
	 
	 public boolean onOptionsItemSelected(MenuItem item){
		 
		 
		 if(item.getItemId() == R.id.New) {
		        
	    	    
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.dialog_theme);

				alert.setTitle(getResources().getString(R.string.newTitle));
				alert.setMessage(getResources().getString(R.string.newTitleDescription));

				// Set an EditText view to get user input 
				final EditText input = new EditText(getActivity());
				alert.setView(input);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				  String value = input.getText().toString();
				  // Do something with value!
				 
				 if(allreadyCreated(value))
				 {
					 Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Error: Name already exists",
							   Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 0, 60);
					toast.show();
				
					
					
				 }
				 else
					 if(value.equals(""))
					 {

						 Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Error: Name cannot be emtpy",
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
		 
		 return super.onOptionsItemSelected(item);
	 }
	 
	 
	 protected void launchIntent(String value) {
			// TODO Auto-generated method stub
	    	 Intent intent = new Intent(getActivity(), NoteWriter.class);
	    	 intent.putExtra("title", value);
			startActivity(intent);
		}
	 

	 
	 public class NoteAdapter extends BaseAdapter implements OnClickListener{
			Context context;
			TextView noteTitel;
			ListView list;
			ImageButton delete;
			ImageButton edit;
			
			public NoteAdapter()
			{
				
			}
			
			public NoteAdapter(Context context) {
				// TODO Auto-generated constructor stub
				this.context = context;
				
				noteTitles = stringLoader();
				
				
				System.out.println("Adapter erstellt");
				
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if(noteTitles != null)
				return noteTitles.size();
				else
					return 0;
			}

			@Override
			public String getItem(int position) {
				// TODO Auto-generated method stub
				return noteTitles.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
			    View _view = (View)View.inflate(context, R.layout.note_edit, null);
				noteTitel = (TextView) _view.findViewById(R.id.noteTitel);
				noteTitel.setOnClickListener(this);
				delete = (ImageButton) _view.findViewById(R.id.deleteButton);
				delete.setOnClickListener(adapter);
				edit = (ImageButton) _view.findViewById(R.id.editButton);
				edit.setOnClickListener(adapter);
				
				if (noteTitles != null)
				{
				noteTitel.setText(noteTitles.get(position));
				noteTitel.setGravity(Gravity.CENTER_VERTICAL);
				
				}
				return _view;
			}

			@Override
			public void onClick(View v) {
				final int position = getListView().getPositionForView(v);
				if(v.getId() == R.id.noteTitel)
				{
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(context, NoteWriter.class);
				intent.putExtra("title",noteTitles.get(position));
				context.startActivity(intent);
				}
				if(v.getId() == R.id.deleteButton)
				{
					
					AlertDialog.Builder alert = new AlertDialog.Builder(context,R.style.dialog_theme);
				
					alert.setTitle(getResources().getString(R.string.deleteTitle)+" \""+getItem(position)+"\"") ;
					alert.setMessage(getResources().getString(R.string.deleteTitleDescription));

					
					

					alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
				
					  // Delete the File and Update the Arraylist Data
					
					  
						File f = new File(context.getDir("data", context.MODE_PRIVATE), getItem(position));
						f.delete();
						ArrayList<String> list = stringLoader();
						list.remove(position);
						stringSaver(list);
						notifyDataSetChanged();
					}
					
					});
					
					alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
						  public void onClick(DialogInterface dialog, int whichButton) {
						    // Canceled.
						  }
						});

					
					alert.show();
					
					
				
					
					
				}
				if(v.getId() == R.id.editButton)
				{
					AlertDialog.Builder alert = new AlertDialog.Builder(context,R.style.dialog_theme);
		

					alert.setTitle(getResources().getString(R.string.editTitle));
					alert.setMessage(getResources().getString(R.string.editTitleDescription));

					// Set an EditText view to get user input 
					final EditText input = new EditText(context);
					input.setText(getItem(position));
					input.setSelection(getItem(position).length());
					alert.setView(input);

					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					  String value = input.getText().toString();
					  // Do something with value!
					  System.out.println(adapter.getItem(position));
					  
						 if(allreadyCreated(value))
						 {
							 Toast toast = Toast.makeText(context.getApplicationContext(), "Error: Name already exists",
									   Toast.LENGTH_LONG);
							toast.setGravity(Gravity.TOP, 0, 60);
							toast.show();
						
							
							
						 }
						 else
						 if(value.equals(""))
						 {

							 Toast toast = Toast.makeText(context.getApplicationContext(), "Error: Name cannot be emtpy",
									   Toast.LENGTH_LONG);
							toast.setGravity(Gravity.TOP, 0, 60);
							toast.show();
						 }
						 else
						 { String text ="";
							 try{
							 
						//Delete Old File and Create a new on -- Renaming the File does not work unfortunately		 
								 
							 File file = new File(context.getDir("data", context.MODE_PRIVATE), adapter.getItem(position)); 
								FileInputStream f = new FileInputStream(file);
								ObjectInputStream s = new ObjectInputStream(f);
								text= (String) s.readObject();
								s.close();
								file.delete();
							 }
							 catch(Exception e)
							 {
								 e.printStackTrace();
							 }
							 
							 
							 
					/*	File oldFile = context.getFileStreamPath(adapter.getItem(position));
						  File newFile = context.getFileStreamPath(value);
						  oldFile.renameTo(newFile);*/
					
					try{
						File file = new File(context.getDir("data", context.MODE_PRIVATE), value);    
								ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
								outputStream.writeObject(text);
								outputStream.flush();
								outputStream.close(); 
							 
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
						
						  noteTitles.set(position, value);
						  stringSaver(noteTitles);
						  notifyDataSetChanged();
						  
						  //  stringSaver(noteTitles);
						
						
						 }
						 

					  }
					});

					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int whichButton) {
					    // Canceled.
					  }
					});

					alert.show();
					
			
				}
			}
			
			public ArrayList<String>stringLoader()
			{		
				noteTitles = new ArrayList<String>();
				
				try{
				
				FileInputStream input = context.openFileInput("lines.txt"); // Open input stream
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
						   FileOutputStream output = context.openFileOutput("lines.txt",context.MODE_PRIVATE);
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
				return noteTitles;

				
				
				
		}
			
			public void stringSaver(ArrayList<String> list)
			{
				



				
				try{
				 FileOutputStream output = context.openFileOutput("lines.txt",context.MODE_PRIVATE);
				   DataOutputStream dout = new DataOutputStream(output);
				   dout.writeInt(list.size()); // Save line count
				   for(String line : list) // Save lines
				      dout.writeUTF(line);
				   dout.flush(); // Flush stream ...
				   dout.close(); // ... and close.
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
	 
		
	 }
	 
	
	 public boolean allreadyCreated(String title)
	 {
		 if(noteTitles.contains(title))
			 return true;
		 else
			 return false;
	 }



}


	
	
	 


