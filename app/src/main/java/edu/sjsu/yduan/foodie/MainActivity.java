package edu.sjsu.yduan.foodie;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.picasso.Picasso;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Reviews;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends ProgressActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, BusinessEvent, DialogEvent,ConnectionCallbacks,FacebookEvent,
        OnConnectionFailedListener{

    private TextView uname;
//    private TextView uemail;
    private ImageView uimg;
    private EditText searchtext;
    private boolean YelpProxyConnected = false;
    private boolean FBFriendsUpdated = false;
    private BusinessProxy BizProxy;
    private static final int ReqCheckNetwork = 0;
    private static final int ReqLocationPremission = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private GoogleApiClient mGoogleApiClient;
    private LocationListener locationListener;
//    private boolean reqSearch=false;
    private FacebookProxy fbProxy;
    private int searchMode=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateValuesFromBundle(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showProgressDialog();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        uname = (TextView) header.findViewById(R.id.header_uname);
//        uemail = (TextView) header.findViewById(R.id.header_uemail);
        uimg = (ImageView) header.findViewById(R.id.header_uimg);

        ImageButton searchbtn = (ImageButton) findViewById(R.id.searchButton);
        searchbtn.setOnClickListener(this);
        searchtext = (EditText) findViewById(R.id.searchText);
        AddEvent(this);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mCurrentLocation = location;
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                stopLocationUpdates();
            }
        };
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
        BusinessProxy.Initial(getString(R.string.yelp_id), getString(R.string.yelp_Secret));
        BizProxy = BusinessProxy.getInstance();
        BizProxy.SetEvent(this);
        BizProxy.SetupProxy();
        fbProxy = FacebookProxy.getInstance();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //return id == R.id.action_settings || super.onOptionsItemSelected(item);
		if (id == R.id.contact) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            String uriText = "mailto:" +
                    Uri.encode("liangxiaoyu828@gmail.com ; camopel@gmail.com")
                    +"?subject=" + Uri.encode("Feedback / Reporting a Bug") + "&body=" +
                    Uri.encode("Hello developers, \nI want to report a bug/give feedback corresponding to this app. \n\n.....\n\n-Your name");

            Uri uri = Uri.parse(uriText);
            intent.setData(uri);
            startActivity(Intent.createChooser(intent, "Send Email"));

            return true;
        }else if(id==R.id.about){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(String.format("%1$s", getString(R.string.app_name)));
            builder.setMessage(getResources().getText(R.string.abouts));
            builder.setPositiveButton("OK", null);
            AlertDialog welcomeAlert = builder.create();
            welcomeAlert.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_login) {
            startActivity(new Intent(this, FacebookLoginActivity.class));
        } else if (id == R.id.menu_fav) {
            startActivity(new Intent(this.getApplicationContext(), FavoriteActivity.class));
        } else if (id == R.id.menu_friends) {
            Intent i = new Intent(this, FriendsActivity.class);
            i.putExtra("lng",mCurrentLocation.getLongitude());
            i.putExtra("lat",mCurrentLocation.getLatitude());
			startActivity(i);
		} else if (id == R.id.menu_Restaurant) {
            onClick(null);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isNetworkConnected()) {
            showAlertDialog(ReqCheckNetwork, "Error!", "No Network Connection");
        }
        if (!hasLocationPermission()) requestLocationPermission(ReqLocationPremission);
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) startLocationUpdates();
//        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        if(AccessToken.getCurrentAccessToken()!=null) {
            fbProxy.SetEvent(this);
            fbProxy.GetUserProfile();
            fbProxy.GetMyFriends(null);
        }
        else {
            updateMyInfo(null);
            FBFriendsUpdated=true;
        }
        searchtext.setText(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()) stopLocationUpdates();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(getString(R.string.location_key), mCurrentLocation);
        savedInstanceState.putString(getString(R.string.last_updated_time), mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(getString(R.string.location_key))) {
                mCurrentLocation = savedInstanceState.getParcelable(getString(R.string.location_key));
            }
            if (savedInstanceState.keySet().contains(getString(R.string.last_updated_time))) {
                mLastUpdateTime = savedInstanceState.getString(getString(R.string.last_updated_time));
            }
        }
    }

    private void updateMyInfo(FacebookUser fu){
        if(fu==null){
            uname.setText(getString(R.string.unknow));
//            uemail.setText(getString(R.string.unknow_email));
            Drawable icon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_person_white, null);
            uimg.setImageDrawable(icon);
        }
        else{
            uname.setText(fu.getName());
//            uemail.setText(fu.getEmail());
            Picasso.with(uimg.getContext()).load(fu.getImgUrl()).resize(125,125).into(uimg);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ReqLocationPremission && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            showAlertDialog(ReqLocationPremission, "Error", "No permissions to read location!");
        }
    }

    //////////////////////
    ///Google Location////
    //////////////////////
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_CANCELED)
            showAlertDialog(REQUEST_CHECK_SETTINGS, "Error", "Location Setting Change is not allowed!");
    }
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener);
    }
    protected void startLocationUpdates() {
        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest)
                .setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                        final Status status = locationSettingsResult.getStatus();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                try{
                                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener);
                                }catch (SecurityException ignored){}
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException ignored){}
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings.";
                            showAlertDialog(REQUEST_CHECK_SETTINGS,"Error",errorMessage);
                    }
                }
            });
    }

    @Override
    public void onConnectionSuspended(int cause){}
    @Override
    public void onConnectionFailed (@NonNull ConnectionResult result){}
    @Override
    public void onConnected(Bundle connectionHint) {
        if (mCurrentLocation == null) {
            try {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            }catch (SecurityException ignored){}
        }
//        startLocationUpdates();
    }

    /////////////Proxy//////////////
    public void onDialogButtonClick(int req,int id){
//        if(req==ReqCheckNetwork||req==ReqLocationPremission||req==REQUEST_CHECK_SETTINGS)
        finish();
    }
    public void onYelpProxyConnected(){
        YelpProxyConnected=true;
        if(FBFriendsUpdated) hideProgressDialog();
    }
    public void onYelpSearchComplete(ArrayList<Business> businesses ){
        if(businesses.size()==0){
            hideProgressDialog();
            toastLongMessage("No Match Result!");
            searchtext.setText(null);
        }
        else if(fbProxy.user!=null) fbProxy.SearchPage(businesses);
        else {
            hideProgressDialog();
            searchtext.setText(null);
            toastLongMessage("Login First!");
        }
    }
    public void onYelpProxyError(int err){
        hideProgressDialog();
        toastMessage("BusinessProxy Error:"+BusinessProxy.parseError(err));
    }
    public void onYelpBusinessComplete(Business business){}
    public void onYelpReviewsComplete(Reviews rvs){}
    //////Search///////////

    @Override
    public void onClick(View v){
        if(YelpProxyConnected){
            showProgressDialog();
            if(v!=null){
                searchMode=1;
                BizProxy.SearchBusiness(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(),searchtext.getText().toString());
            }else {
                searchMode=0;
                BizProxy.SearchBusiness(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//            startLocationUpdates();
//            searchMode=(v!=null)?1:0;
//            reqSearch=true;
            }
        }else{
            toastMessage("Yelp proxy fail to setup. Retry later!");
            BizProxy.SetupProxy();
        }
    }
    public void onSearchPageComplete(ArrayList<BusinessAIO> alba){
        hideProgressDialog();
        Intent it = new Intent(this, BusinessListViewActivity.class);
        it.putExtra(getString(R.string.businesses_intent_key), alba);
        it.putExtra("mode",searchMode);
        startActivity(it);
    }
    public void onMyFriendsComplate(){
//        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.share),MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putStringSet("myfriends", friends);
        FBFriendsUpdated=true;
        if(YelpProxyConnected)hideProgressDialog();
    }
    public void onPageLikedFriendsComplate(ArrayList<FacebookUser> alfu){}
    public void onProfileComplete(FacebookUser fu){
        updateMyInfo(fu);
    }
}
