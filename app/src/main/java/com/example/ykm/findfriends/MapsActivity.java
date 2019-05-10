package com.example.ykm.findfriends;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ykm.findfriends.BackgroundService.MyService;
import com.example.ykm.find.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static int FIND = 0;
    private static int SESSION = 0;
    long back_pressed = 0;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    EditText searchText;
    Button searchButton, logoutButton, friendsButton;
    Button mPhoneButton, stopUpdate;
    private int locationUpdatesStatus = 0;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private double mLatitude, mLongitude;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private int flag = 0;
    private Marker mCurrLocationMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.locate);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.friends) {
                    Intent intent = new Intent(MapsActivity.this, FriendsListActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    Log.d(TAG, "onTabSelected: Friends!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                } else if (tabId == R.id.locate) {
                } else if (tabId == R.id.request) {
                    Intent intent = new Intent(MapsActivity.this, RequestAcitivtyTabs.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else if (tabId == R.id.profile) {
                    Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }
            }
        });
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.friends) {
                } else if (tabId == R.id.locate) {

                } else if (tabId == R.id.profile) {

                }
            }
        });

        Toast.makeText(MapsActivity.this, "FIND:" + FIND, Toast.LENGTH_SHORT).show();

        stopService(new Intent(this, MyService.class));
        Log.d(TAG, "onCreate ------------service stopped. ");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);//1 sec
        mLocationRequest.setFastestInterval(2000);// 2 sec
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        //Log.d(TAG, "onCreate: "+myRef.child());
        searchText = findViewById(R.id.SearchEditText);
        searchButton = findViewById(R.id.searchButton);
        logoutButton = findViewById(R.id.LogoutButton);
        //friendsButton = findViewById(R.id.friendsButton);

        // mPhoneButton = findViewById(R.id.Phone);
        // stopUpdate = findViewById(R.id.stopUpdate);

        /*mPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MapsActivity.this,PhoneActivity.class);
                startActivity(intent);
            }
        });
*/
/*
        stopUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLocationUpdates();
            }
        });
*/

/*
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SESSION = 1;
               // if(locationUpdatesStatus==0) {
                    stopLocationUpdates();
                 //   locationUpdatesStatus =1;
                //}
                Intent intent = new Intent(MapsActivity.this,FriendsListActivity.class);
                startActivity(intent);
            }
        });
*/

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(intent);
                SESSION = 1;
                finish();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = searchText.getText().toString();
                if (name.equals("")) {
                    Log.d(TAG, "Please enter a name");
                    Toast.makeText(MapsActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    find(name);
                }
            }
        });


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Log.d(TAG, "-----------------Inside onMapReady()");
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
/*
        if (FIND==1){
            Toast.makeText(MapsActivity.this,"if Find==1",Toast.LENGTH_SHORT).show();
            Intent intent = getIntent();
            String name = intent.getStringExtra("NameFromAdapterClass");
            find(name);
        }*/

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    Toast.makeText(this, "Location Permission Necessary !", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    void getCurrentLocation() {
        Log.d(TAG, "------------Inside getCurrentLocation() ");
        // mFirebaseDatabase = FirebaseDatabase.getInstance();
        // mAuth = FirebaseAuth.getInstance();
        String UserId = mAuth.getCurrentUser().getUid().toString();
        myRef = mFirebaseDatabase.getReference().child("users").child(UserId);
        Log.d(TAG, "-------->>>l: " + UserId);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            if (mLatitude == 0.0 && mLatitude == 0.0) {
                                mLatitude = location.getLatitude();
                                mLongitude = location.getLongitude();
                            }
                            Log.d(TAG, "----------------------set latitude : " + mLatitude);
                            Log.d(TAG, "----------------------set longitude: " + mLongitude);
                            myRef.child("latitude").setValue(mLatitude);
                            myRef.child("longitude").setValue(mLongitude);
                            locate("Your Location");

                        }
                    }
                });
    }

    void locate(final String name) {
        LatLng sydney = new LatLng(mLatitude, mLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
        mMap.addMarker(new MarkerOptions().position(sydney).title(name).flat(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        Log.d(TAG, "locate function end: ");

    }

    void find(final String name) {
        flag = 0;

        Log.d(TAG, "Finding " + name);
        Toast.makeText(MapsActivity.this, "Finding", Toast.LENGTH_SHORT).show();


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "-------------------onDataChange: ");
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                Log.d(TAG, "-------------------onDataSnapshot: ");
                for (DataSnapshot child : children) {

                    Log.d(TAG, "-----for-------------------------->: " + child.child("name").getValue());
                    Log.d(TAG, "-----for-------------------------->: " + child.child("latitude").getValue());
                    Log.d(TAG, "-----for-------------------------->: " + child.child("longitude").getValue());

                    if (child.child("name").getValue().equals(name)) {

                        Log.d(TAG, "------if------------------------->: " + child.child("name").getValue());
                        Log.d(TAG, "------if------------------------->: " + child.child("latitude").getValue());
                        Log.d(TAG, "------if------------------------->: " + child.child("longitude").getValue());

                        Toast.makeText(MapsActivity.this, "User Found", Toast.LENGTH_SHORT).show();
                        String latitude = child.child("latitude").getValue().toString();
                        String longitude = child.child("longitude").getValue().toString();

                        flag = 10;
                        if (latitude.equals("0") && longitude.equals("0")) {
                            Toast.makeText(MapsActivity.this, name + " location not found", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "------------2222222222222---------------: ");
                            mLatitude = (double) child.child("latitude").getValue();
                            mLongitude = (double) child.child("longitude").getValue();
                            locate(child.child("name").getValue() + "");
                            Log.d(TAG, "-------------------------333333333333---------: ");

                        }
                        break;
                    }
                }

                if (flag == 0) {
                    Log.d(TAG, "User not found");
                    Toast.makeText(MapsActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        FIND = 0;
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        if (SESSION == 0) {
            startService(new Intent(this, MyService.class));
            Log.d(TAG, "-------------Start LocationUpdates Service ");
        } else {
            Log.d(TAG, "-------------No Service initiated");
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        stopService(new Intent(this, MyService.class));
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()) {

            FIND = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        if (FIND == 0) {
            Toast.makeText(MapsActivity.this, "Connected and Requesting", Toast.LENGTH_SHORT).show();
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } else {
            Toast.makeText(MapsActivity.this, "Connected and Stopping", Toast.LENGTH_SHORT).show();
            stopLocationUpdates();
            Toast.makeText(MapsActivity.this, "Now searching", Toast.LENGTH_SHORT).show();
            Intent intent = getIntent();
            String name = intent.getStringExtra("NameFromAdapterClass");
            find(name);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(MapsActivity.this,
                "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        String UserId = mAuth.getCurrentUser().getUid().toString();
        myRef = mFirebaseDatabase.getReference().child("users").child(UserId);


        if (location != null) {
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            Log.d(TAG, "------onLocationChanged----updates--------latitude: " + mLatitude);
            Log.d(TAG, "------onLocationChanged----updates--------longitude: " + mLongitude);

            myRef.child("latitude").setValue(mLatitude);
            myRef.child("longitude").setValue(mLongitude);

            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            LatLng latLng = new LatLng(mLatitude, mLongitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position").flat(true);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        }
    }

    private void stopLocationUpdates() {
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d(TAG, "stoped Location Updates:");
        } else {
            Log.d(TAG, "Location Updates Off, no need to stop");//no need to stop updates - we are no longer connected to location service anyway
        }

    }

}
