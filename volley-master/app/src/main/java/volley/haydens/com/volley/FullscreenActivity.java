package com.agri;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;



/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_fullscreen);



           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {


                   final Intent mainIntent = new Intent(FullscreenActivity.this, LoginActivity.class);
                   FullscreenActivity.this.startActivity(mainIntent);
                   FullscreenActivity.this.finish();
               }
           }, 3000);





    }











}
