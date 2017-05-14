package edu.sjsu.yduan.foodie;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by xiaoyuliang on 5/12/17.
 */

public class FriendsActivity extends AppCompatActivity implements OnMapReadyCallback {

    ListView mListView;
    MapView mapView;
    GoogleMap googleMap;
    LatLngBounds.Builder builder;
    final int MY_PERMISSION_LOCATION = 10;
    ArrayList<Friend> friendList;
    CameraUpdate cameraUpdate;
    double lng;
    double lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(edu.sjsu.yduan.foodie.R.layout.activity_friend);
        final Context context = this;
        Intent i = getIntent();
        lng = i.getDoubleExtra("lng",0);
        lat = i.getDoubleExtra("lat",0);
        friendList = Friend.getRecipesFromFile("friends.json", this);
        FriendsAdapter adapter = new FriendsAdapter(this, friendList);
        mListView = (ListView) findViewById(R.id.friends_list_view);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Friend selectedRecipe = friendList.get(position);
                Intent detailIntent = new Intent(context, FriendDetailActivity.class);
                detailIntent.putExtra("pos",position);
//                detailIntent.putExtra("mname", selectedRecipe.name);
//                detailIntent.putExtra("murl", selectedRecipe.image);
//                detailIntent.putExtra("des", selectedRecipe.Label);
                startActivity(detailIntent);
            }

        });
        mapView = (MapView)findViewById(R.id.mapView);
    }
    @Override
    public void onStart() {
        super.onStart();
        if (mapView != null) {
            mapView.getMapAsync(this);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        this.googleMap = gMap;
        builder = new LatLngBounds.Builder();
        Random r = new Random();
        int i=0;
        for (Friend f : friendList) {
            i++;
            double offset_lat = r.nextDouble()*0.04-0.02+lat;
            double offset_lng = r.nextDouble()*0.04-0.02+lng;
            LatLng ll = new LatLng(offset_lat,offset_lng);
            builder.include(ll);
            Bitmap bmp = Bitmap.createBitmap(36, 47, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            Paint color = new Paint();
            color.setTextSize(12);
            color.setColor(Color.WHITE);
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker), 0,0, color);
            canvas.drawText(String.valueOf(i), 7, 18, color);
            Marker m = googleMap.addMarker(new MarkerOptions()
                    .position(ll)
                    .title(f.name)
                    .snippet(f.Preference)
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.fromBitmap(bmp)));//.anchor(0.5f, 1)
        }
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLngBounds bounds = builder.build();
        cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 200);
        showMap();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSION_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showMap();
        }
    }
    private void showMap(){
        int c1 = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int c2 = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int c3 = PackageManager.PERMISSION_GRANTED;
        if (c1 != c3 || c2 != c3) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_LOCATION);
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        MapsInitializer.initialize(this);
        googleMap.moveCamera(cameraUpdate);
    }
}
