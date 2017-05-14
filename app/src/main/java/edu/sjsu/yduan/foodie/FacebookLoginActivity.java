package edu.sjsu.yduan.foodie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static edu.sjsu.yduan.foodie.R.id.button_facebook_login;

public class FacebookLoginActivity extends ProgressActivity implements View.OnClickListener{
    private CallbackManager mCallbackManager;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private FirebaseAuth mAuth;
    private LoginButton loginButton;
    private final String TAG = this.getClass().getSimpleName();
    private boolean login = false;
    @VisibleForTesting

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        //views
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);
        //control
        findViewById(R.id.button_facebook_signout).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile","user_friends","user_likes");
        //loginButton.setPublishPermissions("publish_actions");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                updateUI(null);
            }

            @Override
            public void onError(FacebookException error) {
                updateUI(null);
            }
        });
//        loginButton.setOnClickListener(this);
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        findViewById(R.id.button_facebook_login).setVisibility(View.GONE);
        showProgressDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    Toast.makeText(FacebookLoginActivity.this, "Signin failed",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    public void signOut() {
        findViewById(R.id.button_facebook_login).setVisibility(View.GONE);
        showProgressDialog();
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        updateUI(null);
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mStatusTextView.setText(getString(R.string.facebook_status_fmt, user.getDisplayName()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            findViewById(R.id.button_facebook_login).setVisibility(View.GONE);
            findViewById(R.id.button_facebook_signout).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);
            findViewById(R.id.button_facebook_login).setVisibility(View.VISIBLE);
            findViewById(R.id.button_facebook_signout).setVisibility(View.GONE);
        }
        updateShare(user);
        hideProgressDialog();
    }
    private void updateShare(FirebaseUser user){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.share),MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(user!=null){
            editor.putString("uname", user.getDisplayName());
            editor.putString("ueml", user.getEmail());
            editor.putString("uimgurl", user.getPhotoUrl().toString());
//            editor.putString("facebookid",user.getProviderData().get(0).getUid());
//            editor.putString("providerid",user.getProviderData().get(0).getProviderId());
        }
        else{
            editor.remove("uname").remove("ueml").remove("uimgurl");//.remove("myfriends");//.remove("facebookid")
            Util.FriendsAndMe.clear();
            FacebookProxy.close();
        }
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_facebook_signout) signOut();
    }



}
