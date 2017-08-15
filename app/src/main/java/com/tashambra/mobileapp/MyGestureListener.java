package com.tashambra.mobileapp;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by saryal on 8/13/17.
 */

public class MyGestureListener implements GestureDetector.OnGestureListener{

    private Context context;

    public MyGestureListener(Context context) {
        this.context = context;
    }
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {

        if(e1.getY() - e2.getY() > 50){
            Toast.makeText(context, "UP", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
