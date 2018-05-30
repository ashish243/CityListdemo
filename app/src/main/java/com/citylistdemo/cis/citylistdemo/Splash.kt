package com.citylistdemo.cis.citylistdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window

/**
 * Created by cis on 28/5/18.
 */
class Splash (): Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash)

        Handler().postDelayed({
            /* Create an Intent that will start the MainActivity. */
            val intent = Intent(this, MainActivity::class.java)
            // start your next activity
            startActivity(intent)
            finish()
        }, 2000)
    }

}