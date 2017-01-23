package com.akisoft.slidingmenu;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
public class Tour extends Fragment{
	

	private WebView mWebView;

	/** Called when the activity is first created. */
	     @Override
	     public View onCreateView(LayoutInflater inflater,
	             ViewGroup container,
	             Bundle savedInstanceState) {
	         View view = inflater
	                 .inflate(R.layout.events, container, false);


	         // html file with sample swf video in sdcard

	         //swf2.html points to swf in sdcard

	         mWebView = (WebView)getView().findViewById(R.id.webView1);
	         mWebView.getSettings().setJavaScriptEnabled(true);
	         mWebView.getSettings().setPluginState(PluginState.ON);
	         mWebView.getSettings().setAllowFileAccess(true);


	         
	        mWebView.loadUrl("http://christuniversity.in/virtualtour/index.html");
	         
return view;
	     }
}

