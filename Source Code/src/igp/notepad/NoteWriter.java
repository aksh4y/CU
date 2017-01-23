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

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.akisoft.slidingmenu.R;
import com.akisoft.slidingmenu.WidgetProvider;

public class NoteWriter extends Activity {
	
	String dataString;
	LinedEditText text;
	String title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_editor);
		Bundle bundle = getIntent().getExtras();
		if(bundle.getString("title")!=null)
		title = bundle.getString("title");
		else
			title= getIntent().getStringExtra(WidgetProvider.EXTRA_WORD);
		text = (LinedEditText)findViewById(R.id.note);
		
		System.out.println("OnCreate");
		
		
		
		
		try
		{
			File file = new File(getDir("data", MODE_PRIVATE), title); 
			FileInputStream f = new FileInputStream(file);
			ObjectInputStream s = new ObjectInputStream(f);
			text.setText((String) s.readObject());
			s.close();
	    	
		} 
		catch (FileNotFoundException e1)
		{
			System.out.println("Title wäre gewesen: "+title);
			try{
				File file = new File(getDir("data", MODE_PRIVATE), title);    
				ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
				outputStream.writeObject(text.getText().toString());
				outputStream.flush();
				outputStream.close();
				
				System.out.println("Erstelle File");
				
				ArrayList<String> titles = new ArrayList<String>();
				
				FileInputStream input = openFileInput("lines.txt"); // Open input stream
				DataInputStream din = new DataInputStream(input);
				int sz = din.readInt(); // Read line count
				for (int i=0;i<sz;i++) { // Read lines
				   String line = din.readUTF();
				   
				  titles.add(line);
				}
				din.close();
				titles.add(title);
				System.out.println("Geladen");
				
					
				
				
				 
				 //Modes: MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITABLE
				   FileOutputStream output = openFileOutput("lines.txt",MODE_PRIVATE);
				   DataOutputStream dout = new DataOutputStream(output);
				   dout.writeInt(titles.size()); // Save line count
				   for(String line : titles) // Save lines
				      dout.writeUTF(line);
				   dout.flush(); // Flush stream ...
				   dout.close(); // ... and close.
				   System.out.println("Gespeichert");
				
			}
			catch (IOException e3)
			{
				e3.printStackTrace();
			}
			
		}


		catch (IOException e3)
		{
			e3.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Map Wiederherstgestellt");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.note_writer, menu);
	    
	    return super.onCreateOptionsMenu(menu);

	}
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		if(item.getItemId() == R.id.doneButton)
		{
		try{
	    // Handle presses on the action bar items
			File file = new File(getDir("data", MODE_PRIVATE), title);    
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
			outputStream.writeObject(text.getText().toString());
			outputStream.flush();
			outputStream.close();
		finish();
		}
		
		catch (IOException e3)
		{
			e3.printStackTrace();
		}
		
		}
		
		return true;
	}
	
	public static class LinedEditText extends EditText {
        private Rect mRect;
        private Paint mPaint;

        // This constructor is used by LayoutInflater
        public LinedEditText(Context context, AttributeSet attrs) {
            super(context, attrs);

            // Creates a Rect and a Paint object, and sets the style and color of the Paint object.
            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(0x800000FF);
        }

        /**
         * This is called to draw the LinedEditText object
         * @param canvas The canvas on which the background is drawn.
         */
        @Override
        protected void onDraw(Canvas canvas) {

            // Gets the number of lines of text in the View.
            int count = getLineCount();

            // Gets the global Rect and Paint objects
            Rect r = mRect;
            Paint paint = mPaint;

            /*
             * Draws one line in the rectangle for every line of text in the EditText
             */
            for (int i = 0; i < count; i++) {

                // Gets the baseline coordinates for the current line of text
                int baseline = getLineBounds(i, r);

                /*
                 * Draws a line in the background from the left of the rectangle to the right,
                 * at a vertical position one dip below the baseline, using the "paint" object
                 * for details.
                 */
                canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
            }

            // Finishes up by calling the parent method
            super.onDraw(canvas);
        }
    }

}
