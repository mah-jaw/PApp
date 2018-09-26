package com.mahmoud.papp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.facebook.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_show_account.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import org.json.JSONException
import com.facebook.GraphResponse
import org.json.JSONObject
import com.facebook.GraphRequest
import com.facebook.AccessToken
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser




class ShowAccount : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var callbackManager: CallbackManager
    private var fbString=""
    lateinit var accGSIA: GoogleSignInAccount
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_account)
        if (intent.hasExtra("Google")){
            fbString=""
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            val account = GoogleSignIn.getLastSignedInAccount(this)

            if(!account!!.email.isNullOrEmpty()){
                tvEmail.setText(account.email)
                tvName.setText(account.displayName)
                Glide.with(this)
                        .load(account.photoUrl)
                        .into(ivImage);
            }else {
                Toast.makeText(this,"herecomes",Toast.LENGTH_LONG).show()

            }
            mAuth = FirebaseAuth.getInstance();
            val user=mAuth.currentUser
            val email:String?= user?.email!!
            if(!email!!.isEmpty()) tvEmail.setText(email)
        }
        btnSignOut.setOnClickListener({
            if (fbString.equals("facebook")) {
                Toast.makeText(this,"facebooksignout",Toast.LENGTH_LONG).show()

                signOutFB()
            }
            else signOut()
        })

        if (intent.hasExtra("Facebook")){
            Log.d("fb", "i am here")
            fbString="facebook"
            FacebookSdk.sdkInitialize(this.getApplicationContext());
            callbackManager = CallbackManager.Factory.create();
            val facebookSignInButton=findViewById(R.id.login_button) as LoginButton
            facebookSignInButton.setReadPermissions("email")
// Callback registration
            facebookSignInButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    // App code
                    handleFacebookAccessToken(loginResult.accessToken);
                }
                override fun onCancel() {
                    // App code
                }
                override fun onError(exception: FacebookException) {
                    // App code
                }
            })



    }
    }

    private fun signOut() {
        mAuth.signOut()
        startActivity(Intent(applicationContext,MainActivity::class.java))
    }
    private fun signOutFB() {
        LoginManager.getInstance().logOut();
        startActivity(Intent(applicationContext,MainActivity::class.java))
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("ghdfjhdf", "handleFacebookAccessToken:" + token)
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("dfaf", "signInWithCredential:success")
                        val user = mAuth!!.currentUser
                        val name= user!!.displayName.toString()
                        val email= user.email.toString()
                        val imgURL= user.photoUrl.toString()
                        println(imgURL+" fgfg")
                        val intent=Intent(this, ShowAccount::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("adfadfd", "signInWithCredential:failure", task.getException())
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }
}
