package igp.translator;

import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.akisoft.slidingmenu.R;

public class TranslateFrag extends Fragment implements OnClickListener, OnInitListener {

	private int check_code = 0;
	Spinner inputLangSpinner;
	Spinner outputLangSpinner;
	static EditText textInputEditText;
	Button translateButton;
	Button clearButton;
	EditText translatedView;
	ImageButton speaker;
	private TextToSpeech convert;
	Editable Input;
	ProgressDialog pDialog;
	String result;
	String res;
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		 
		 View rootView = inflater.inflate(R.layout.translator, container, false);
   	 getActivity().setTitle("Translator");
   	 
  // 	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
   //	StrictMode.setThreadPolicy(policy);
   	Intent check = new Intent();

    check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);

    startActivityForResult(check, check_code);
   	 
 	
	System.out.println(rootView);
	
	inputLangSpinner = (Spinner) rootView.findViewById(R.id.inputLangSpinner);
	outputLangSpinner = (Spinner) rootView.findViewById(R.id.outputLangSpinner1);
	
	textInputEditText = (EditText) rootView.findViewById(R.id.textInputEditText);
	textInputEditText.requestFocus();
	
	
	translatedView = (EditText) rootView.findViewById(R.id.translate2View);
	
	translateButton = (Button) rootView.findViewById(R.id.translateButton);
	translateButton.setOnClickListener(this);
	
	clearButton = (Button) rootView.findViewById(R.id.clearButton1);
	clearButton.setOnClickListener(this);
	
	speaker = (ImageButton) rootView.findViewById(R.id.speaker);
	speaker.setOnClickListener(this);
			
	
	
	//Bring up the Keyboard manually
	InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	imm.showSoftInput(textInputEditText, InputMethodManager.SHOW_IMPLICIT);
	
	//Setting up entries for Spinners
	ArrayAdapter<CharSequence> adapterInput = ArrayAdapter.createFromResource(getActivity(),
	        R.array.Source_Language, android.R.layout.simple_spinner_item);
	adapterInput.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	inputLangSpinner.setAdapter(adapterInput);
	
	ArrayAdapter<CharSequence> adapterOutput = ArrayAdapter.createFromResource(getActivity(),
	        R.array.Source_Language, android.R.layout.simple_spinner_item);
	adapterOutput.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	outputLangSpinner.setAdapter(adapterOutput);
	outputLangSpinner.setSelection(1); 
   	 
   	 
   	 return rootView;
	 }

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
	
	if(v.getId() == R.id.translateButton){
		//translation on Click
		Input = textInputEditText.getText();
		System.out.println(Input.toString());
		new translator().execute();
		
		}
		else
		if(v.getId() == R.id.speaker)
		{
			switch (outputLangSpinner.getSelectedItemPosition()){
			
			case 0:
				
				convert.setLanguage(Locale.GERMAN);
				break;
				
			
			case 1:
				convert.setLanguage(Locale.ENGLISH);
				System.out.println("English has been choosen");
				break;
				
			case 2:
			
				Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Sorry Language is not Supported",
						   Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 60);
				toast.show();
				break;
			
			
			case 3:
				convert.setLanguage(Locale.ITALIAN);
				System.out.println("Italian has been choosen");
				break;
			
			case 4:
				convert.setLanguage(Locale.FRENCH);
				System.out.println("French has been choosen");
				break;
				
			default:
			{
				System.out.println("Default has been choosen");
				convert.setLanguage(Locale.ENGLISH);
				
			}
			
			}
			
			convert.speak(translatedView.getText().toString(), TextToSpeech.QUEUE_ADD, null);
			
		}
		else
		{
			textInputEditText.setText("");
			translatedView.setText("");
		}
}

public void executeTranslation()
{
	res="true";
	try
	{
	result=Translator.translate(Input.toString(),Translator.languageChooser(inputLangSpinner.getSelectedItemPosition()),Translator.languageChooser(outputLangSpinner.getSelectedItemPosition()));
	}
	catch(Exception e)
	{
		res="false";
		Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
	}
}
class translator extends AsyncTask<String,String,String>
{

	@Override
	protected String doInBackground(String... args) {
try {
			
			
			executeTranslation();		}
		catch(Exception e)
		{
			
			e.printStackTrace();
		}
		return res;
	}
	
	protected void onPreExecute() {
		super.onPreExecute();

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Translating...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}
	
	protected void onPostExecute(String url) {
		// dismiss the dialog once done
		if(translatedView !=null)
		{
		translatedView.setText(result);
		translatedView.requestFocus();
		}
		else
			textInputEditText.setText(result);
		Toast toast;
		if(res=="true")
			toast = Toast.makeText(getActivity().getApplicationContext(), "Translation complete",
				   Toast.LENGTH_LONG);
		else
			toast = Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection, cannot translate!",
					   Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP, 0, 60);
		toast.show();

		pDialog.dismiss();
	}
}

public static void closeKeyboard(Context c) {
    InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
    mgr.hideSoftInputFromWindow(textInputEditText.getWindowToken(), 0);
    System.out.println("here");
}

public static void openKeyboard(Context c)
{
	 InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
	 mgr.showSoftInput(textInputEditText, 0);
	    System.out.println("here2");
}



public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == check_code) {

        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

            // success, create the TTS instance

            convert = new TextToSpeech(getActivity(), this);

        }

        else {

            // missing data, install it

            Intent installIntent = new Intent();

            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);

            startActivity(installIntent);

        }

    }

}

@Override
public void onInit(int status) {
	// TODO Auto-generated method stub
	
    if (status == TextToSpeech.SUCCESS) {

        //Toast.makeText(getActivity(),"Engine is initialized", Toast.LENGTH_LONG).show();

       

        int result = convert.setLanguage(Locale.GERMANY);
        

       

        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

            Log.i("TTS", "This Language is not supported");

        }

        else {                

            //speakOut("Ich");

          Log.i("TTS", "This Language is supported");

        }

    }

    else if (status == TextToSpeech.ERROR) {

        Toast.makeText(getActivity(),"Error occurred while initializing engine", Toast.LENGTH_LONG).show();

    }
	
}

}



