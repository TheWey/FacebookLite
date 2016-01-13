package com.wey.facebooklite;





import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewer extends Activity {


	WebView a;
	MyWebViewClient mywvc=new MyWebViewClient();
	MyWebChromeClient wcc=new MyWebChromeClient();
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_viewer);
		
		ActionBar q=getActionBar();
		q.hide();
		
		a=(WebView) findViewById(R.id.mainweb);
		a.setWebViewClient(mywvc);
		WebSettings ws=a.getSettings();
		ws.setSaveFormData(false);
		ws.setJavaScriptEnabled(true);
		a.setWebChromeClient(wcc);
		a.loadUrl("https://m.facebook.com");


			Intent intent = getIntent();
			String action = intent.getAction();
			String type = intent.getType();

			if (!intent.getAction().equals(Intent.ACTION_MAIN)){
				openURL(intent.getData().toString());
			}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_viewer, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int kc, KeyEvent e){
		if ((kc==KeyEvent.KEYCODE_BACK) && a.canGoBack()){
			a.goBack();
		}
		return true;
	}
	
	public void refresh(View view){
		a.loadUrl( "javascript:window.location.reload( true )" );
	}

	public void openURL(String URL){
		a.loadUrl( URL );
	}

	public void onShowCustomView(View view, CustomViewCallback cvc){
		
	}
	
	public void onHideCustomView(){
		
	}
	
	public void getVideoLoadingProgressView(){
		
	}
	

private class MyWebViewClient extends WebViewClient {
	

	
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
       if (Uri.parse(url).toString().contains("facebook.com")) {
            // This is my web site, so do not override; let my WebView load the page
            return false;
        }
        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
        else{
        	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        	startActivity(intent);
        	return true;
        }
       
        
    }

}

class MyWebChromeClient extends WebChromeClient {
    // The undocumented magic method override
    // Eclipse will swear at you if you try to put @Override here
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
        mUploadMessage = uploadMsg;  
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
        i.addCategory(Intent.CATEGORY_OPENABLE);  
        i.setType("image/*");  
        WebViewer.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), FILECHOOSER_RESULTCODE );

    }
	
	public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
	    callback.invoke(origin, true, false);
	 }
    }
    

private ValueCallback<Uri> mUploadMessage;
private final static int FILECHOOSER_RESULTCODE = 1;
@Override
protected void onActivityResult(int requestCode, int resultCode,
        Intent intent) {
    if (requestCode == FILECHOOSER_RESULTCODE) {
        if (null == mUploadMessage)
            return;
        Uri result = intent == null || resultCode != RESULT_OK ? null
                : intent.getData();
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;

    }
}

}
