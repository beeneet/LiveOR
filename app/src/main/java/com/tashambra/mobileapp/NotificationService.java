package com.tashambra.mobileapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.jjoe64.graphview.series.DataPoint;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class NotificationService extends Service {

    public DBHelper mDbHelper;
    private Runnable mTimer2;
    private final Handler mHandler = new Handler();
    public List<Drink> mDrinks = new ArrayList<Drink>();
    private double mBAC = 0;
    int notifShowed = 0;
    int firstRun = 1;
    int aboveState = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private double getBAC(){
        double totalBAC = 0.0;
        for (int i =0; i < mDrinks.size(); i++) {
            totalBAC += mDrinks.get(i).GetAlcoholContent();
            Log.i("CURRBAC", String.valueOf(totalBAC));
        }
        return totalBAC;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("DEI-SERVICE", "service started");

        mDbHelper = new DBHelper(getApplicationContext());
        mDrinks = GraphActivity.readData(mDbHelper);


        mTimer2 = new Runnable() {
            @Override
            public void run() {
                Log.i("DEI-SERVICE2", "service");
                double BAC = mBAC;
                BAC = getBAC();
                Log.i("DEI", "BAC2: " + Double.toString(mBAC));

                if ((BAC > 0.08)){
                    aboveState = 1;
                }
                else if ((aboveState == 1) && (BAC < 0.08) && (notifShowed != 1)) {
                    showNotification();
                    notifShowed = 1;
                    aboveState = 0;
                }
                mBAC = BAC;
                Log.i("DEI", "BAC: " + Double.toString(mBAC));

                mHandler.postDelayed(this, 3000);
            }
        };
        mHandler.postDelayed(mTimer2, 0);


        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("DEI-SERVICE", "service stopped");
    }



    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Notification n  = new NotificationCompat.Builder(this)
                .setContentTitle("LiveOR")
                .setContentText("Your Blood Alcohol Level is now below the legal limit (0.08)")
                .setSmallIcon(android.R.drawable.btn_dropdown)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, n);
    }

}

