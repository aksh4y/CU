package com.akisoft.slidingmenu;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Events extends Fragment {
	ProgressDialog pDialog;
    private String curURL="http://www.christuniversity.in/ticker.php";

    public void init(String url) {

        curURL = "http://www.christuniversity.in/ticker.php";

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater
                .inflate(R.layout.events, container, false);
        if (curURL != null) {
        	
            WebView webview = (WebView) view.findViewById(R.id.webView1);

            webview.getSettings().setJavaScriptEnabled(true);

            webview.setWebViewClient(new webClient());
                     
            webview.loadUrl(curURL);
          

        }

        return view;

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