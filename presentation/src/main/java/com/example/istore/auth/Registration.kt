package com.example.istore.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.istore.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login.input_email
import kotlinx.android.synthetic.main.login.input_password
import kotlinx.android.synthetic.main.registration_screen.*
import java.util.*

class Registration:  AppCompatActivity() {

    private val TAG = "RegisterActivity"

    private val DOMAIN_NAME = "gmail.com"

    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_screen)

        btn_register!!.setOnClickListener {
            Log.d(TAG, "onClick: attempting to register.")

            //check for null valued EditText fields
            if (!isEmpty(input_email.text.toString())
                && !isEmpty(input_password.text.toString())
                && !isEmpty(input_confirm_password.text.toString())
            ) {

                //check if user has a company email address
                if (isValidDomain(input_email.text.toString())) {

                    //check if passwords match
                    if (doStringsMatch(
                            input_password.text.toString(),
                            input_confirm_password.text.toString()
                        )
                    ) {

                        //Initiate registration task
                        registerNewEmail(input_email.text.toString(), input_password.text.toString())
                    } else {
                        Toast.makeText(
                            this,
                            "Passwords do not Match",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Please Register with Company Email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "You must fill out all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        hideSoftKeyboard()

        link_login.setOnClickListener {
            redirectLoginScreen()
        }
    }


    /**
     * Register a new email and password to Firebase Authentication
     * @param email
     * @param password
     */
    fun registerNewEmail(email: String?, password: String?) {
        showDialog()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener { task ->
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful)
                if (task.isSuccessful) {
                    Log.d(
                        TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().currentUser!!
                            .uid
                    )

                    //send email verificaiton
                    sendVerificationEmail()
                    FirebaseAuth.getInstance().signOut()

                    //redirect the user to the login screen
                    redirectLoginScreen()
                }
                if (!task.isSuccessful) {
                    Toast.makeText(
                        this, "Unable to Register",
                        Toast.LENGTH_SHORT
                    ).show()
                }
//                hideDialog()

                // ...
            }
    }

    /**
     * sends an email verification link to the user
     */
    private fun sendVerificationEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this,
                    "Sent Verification Email",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Couldn't Verification Send Email",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Returns True if the user's email contains '@tabian.ca'
     * @param email
     * @return
     */
    private fun isValidDomain(email: String): Boolean {
        Log.d(TAG, "isValidDomain: verifying email has correct domain: $email")
        val domain = email.substring(email.indexOf("@") + 1).toLowerCase(Locale.ROOT)
        Log.d(TAG, "isValidDomain: users domain: $domain")
        return domain == DOMAIN_NAME
    }

    /**
     * Redirects the user to the login screen
     */
    private fun redirectLoginScreen() {
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.")
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Return true if @param 's1' matches @param 's2'
     * @param s1
     * @param s2
     * @return
     */
    private fun doStringsMatch(s1: String, s2: String): Boolean {
        return s1 == s2
    }

    /**
     * Return true if the @param is null
     * @param string
     * @return
     */
    private fun isEmpty(string: String): Boolean {
        return string == ""
    }


    private fun showDialog() {
        progressBar!!.visibility = View.VISIBLE
    }

    private fun hideDialog() {
        if (progressBar!!.visibility == View.VISIBLE) {
            progressBar!!.visibility = View.INVISIBLE
        }
    }

    private fun hideSoftKeyboard() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
}