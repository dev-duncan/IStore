package com.example.istore

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    //After completion of 2000 ms, the next activity will get started.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        //This method is used so that your splash activity
        //can cover the entire screen.

        Handler().postDelayed(
            {
                val i = Intent(
                    this@SplashActivity,
                    HomeActivity::class.java,
                )
                //Intent is used to switch from one activity to another.
                startActivity(i)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                //invoke the SecondActivity.
                finish()
                //the current activity will get finished.
            },
            SPLASH_SCREEN_TIME_OUT.toLong(),
        )
    }

    companion object {
        const val SPLASH_SCREEN_TIME_OUT: Int =2000
    }

}