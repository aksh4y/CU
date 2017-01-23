package com.akisoft.slidingmenu;

import com.akisoft.slidingmenu.R;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Gallery extends Fragment {
	
	ProgressDialog pDialog;
	public Gallery(){}
	private String curURL="http://www.flickr.com/photos/christuniversity";

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.gallery, container, false);
if (curURL != null) {
        	
            WebView webview = (WebView) rootView.findViewById(R.id.webView1);

            webview.getSettings().setJavaScriptEnabled(true);

            webview.setWebViewClient(new webClient());
                     
            webview.loadUrl(curURL);
}
        return rootView;
    }

public void updateUrl(String url) {
    curURL = url;
    WebView webview = (WebView) getView().findViewById(R.id.webView1);
    webview.getSettings().setJavaScriptEnabled(true);
    webview.loadUrl(url);
}

private class webClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        return false;

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

     super.onPageStarted(view, url, favicon);
     pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
    }

   @Override
   public void onPageFinished(WebView view, String url) {

       pDialog.dismiss();
   }
}

}
