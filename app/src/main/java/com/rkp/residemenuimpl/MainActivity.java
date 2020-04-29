package com.rkp.residemenuimpl;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.TouchDisableView;

import static android.view.View.inflate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    private static final String TAG = "MainActivity";
    CardView cvInner, cvDashboard;
    AppCompatImageView iv_icon;
    int x1, x2, y1, y2, dx, dy;
    Activity activity;
    float startX, deltaX, previousDeltaX = -1080, initialDeltaX;
    float previousRatationValue, mPrevipousScaleValue;
    String direction;
    Context mContext;
    private ResideMenu resideMenu;
    private RelativeLayout.LayoutParams layoutParams;
    private FrameLayout.LayoutParams fragParams;
    private View mSideMenuView = null;
    private float mScaleValue = 0.41f;
    private float mScaleValueY = 0.41f;
    private float mScaleValueYprevious;
    private int rotationDirection = 1;/*2 for right to left and 1 for left to right*/
    private TouchDisableView viewActivity;
    private ObjectAnimator lftToRgt, rgtToLft;
    private float halfW;
    private AnimatorSet animatorSet;
    private int percentage;
    private float lastActionDownX, lastActionDownY;
    private boolean isFingureUp;
    private boolean moreThanOneSwipe, rightSwipeMoreThanOne;
    private DisplayMetrics displayMetrics = new DisplayMetrics();


    private ResideMenu.OnMenuDublicateListener menuDublicateListener = new ResideMenu.OnMenuDublicateListener() {
        @Override
        public void openDublicateMenu() {

        }

        @Override
        public void closeDublicateMenu() {

        }
    };


    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {

        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }
    };


    private boolean isOpened;
    private int screenWidth;
    private boolean first, second, third, fourth, fifth, six, seven, eight;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initViews();


        cvDashboard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float X = event.getRawX();
                final float Y = event.getRawY();

                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):
                        lastActionDownX = event.getRawX();
                        lastActionDownY = event.getRawY();
                        startX = event.getRawX();
                        x1 = (int) event.getX();
                        y1 = (int) event.getY();
                        Log.d(TAG, "ACTION_DOWN: " + x1 + " ,  " + y1);
                        Log.d(TAG, "ACTION_DOWN: getX " + startX);
                        break;

                    case (MotionEvent.ACTION_MOVE):
                        Log.d(TAG, "ACTION_MOVE: " + x1 + " ,  " + y1);

                        // int xOffset = (int) (event.getX() - lastActionDownX);
                        int xOffset = (int) (event.getRawX() - lastActionDownX);
                        int yOffset = (int) (event.getY() - lastActionDownY);

                        float moving = -(event.getRawX() - startX);
                        deltaX = moving - (previousDeltaX);


                        //previousDeltaX=deltaX;

                        Log.d(TAG, "ACTION_MOVE: deltaX " + deltaX);
                        Log.d(TAG, "ACTION_MOVE: moving " + moving);
                        Log.d(TAG, "ACTION_MOVE: offsetX " + xOffset);
                        Log.d(TAG, "ACTION_MOVE: lastActionDownX " + lastActionDownX);
                        Log.d(TAG, "ACTION_MOVE: event.getX() " + event.getRawX());
                        Log.d(TAG, "ACTION_MOVE: isFingureUp " + isFingureUp);

                        if (!isFingureUp) {
                            //for scaleX
                            Double d = new Double(((0.45 -/*previousRatationValue*/.7) / (getScreenWidth()) * (moving)) + .7);
                            // for scaleY
                            Double y1 = new Double((0.76 - 1) / getScreenHeight()) * moving + 0.7;
                            // Double d = new Double(((0.45-previousRatationValue)/(1000)*(updatedMovingValue)+0.45));
                            previousRatationValue = d.floatValue();
                            mScaleValue = d.floatValue();
                            mScaleValueY = y1.floatValue();
                            mScaleValueYprevious = mScaleValueY;
                            Log.d(TAG, "mScaleValue : " + mScaleValue);
                            mPrevipousScaleValue = mScaleValue;
                            Log.d(TAG, "ACTION_MOVE: mPrevipousScaleValue : " + mPrevipousScaleValue);


                        } else {

                            if (xOffset < 0) {
                                // for right to left
                                rotationDirection = 1;
                                moreThanOneSwipe = true;
                                Double d = new Double(((0.45 -/*previousRatationValue*/.7) / (getScreenWidth()) * (moving)) + previousRatationValue);
                                mScaleValue = d.floatValue();
                                if (mScaleValue < 0.42) {
                                    mScaleValue = 0.42f;
                                }

                                // for scaleY
                                Double y1 = new Double((0.76 - 1) / getScreenHeight()) * moving + mScaleValueYprevious;
                                mScaleValueY = y1.floatValue();
                                if(mScaleValueY<0.55){
                                    mScaleValueY=0.55f;
                                }


                                Log.e(TAG, "ACTION_MOVE: mScaleValueY" + mScaleValueY);


                            } else {
                                // for left to right
                                rotationDirection = 2;
                                rightSwipeMoreThanOne = true;
                                Double dprivious = new Double(((0.45 -/*previousRatationValue*/.7) / (getScreenWidth()) * (moving)) + previousRatationValue);
                                mScaleValue = dprivious.floatValue();
                               /* if(mScaleValue>0.77){
                                    mScaleValue=0.77f;
                                }*/


                                // for scaleY
                                Double y1 = new Double((0.76 - 1) / getScreenHeight()) * moving + mScaleValueYprevious;
                                mScaleValueY = y1.floatValue();
                               /* if(mScaleValueY>0.88){
                                    mScaleValueY=0.88f;
                                }*/


                            }


                        }


                        resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, /*deltaX*//* moving*/mScaleValue, xOffset, (int) lastActionDownX, x1 / screenWidth, false, isFingureUp, mScaleValueY);
                        //  resideMenu.closeDublicateMenu(ResideMenu.DIRECTION_RIGHT,x1,xOffset,false);
                        // resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);


                        break;


                    case (MotionEvent.ACTION_UP): {
                        isFingureUp = true;

                        x2 = (int) event.getX();
                        y2 = (int) event.getY();
                        dx = x2 - x1;
                        dy = y2 - y1;
                        int xOfffset = (int) (event.getX() - lastActionDownX);
                        Log.d(TAG, "ACTION_UP: " + x1 + " ,  " + y1);
                        Log.d(TAG, "ACTION_UP:  event.getX()  " + event.getX());
                        Log.d(TAG, "ACTION_UP:  lastActionDownX  " + lastActionDownX);
                        Log.d(TAG, "ACTION_UP:  isFingureUp  " + isFingureUp);
                        float deltaX = -(event.getRawX() - startX);
                        if (rotationDirection == 1) {
                            Double d = new Double(((0.45 -/*previousRatationValue*/.7) / (getScreenWidth()) * (deltaX)) + previousRatationValue);
                            if (moreThanOneSwipe) {
                                previousRatationValue = d.floatValue();
                                if (previousRatationValue < 0.42f) {
                                    previousRatationValue = 0.42f;
                                }
                                Log.d(TAG, "ACTION_UP previousRatationValue :" + previousRatationValue);
                            }
                            // for ScaleY
                            Double y1 = new Double((0.76 - 1) / getScreenHeight()) * deltaX + mScaleValueYprevious;

                            mScaleValueYprevious = y1.floatValue();
                            if (mScaleValueYprevious < 0.55) {
                                mScaleValueYprevious = 0.55f;
                            }

                            moreThanOneSwipe = false;
                            Log.d(TAG, "ACTION_UP previousRatationValue :" + previousRatationValue);
                        } else {

                            // for scaleX
                            Double dprivious = new Double(((0.45 - .7) / (getScreenWidth()) * (deltaX)) + previousRatationValue);
                            if (rightSwipeMoreThanOne) {

                                previousRatationValue = dprivious.floatValue();
                                /*if(previousRatationValue>0.77){
                                    previousRatationValue=0.77f;
                                }*/
                            }
                            rightSwipeMoreThanOne = false;

                            // for ScaleY
                            Double y1 = new Double((0.76 - 1) / getScreenHeight()) * deltaX + mScaleValueYprevious;
                            mScaleValueYprevious = y1.floatValue();
                            /*if(mScaleValueYprevious>0.8){
                                mScaleValueYprevious=0.8f;
                            }*/


                        }
                        //resideMenu.setScaleDirectionByRawX(event.getRawX());
                        //  resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, /*(int) event.getX()*/deltaX, xOfffset, (int)lastActionDownX,x1 / screenWidth, false,false);


                    }
                }
                return false;
            }

        });
    }

    private void initViews() {
        animatorSet = new AnimatorSet();
        activity = MainActivity.this;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        Log.d(TAG, screenWidth + "    screen");
        Log.d(TAG, "screenWidth" + getScreenWidth() + "");
        Log.d(TAG, "ScreenHeight" + getScreenHeight());


        mContext = MainActivity.this;
        cvInner = findViewById(R.id.cv_inner);
        cvDashboard = findViewById(R.id.cv_dashboard);
        iv_icon = findViewById(R.id.iv_icon);

        iv_icon.setOnClickListener(this);

        layoutParams = (RelativeLayout.LayoutParams) cvInner.getLayoutParams();
        fragParams = (FrameLayout.LayoutParams) cvDashboard.getLayoutParams();
        resideMenu = new ResideMenu(this, cvDashboard, cvInner);
        resideMenu = new ResideMenu(this, cvDashboard, cvInner);
        resideMenu.attachToActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMenu();
    }

    private void setUpMenu() {
        resideMenu.setMenuDublicateListener(menuDublicateListener);
        // resideMenu.setMenuListener(menuListener);
        mSideMenuView = inflate(this, R.layout.layout_side_menu, null);

        resideMenu.setUse3D(true);
        resideMenu.addMenuItem(mSideMenuView, ResideMenu.DIRECTION_RIGHT);
        resideMenu.setShadowVisible(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_icon:
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                break;

        }
    }


  /*  @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }*/


    private void changeVariableValue() {
        if (!first) {
            mScaleValue = 1.0f;
            // resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);
            first = true;
        } else if (!second) {
            mScaleValue = 0.9542f;
            // resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

            second = true;
        } else if (!third) {
            mScaleValue = 0.7932f;
            //resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

            third = true;
        } else if (!fourth) {
            mScaleValue = 0.565f;
            //resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

            fourth = true;
        } else if (!fifth) {
            mScaleValue = 0.52631f;
            // resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

            fifth = true;
        } else if (!six) {
            mScaleValue = 0.49625498f;
            //resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

            six = true;
        } else if (!seven) {
            mScaleValue = 0.48369092f;
            /// resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

            seven = true;
        } else if (!eight) {
            mScaleValue = 0.46452343f;
            // resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

            eight = true;
        } else {
            mScaleValue = 0.41f;
            // resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

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


}
