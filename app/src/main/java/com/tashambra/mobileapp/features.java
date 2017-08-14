package com.tashambra.mobileapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lyft.lyftbutton.LyftButton;
import com.lyft.lyftbutton.RideParams;
import com.lyft.lyftbutton.RideTypeEnum;
import com.lyft.networking.ApiConfig;
import com.uber.sdk.android.core.UberButton;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestActivityBehavior;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.error.ApiError;

import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class features extends AppCompatActivity {

    private Button mBack;
    //private Button mReset;
    private LinearLayout mFeaturesLayout;
    private Button mEmergencyCall;
    private RideRequestButton mUberButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);
//        mReset = (Button) findViewById(R.id.reset_params);
//        mReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fm = getSupportFragmentManager();
//                Fragment existingFragment = fm.findFragmentById(R.id.container);
//                if (existingFragment == null) {
//                    Fragment info = new FirstTimeFragment();
//                    fm.beginTransaction().replace(R.id.container, info).commit();
//                }
//                else{
//                    Log.i("SHR","Not what I wanted");
//                }
//            }
//        });
        mBack = (Button) findViewById(R.id.back_button);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(features.this);
        final String emergency_phone = prefs.getString("EmergencyContact", "2027701632");
        final String emergency_address = prefs.getString("EmergencyAddress", "301 W Washington Ave NW");

        mFeaturesLayout = (LinearLayout) findViewById(R.id.featuresLayout);
        Geocoder gc = new Geocoder(this);
        double latitude = 0;
        double longitude = 0;
        try {
            List<Address> list = gc.getFromLocationName(emergency_address, 1);
            Address address = list.get(0);//getting first address from the list
            longitude = address.getLongitude();
            latitude = address.getLatitude();

        } catch (IOException e) {
            Toast.makeText(features.this, "Address Invalid", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        Toast.makeText(features.this, "Latitude is: "+latitude,Toast.LENGTH_LONG).show();
//        Toast.makeText(features.this, "Longitude is: "+longitude,Toast.LENGTH_LONG).show();
        SessionConfiguration config = new SessionConfiguration.Builder()
                .setClientId("N6mrT9M77XKWdkD0bZvsJYKIvR9MeG0z") //This is necessary
                .setRedirectUri("google.com") //This is necessary if you'll be using implicit grant
                .setEnvironment(SessionConfiguration.Environment.SANDBOX) //Useful for testing your app in the sandbox environment
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.RIDE_WIDGETS)) //Your scopes for authentication here
                .build();

        UberSdk.initialize(config);

        mUberButton = (RideRequestButton) findViewById(R.id.uber_button);

        RideParameters rideParams = new RideParameters.Builder()
                .setDropoffLocation(latitude, longitude, "dropoff","address") // Price estimate will only be provided if this is provided.
                .build();
        mUberButton.setRideParameters(rideParams);


        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("ShZbQPSTtb2r")
                .setClientToken("")
                .build();
        LyftButton lyftbutton = (LyftButton) findViewById(R.id.lyft_button);
        lyftbutton.setApiConfig(apiConfig);
        RideParams.Builder rideParamsBuilder = new RideParams.Builder()
                .setDropoffLocation(latitude, longitude);
        rideParamsBuilder.setRideTypeEnum(RideTypeEnum.CLASSIC);
        lyftbutton.setRideParams(rideParamsBuilder.build());
        lyftbutton.load();

        //upon clicking the emergency button, initiate an intent which opens phone app with number
        mEmergencyCall = (Button) findViewById(R.id.emergency_call);
        mEmergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phonecard = new Intent(Intent.ACTION_DIAL);
                String phone_number = "tel:" + emergency_phone;
                phonecard.setData(Uri.parse(phone_number));
                startActivity(phonecard);
            }
        });


    }










}
