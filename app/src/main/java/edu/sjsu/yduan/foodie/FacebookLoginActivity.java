package edu.sjsu.yduan.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static edu.sjsu.yduan.foodie.R.id.button_facebook_login;

public class FacebookLoginActivity extends ProgressActivity implements View.OnClickListener,FacebookEvent{
    private CallbackManager mCallbackManager;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private TextView mEmailTextView;
    private ImageView icon;
//    private FirebaseAuth mAuth;
    private FacebookProxy fproxy;
    private LoginButton loginButton;
//    private final String TAG = this.getClass().getSimpleName();
    private boolean login = false;
    @VisibleForTesting

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        setTitle("Login");
        //views
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);
        mEmailTextView = (TextView) findViewById(R.id.email);
        icon = (ImageView) findViewById(R.id.usr_icon);
        //control
//        findViewById(R.id.button_facebook_signout).setOnClickListener(this);
//        mAuth = FirebaseAuth.getInstance();
        fproxy = FacebookProxy.getInstance();
        fproxy.SetEvent(this);
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile","user_friends","user_likes");
        //loginButton.setPublishPermissions("publish_actions");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                handleFacebookAccessToken(loginResult.getAccessToken());
                fproxy.GetUserProfile();
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
        loginButton.setOnClickListener(this);
    }
    @Override
    public void onStart() {
        super.onStart();
        fproxy.GetUserProfile();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
//    private void handleFacebookAccessToken(AccessToken token) {
//        findViewById(R.id.button_facebook_login).setVisibility(View.GONE);
//        showProgressDialog();
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    updateUI(user);
//                } else {
//                    Toast.makeText(FacebookLoginActivity.this, "Signin failed",Toast.LENGTH_SHORT).show();
//                    updateUI(null);
//                }
//            }
//        });
//    }

//    public void signOut() {
//        findViewById(R.id.button_facebook_login).setVisibility(View.GONE);
//        showProgressDialog();
////        mAuth.signOut();
//        LoginManager.getInstance().logOut();
//        updateUI(null);
//    }
    private void updateUI(FacebookUser user) {//FirebaseUser user
        if (user != null && !user.getID().isEmpty()) {
            mStatusTextView.setText(getString(R.string.facebook_status_fmt, user.getName()));
            mDetailTextView.setText(getString(R.string.facebook_id_fmt, user.getID()));
            mEmailTextView.setText(user.getEmail());
            Picasso.with(icon.getContext()).load(user.getImgUrl()).resize(120,120).into(icon);
//            findViewById(R.id.button_facebook_login).setVisibility(View.GONE);
//            findViewById(R.id.button_facebook_signout).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);
            mEmailTextView.setText(null);
            icon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fb_large, null));
//            findViewById(R.id.button_facebook_login).setVisibility(View.VISIBLE);
//            findViewById(R.id.button_facebook_signout).setVisibility(View.GONE);
            Util.FriendsAndMe.clear();
            FacebookProxy.close();
        }
//        updateShare(user);
        hideProgressDialog();
    }
//    private void updateShare(FacebookUser user){
//        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.share),MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        if(user!=null){
//            editor.putString("uname", user.getName());
//            editor.putString("ueml", user.getEmail());
//            editor.putString("uimgurl", user.getImgUrl());
//        }
//        else{
//            editor.remove("uname").remove("ueml").remove("uimgurl");
////            Util.FriendsAndMe.clear();
////            FacebookProxy.close();
//        }
//        editor.commit();
//    }

    @Override
    public void onClick(View v) {
        if(AccessToken.getCurrentAccessToken()==null) showProgressDialog();
        else updateUI(null);
//        if (v.getId() == R.id.button_facebook_signout) signOut();
    }

    public void onSearchPageComplete(ArrayList<BusinessAIO> alba){}
    public void onMyFriendsComplate(){}
    public void onPageLikedFriendsComplate(ArrayList<FacebookUser> alfu){}
    public void onProfileComplete(FacebookUser fu){
        updateUI(fu);
    }
}
