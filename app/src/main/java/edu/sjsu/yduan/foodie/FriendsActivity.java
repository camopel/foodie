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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;

public class FriendsActivity extends AppCompatActivity implements OnMapReadyCallback {
    ListView mListView;
    GoogleMap googleMap;
    LatLngBounds.Builder builder;
    final int MY_PERMISSION_LOCATION = 10;
    ArrayList<Friend> friendList;
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
                Intent detailIntent = new Intent(context, FriendDetailActivity.class);
                detailIntent.putExtra("pos",position);
                startActivity(detailIntent);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap gMap) {
        this.googleMap = gMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        builder = new LatLngBounds.Builder();
        Random r = new Random();
        int i=0;
        for (Friend f : friendList) {
            i++;
            double offset_lat = r.nextDouble()*0.04-0.02+lat;
            double offset_lng = r.nextDouble()*0.04-0.02+lng;
            LatLng ll = new LatLng(offset_lat,offset_lng);
            builder.include(ll);
            Bitmap bmp = Bitmap.createBitmap(60, 80, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            Paint color = new Paint();
            color.setTextSize(46);
            color.setColor(Color.WHITE);
            color.setStrokeWidth(2);
            color.setAntiAlias(true);
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker), 0,0, color);
            canvas.drawText(String.valueOf(i), 16, 50, color);
            Marker m = googleMap.addMarker(new MarkerOptions()
                    .position(ll)
                    .title(f.name)
                    .snippet(f.Preference)
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        }
        LatLngBounds bounds = builder.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        showMyLocation();
    }

    private void showMyLocation(){
        int c1 = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int c2 = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int c3 = PackageManager.PERMISSION_GRANTED;
        if (c1 == c3 && c2 == c3) {
            googleMap.setMyLocationEnabled(true);
        }
    }
}
