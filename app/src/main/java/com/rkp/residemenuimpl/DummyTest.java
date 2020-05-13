package com.rkp.residemenuimpl;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import com.google.android.material.appbar.AppBarLayout;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.TouchDisableView;

import static android.view.View.inflate;

public class DummyTest extends AppCompatActivity implements View.OnClickListener {
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    private static final String TAG = "MainActivity";
    private static final float SWIPE_THRESHOLD = 100f;
    private static final float SWIPE_VELOCITY_THRESHOLD = 100;
    CardView cvInner, cvDashboard;
    FrameLayout fl_image;
    int x1, x2, y1, y2, dx, dy;
    Activity activity;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    TextView tv1,tv2;
    FrameLayout fl_image_home;

    private boolean isOpened;
    private int screenWidth;

    Rect outRect = new Rect();
    int[] location = new int[2];

    private boolean isViewInBounds(View view, int x, int y){
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dummy);
        initViews();

    }

/*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);

    }*/


    private void initViews() {
        activity = DummyTest.this;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);
        fl_image_home=findViewById(R.id.fl_image_home);

        fl_image_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fl_image_home.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "onTouch ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "onTouch ACTION_MOVE");

                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "onTouch ACTION_UP");


                            //Here goes code to execute on onTouch ViewA

                        break;


                }




                return false;
            }
        });


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });

        tv1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int)event.getRawX();
                int y = (int)event.getRawY();
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "onTouch ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "onTouch ACTION_MOVE");

                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "onTouch ACTION_UP");


                        if(isViewInBounds(tv2, x, y)){
                           // tv2.dispatchTouchEvent(event);
                        }

                        else if(isViewInBounds(tv1, x, y)){
                            Log.d(TAG, "onTouch ViewA");
                            //Here goes code to execute on onTouch ViewA
                        }
                        break;


                }

                // Further touch is not handled
                return false;
            }
        });




    }


    @Override
    protected void onResume() {
        super.onResume();
       // setUpMenu();
    }

    private void setUpMenu() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_image_home:
                Log.d("CLICK", "click");
               /* if(isOpened){
                    resideMenu.closeMenu();
                    isOpened=false;
                }else{
                    resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                    setPreviousStateData();
                }*/

                break;

        }
    }




    public int getScreenHeight() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public int getScreenWidth() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private void onSwipRight() {
        Log.d("AAAA", "Right Swipe");
       // flingState = 2;
    }

    private void onSwipeLeft() {
        Log.d("AAAA", "Left Swipe");
       // flingState = 1;

    }

    private class GestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent downEvent, MotionEvent upEvent, float velocityX, float velocityY) {
            boolean result = false;
            float diffY = upEvent.getY() - downEvent.getY();
            float diffX = upEvent.getRawX() - downEvent.getRawX();
            // which was graeter moovement across X or Y ?
            if (Math.abs(diffX) > Math.abs(diffY)) {
                //  right or left swipe
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipRight();
                    } else {
                        onSwipeLeft();
                    }
                }

            } else {
                // top or bottom swipe
            }
            return result;
        }
    }


}
