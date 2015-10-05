package edu.uma.umasimple;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import edu.uma.umasimple.R;

public class splash extends Activity{
	ProgressDialog mProgress;
	boolean loadingFinished = true;
	boolean redirect = false;
	WebView mWebView;
	private final int SPLASH_DISPLAY_LENGHT = 4000;
	Context tthis = this;
	 public void onCreate(Bundle saveInstanceState)
     {
             super.onCreate(saveInstanceState);
             setContentView(R.layout.splash_screen);
             mWebView=(WebView)findViewById(R.id.splashView);
             mWebView.getSettings().setBuiltInZoomControls(false);
             mWebView.loadUrl("file:///android_asset/spashweb/splash.html");
             final Intent myintent = new Intent(tthis, MainActivity.class);

             new Handler().postDelayed(new Runnable(){
                  @Override
                  public void run() {
                      startActivity(myintent);
                      finish();
                  }
              }, SPLASH_DISPLAY_LENGHT);
     }
	 

                
     }

