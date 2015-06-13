package com.lovejoy777.rroandlayersmanager1;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by lovejoy777 on 02/06/15.
 */
public class Splash extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ImageView iv = (ImageView) findViewById(R.id.imageView1);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(),R.anim.splash);

        iv.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                finish();

                Intent menuactivity = new Intent(Splash.this, menu.class);

                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anni1, R.anim.anni2).toBundle();
                startActivity(menuactivity, bndlanimation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}