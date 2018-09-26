package com.mahmoud.papp

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.util.Log
import android.view.View
import android.widget.Button
import com.facebook.CallbackManager
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.facebook.login.widget.LoginButton
import java.util.*
import java.util.Arrays.asList
import java.util.Arrays.asList







private val TAG:String="MainActivity"
class MainActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var callbackManager: CallbackManager
    private var email:String = null.toString()
    private var password:String = null.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance();
        sign_in_button.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext,GoogleActivity::class.java))
        })
        btnSignIn.setOnClickListener({

            signInFirebase()
        })
        val EMAIL = "email"
        callbackManager = CallbackManager.Factory.create();
        val loginButton = findViewById<View>(R.id.login_button) as LoginButton
        loginButton.setReadPermissions(Arrays.asList(EMAIL))
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Toast.makeText(applicationContext,"Successful facebook login", Toast.LENGTH_LONG).show()
                val intent= Intent(applicationContext, ShowAccount::class.java)
                intent.putExtra("Facebook", "facebook")
                startActivity(intent)
            }

            override fun onCancel() {
                Toast.makeText(applicationContext,"onCancel facebook login", Toast.LENGTH_LONG).show()
                // App code
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(applicationContext,"onError facebook login", Toast.LENGTH_LONG).show()
                // App code
            }
        })

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun signInFirebase() {

        email= etEmail.text.toString()
        password= etPassword.text.toString()
        if(!email.isEmpty() && !password.isEmpty()){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("SignInFirebase", "signInWithEmail:success")
                val user = mAuth!!.getCurrentUser()
                startActivity(Intent(applicationContext,ShowAccount::class.java))
            } else {
                // If sign in fails, display a message to the user.
                Log.w("SignInFirebase", "signInWithEmail:failure", task.exception)
                Toast.makeText(applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
            }

            // ...
        }

    }else{
            Toast.makeText(applicationContext, "Enter Email and Password", Toast.LENGTH_LONG).show()
        }
    }
}
