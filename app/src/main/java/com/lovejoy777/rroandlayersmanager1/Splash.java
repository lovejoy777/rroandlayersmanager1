package com.lovejoy777.rroandlayersmanager1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by lovejoy777 on 02/06/15.
 */
public class Splash extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                    Intent btIntent = new Intent("com.lovejoy777.rroandlayersmanager1.MENU");

                    startActivity(btIntent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }

        };
        logoTimer.start();
    }
}
