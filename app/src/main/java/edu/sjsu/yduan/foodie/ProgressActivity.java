package edu.sjsu.yduan.foodie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by yduan on 5/9/2017.
 */

public class ProgressActivity extends AppCompatActivity {
    public ProgressDialog mProgressDialog;
    private DialogEvent Event;
    public void AddEvent(DialogEvent e){
        Event = e;
    }
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    public void showAlertDialog(final int req, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message).setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(Event!=null) Event.onDialogButtonClick(req,id);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void toastMessage(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }
    public void toastLongMessage(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_LONG).show();
    }
    public boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    public boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission)== PackageManager.PERMISSION_GRANTED;
    }
    public boolean hasLocationPermission() {
        return hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                || hasPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
    }
    public void requestLocationPermission(int req) {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},
                req);
    }
}
