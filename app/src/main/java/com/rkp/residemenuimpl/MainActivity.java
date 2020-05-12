package com.rkp.residemenuimpl;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.appbar.AppBarLayout;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.TouchDisableView;

import static android.view.View.inflate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener /*, GestureDetector.OnGestureListener*/ {
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    private static final String TAG = "MainActivity";
    private static final float SWIPE_THRESHOLD = 100f;
    private static final float SWIPE_VELOCITY_THRESHOLD = 100;
    CardView cvInner, cvDashboard;
    FrameLayout fl_image;
    int x1, x2, y1, y2, dx, dy;
    Activity activity;
    float startX, deltaX, previousDeltaX = -1080, initialDeltaX;
    float mPreviousScaleX, mPrevipousScaleValue;
    String direction;
    Context mContext;
    float mRotationY, mPreviousRotationY;
    float mXNegative, mYNegative, openedDoorPreviousRotationY;
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
    private float alphaAnimation, previousAlphaAnimation;
    private boolean isFingureUp;
    private boolean moreThanOneSwipe, rightSwipeMoreThanOne;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private AppBarLayout appBar;
    private float mOuterRadiusValue, outerRadiusPreviousValue, mInerRadius, inerRadiusPreviousValue;
    private float transitionValue, previousTransitionValue;
    private GestureDetector gestureDetector;
    private int flingState;   /// 1 for open 2 for close
    private boolean doorOpenFromProfilClick;
    private boolean isOpenThenLeftSwipe;
    private boolean isClickAction = true;
    private RelativeLayout rl_include;


    private ResideMenu.OnMenuDublicateListener menuDublicateListener = new ResideMenu.OnMenuDublicateListener() {
        @Override
        public void openDublicateMenu() {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (appBar != null) {
                    appBar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
        }

        @Override
        public void closeDublicateMenu() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (appBar != null) {
                    appBar.setSystemUiVisibility(0);
                }
            }
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
                gestureDetector.onTouchEvent(event);
                float X = event.getRawX();
                final float Y = event.getRawY();

                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):
                        Log.d("AAAA 0","ACTION_DOWN");

                        lastActionDownX = event.getRawX();
                        lastActionDownY = event.getRawY();
                        isFingureUp=false;
                        startX = event.getRawX();
                        x1 = (int) event.getX();
                        y1 = (int) event.getY();
                        Log.d(TAG, "ACTION_DOWN: getX " + startX);
                        break;

                    case (MotionEvent.ACTION_MOVE):
                        Log.d("AAAA 1","ACTION_MOVE");

                        Log.d(TAG, "ACTION_MOVE: getScreenWidth" + getScreenWidth());
                        Log.d(TAG, "ACTION_MOVE: getScreenHeight" + getScreenHeight());

                        // int xOffset = (int) (event.getX() - lastActionDownX);
                        int xOffset = (int) (event.getRawX() - lastActionDownX);
                        int yOffset = (int) (event.getY() - lastActionDownY);

                        float moving = -(event.getRawX() - startX);
                        deltaX = moving - (previousDeltaX);


                        //previousDeltaX=deltaX;

                        //Log.d(TAG, "ACTION_MOVE: deltaX " + deltaX);
                        Log.d(TAG, "ACTION_MOVE: moving " + moving);
                        Log.d(TAG, "ACTION_MOVE: offsetX " + xOffset);
                        Log.d(TAG, "ACTION_MOVE: lastActionDownX " + lastActionDownX);
                        Log.d(TAG, "ACTION_MOVE: event.getX() " + event.getRawX());
                        Log.d(TAG, "ACTION_MOVE: isFingureUp " + isFingureUp);

                        if(isOpened){

                            /*
                             * this block for closing door with swipe
                             * */

                            if(xOffset>0) {

                                /*
                                * this block is to  close the opened door towards Right only
                                * */


                                isOpenThenLeftSwipe=true;

                                if (doorOpenFromProfilClick) {

                                    /*
                                    * this block for closing door when door is open  from profile click
                                    * */

                                    // for ScaleX

                                    Double d2 = new Double(((0.41 - 1) / (getScreenWidth()) * (moving)) + 0.41);

                                   // mScaleValue=d2.floatValue();

                                    if(d2.floatValue()<0.41){
                                        mScaleValue=0.41f;
                                    }else {
                                        mScaleValue=d2.floatValue();
                                    }
                                    Log.d("TAG","dOpenFromClick mScaleValue :"+mScaleValue);

                                    // for ScaleY
                                    Double yd1 = (((0.656 - 1) / getScreenWidth()) * moving) + 0.656;
                                        //mScaleValueY=yd1.floatValue();

                                        if(yd1<0.656){
                                            mScaleValueY=0.656f;
                                        }else {
                                            mScaleValueY=yd1.floatValue();
                                        }
                                    Log.d("TAG","dOpenFromClick mScaleValueY :"+mScaleValueY);

                                    Double d12 = ((-10 - 0.0) / (getScreenWidth()) * (moving)) + (-10);

                                        if(d12<-10){
                                            mRotationY=-10;
                                        }else { mRotationY=d12.floatValue();}
                                    Log.d("TAG","dOpenFromClick mRotationY :"+d12.floatValue());
                                  //  Log.d("TAG","PROFILE :"+d12.floatValue());

                                    // for outerRadius
                                    Double outerRadius = ((35 - 0.45) / (getScreenWidth()) * (moving)) + 35;

                                        if(outerRadius>35){
                                            mOuterRadiusValue=35;
                                        }else mOuterRadiusValue=outerRadius.floatValue();
                                    Log.d("TAG","dOpenFromClick mOuterRadiusValue :"+mOuterRadiusValue);

                                    /*for inner Radious*/
                                    Double inerRadius = ((28 - 0.45) / (getScreenWidth()) * (moving)) + 28;

                                    if(inerRadius>28){
                                        mInerRadius=28;
                                    }else mInerRadius=inerRadius.floatValue();
                                    Log.d("TAG","dOpenFromClick mInerRadius :"+mInerRadius);


                                    // for rotationY
                                    Double aa = (((0.0-600 ) / (getScreenWidth())) * (moving)) + 0;
                                    Log.d("TAG","dOpenFromClick mRotationY :"+aa.floatValue());
                                    if(aa<0){
                                        transitionValue=0;
                                    }else transitionValue=aa.floatValue();



                                } else {

                                    /*
                                    * this block is for closing the door when the same is open with swipe
                                    * */
                                // for mScaleX
                                Double d = new Double(((0.41 - 1) / (getScreenWidth()) * (moving)) + mPreviousScaleX);
                               // Double d2 = new Double(((0.41 - 1) / (getScreenWidth()) * (deltaX)) + 1);
                                mScaleValue = d.floatValue();
                                mXNegative = mScaleValue;

                                // Log.d(TAG,"SVX 2:"+mScaleValue);
                              //  Log.d(TAG, "SVX 2:" + d2.floatValue());
                                // for yAxis
                                // Double yd1 = new Double((0.76 - 1) / getScreenHeight()) * moving + 0.7;
                                //  Double yd1 = (((0.656 - 1/*1-0.76*/) / getScreenHeight()) * moving) + mScaleValueYprevious;
                                Double yd1 = (((0.41 - 1) / getScreenHeight()) * moving) + mScaleValueYprevious;
                                // Double yd1 = (((0.768-1)/(0.45-1)) * (mScaleValue)) + mScaleValueYprevious;
                                mScaleValueY = yd1.floatValue();
                                    mYNegative=mScaleValueY;
                                Log.d(TAG, "ewewewe 1 " + mScaleValueYprevious);
                                Log.d(TAG, "ewewewe 2 " + mScaleValueY);


                                Log.d(TAG, "mScaleValueY" + mScaleValueY);

                                // for outerRadious
                                //  Double outerRadius = ((/*35 - 0.45*/1-35) / (getScreenWidth()) * (moving)) + 0.45;
                                Double outerRadius = ((35 - 0.45) / (getScreenWidth()) * (moving)) + outerRadiusPreviousValue;
                                mOuterRadiusValue = outerRadius.floatValue();
                                Log.d(TAG, "Outer" + outerRadius);

                                // for innerRadious

                                Double inerRadius = ((28 - 0.45) / (getScreenWidth()) * (moving)) + inerRadiusPreviousValue;
                                mInerRadius = inerRadius.floatValue();
                                Log.d(TAG, "Inner" + outerRadius);

                                // for translation
                                // transitionValue  = (int)2400*mScaleValue-getScreenWidth();
                                Double aa = (((600 - 0.0) / (getScreenWidth())) * (moving)) + 0.0;
                                transitionValue = Math.abs(aa.floatValue());
                                //  Log.d("AZ",aa.floatValue()+"");
                                Log.d(TAG, "ACTION_MOVE: transitionValue : " + transitionValue);


                                //  Double rotatinY= ((0.0-10.0)/getScreenWidth())*moving;
                                Double rotatinY = ((-8 - 0.0) / (getScreenWidth()) * (moving)) + mPreviousRotationY;
                                mRotationY = rotatinY.floatValue();
                                    openedDoorPreviousRotationY=mRotationY;
                                Log.d(TAG, "mRotationY :" + rotatinY.floatValue());

                                // for alphaAnimation
                                Double alphaAnim = (((1 - 0.0) / getScreenWidth()) * moving) + previousAlphaAnimation;
                                alphaAnimation = alphaAnim.floatValue();
                                Log.d(TAG, "alphaAnim :" + alphaAnim.floatValue());
                            }

                            }else {
                                /*
                                * 1.to restrict the opened  door not to move too left.
                                * 2.opened door swipe movement from left>right>left
                                * */
                                if(isOpenThenLeftSwipe && !doorOpenFromProfilClick){
                                Double d = new Double(((0.41 -1) / (getScreenWidth()) * (moving)) + mXNegative);

                                    if(d.floatValue()>=0.41){
                                        mScaleValue=d.floatValue();
                                    }else mScaleValue=0.41f;
                                    // for ScaleY
                                    Double y1 = (((0.41 - 1) / (getScreenHeight())) * (moving)) + mYNegative;
                                    if(y1.floatValue()>=0.656){
                                        mScaleValueY=y1.floatValue();
                                    }else   mScaleValueY=0.656f;

                                    // for RotationY
                                    Double d12 = ((-8 - 0.0) / (getScreenWidth()) * (moving)) + openedDoorPreviousRotationY;

                                        if(d12.floatValue()>=-8){
                                            mRotationY=d12.floatValue();
                                        }else  mRotationY=-8;

                                    Log.d(TAG,"SVX : 7 "+d.floatValue());
                                    Log.d(TAG,"SVX : 8 "+y1.floatValue());

                                }
                            }

                        }else {
                            /*
                                When door is is closed
                             * this block for opening door with swipe left
                             * */

                                //for scaleX
                                Double d = ((0.41 - 1) / (getScreenWidth()) * (moving)) + 1;
                                // Double d = new Double(((0.45-mPreviousScaleX)/(1000)*(moving)+0.45));
                                mPreviousScaleX = d.floatValue();
                                mScaleValue = d.floatValue();
                                mPrevipousScaleValue = mScaleValue;
                                Log.d(TAG,"SVX 1:"+mScaleValue);
                                Log.d(TAG, "ACTION_MOVE : mScaleValue : " + mScaleValue);

                                // for scaleY
                                // Double y1 = (((0.656 - 1) / (getScreenHeight())) * (moving)) + 1;
                                // Double y1 = (((0.656 - 1) / (getScreenHeight())) * (moving)) + 1;
                                Double y1 = (((0.41 - 1) / (getScreenHeight())) * (moving)) + 1;
                                // Double y1 = (((0.768-1)/(0.45-1)) * (mScaleValue)) + 0.578181;
                                // mScaleValueY = y1.floatValue();
                                mScaleValueY=y1.floatValue();
                                if(y1.floatValue()>=0.656 || y1.floatValue()<=1.0f){
                                    //  mScaleValueY=y1.floatValue();
                                }
                                //  mScaleValueY=(float) (0.421*moving)+0.578f;
                                mScaleValueYprevious = mScaleValueY;
                                Log.d(TAG, "ACTION_MOVE: mPrevipousScaleValue : " + mScaleValueYprevious);

                                // for mUuterRadiusValue value
                                Double outerRadius = ((35 - 0.45) / (getScreenWidth()) * (moving)) + .45;
                                mOuterRadiusValue=outerRadius.floatValue();
                                outerRadiusPreviousValue=mOuterRadiusValue;
                                Log.d(TAG, "ACTION_MOVE: mOuterRadiusValue : " + mOuterRadiusValue);

                                // for inner radious
                                Double inerRadius = ((28 - 0.45) / (getScreenWidth()) * (moving)) + .45;
                                mInerRadius=inerRadius.floatValue();
                                inerRadiusPreviousValue=mInerRadius;
                                Log.d(TAG, "ACTION_MOVE: Inner : " + mInerRadius);

                                // for transition
                                //  transitionValue  = (int) (((600-0) / (/*getScreenWidth()/2*/542) * (moving)));
                                // transitionValue  = (int) (((600) / (getScreenWidth()) * (moving))+1);
                                // transitionValue  = (int)2400*mScaleValue-getScreenWidth();
                                Double aa=(((0.0-600)/(getScreenWidth()))*(moving))+600;
                                // previousTransitionValue=aa.floatValue();
                                transitionValue=aa.floatValue();
                                Log.d(TAG, "ACTION_MOVE: transitionValue : " + transitionValue);
                                //Log.d(TAG, "AZ: transitionValue : " + aa.floatValue());
                                Log.d(TAG, "ACTION_MOVE: moving : " + moving);

                                // for mRotationY
                                // mRotationY= ((0-10)/(float)getScreenWidth())*moving;
                                Double d12 = ((-8 - 0.0) / (getScreenWidth()) * (moving)) + 0.0;
                                mRotationY=d12.floatValue();
                                mPreviousRotationY=d12.floatValue();
                                Log.d(TAG, "ACTION_MOVE: mRotationY : " + mRotationY);

                                Double alphaAnim=(((1-0.0)/getScreenWidth())*moving)+0.0f;
                                alphaAnimation=alphaAnim.floatValue();
                                previousAlphaAnimation=alphaAnimation;
                                Log.d(TAG, "ACTION_MOVE: alphaAnimation : " + alphaAnimation);

                        }


                        if(mScaleValue<=1.0f && mScaleValue >=0.41){
                            Log.d("TAG",mScaleValue+"");
                            resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT,mScaleValue, xOffset, (int) lastActionDownX, x1 / screenWidth, false, isFingureUp, mScaleValueY,mOuterRadiusValue,mInerRadius,transitionValue,mRotationY, alphaAnimation);
                        }
                        break;


                    case (MotionEvent.ACTION_UP): {
                        isFingureUp = true;
                        doorOpenFromProfilClick=false;
                        isOpenThenLeftSwipe=false;
                        Log.d("AAAA 2","ACTION_UP");

                        x2 = (int) event.getX();
                        y2 = (int) event.getY();

                        Log.d(TAG,"mScaleValue"+mScaleValue);
                        Log.d(TAG,"mScaleValueY"+mScaleValueY);

                        dx = x2 - x1;
                        //  int xOfffset = (int) (event.getX() - lastActionDownX);
                        int xOfffset = (int) (event.getRawX() - lastActionDownX);

                        float deltaX = -(event.getRawX() - startX);
                        int directionOffset=xOfffset;


                        if(flingState==1  /* fling State 1 for open door and 2 for close door*/){
                            mScaleValue=0.41f;
                            mScaleValueY=0.656f;
                            transitionValue=0;

                            mRotationY=-10;
                            mInerRadius=28;
                            directionOffset=-xOfffset;
                            isOpened=true;
                            flingState=0;

                        }else if(flingState==2){
                            mScaleValue=1.0f;
                            mScaleValueY=1.0f;
                            transitionValue=transitionValue+(600-transitionValue);
                            mRotationY=0;
                            mInerRadius=0;
                            directionOffset=xOfffset;
                            isOpened=false;
                            flingState=0;
                        }else if(mScaleValue<0.57){

                            mScaleValue=0.41f;
                            mScaleValueY=0.656f;
                            transitionValue=0;
                            mRotationY=-10;
                            mInerRadius=28;
                            directionOffset=-xOfffset;
                            isOpened=true;


                        }else {
                            mScaleValue=1.0f;
                            mScaleValueY=1.0f;
                           // transitionValue=600;
                            transitionValue=transitionValue+(600-transitionValue);
                            mRotationY=0;
                            mInerRadius=0;
                            directionOffset=xOfffset;
                            isOpened=false;

                        }

                        Log.d(TAG,"ACTION_UP mScaleValue :"+mScaleValue);
                        Log.d(TAG,"ACTION_UP mScaleValueY :"+mScaleValueY);
                        Log.d(TAG,"ACTION_UP tra :"+transitionValue);
                        Log.d(TAG,"ACTION_UP isOpened :"+isOpened);
                        resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT,mScaleValue , directionOffset, (int) lastActionDownX, x1 / screenWidth, false, true, mScaleValueY,mOuterRadiusValue,mInerRadius,transitionValue,mRotationY,alphaAnimation);

                    }
                }
                return false;

            }

        });



    }

/*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);

    }*/


    private void initViews() {
        animatorSet = new AnimatorSet();
        activity = MainActivity.this;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        gestureDetector = new GestureDetector(new GestureListener());

        Log.d(TAG, screenWidth + "    screen");
        Log.d(TAG, "screenWidth" + getScreenWidth() + "");
        Log.d(TAG, "ScreenHeight" + getScreenHeight());


        mContext = MainActivity.this;
        cvInner = findViewById(R.id.cv_inner);
        cvDashboard = findViewById(R.id.cv_dashboard);
        fl_image = findViewById(R.id.fl_image_home);
        appBar = findViewById(R.id.app_bar);

        //fl_image.setOnClickListener(this);

        fl_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // gestureDetector.onTouchEvent(event);
                int action = event.getAction();
                switch (event.getAction() ) {

                    case MotionEvent.ACTION_DOWN:
                        Log.d("CLICK", "down");

                        lastActionDownX = event.getRawX();
                        lastActionDownY = event.getRawY();
                        isFingureUp = false;
                        isClickAction = true;
                        startX = event.getRawX();
                        x1 = (int) event.getX();
                        y1 = (int) event.getY();
                        Log.d(TAG, "ACTION_DOWN: getX " + startX);

                        break;
                    case (MotionEvent.ACTION_MOVE):
                        isClickAction = false;
                        Log.d("CLICK", "Move");

                        Log.d(TAG, "ACTION_MOVE: getScreenWidth" + getScreenWidth());
                        Log.d(TAG, "ACTION_MOVE: getScreenHeight" + getScreenHeight());

                        // int xOffset = (int) (event.getX() - lastActionDownX);
                        int xOffset = (int) (event.getRawX() - lastActionDownX);
                        int yOffset = (int) (event.getY() - lastActionDownY);

                        float moving = -(event.getRawX() - startX);
                        deltaX = moving - (previousDeltaX);


                        //previousDeltaX=deltaX;

                        //Log.d(TAG, "ACTION_MOVE: deltaX " + deltaX);
                        Log.d(TAG, "ACTION_MOVE: moving " + moving);
                        Log.d(TAG, "ACTION_MOVE: offsetX " + xOffset);
                        Log.d(TAG, "ACTION_MOVE: lastActionDownX " + lastActionDownX);
                        Log.d(TAG, "ACTION_MOVE: event.getX() " + event.getRawX());
                        Log.d(TAG, "ACTION_MOVE: isFingureUp " + isFingureUp);

                        if (isOpened) {

                            /*
                             * this block for closing door
                             * */

                            if (xOffset > 0) {
                                isOpenThenLeftSwipe = true;

                                if (doorOpenFromProfilClick) {

                                    /*
                                     * this block for closing door when open door from profile click
                                     * */

                                    // for ScaleX

                                    Double d2 = new Double(((0.41 - 1) / (getScreenWidth()) * (moving)) + 0.41);

                                    // mScaleValue=d2.floatValue();

                                    if (d2.floatValue() < 0.41) {
                                        mScaleValue = 0.41f;
                                    } else {
                                        mScaleValue = d2.floatValue();
                                    }
                                    Log.d("TAG", "dOpenFromClick mScaleValue :" + mScaleValue);

                                    Double yd1 = (((0.656 - 1) / getScreenWidth()) * moving) + 0.656;
                                    //mScaleValueY=yd1.floatValue();

                                    if (yd1 < 0.656) {
                                        mScaleValueY = 0.656f;
                                    } else {
                                        mScaleValueY = yd1.floatValue();
                                    }
                                    Log.d("TAG", "dOpenFromClick mScaleValueY :" + mScaleValueY);

                                    Double d12 = ((-10 - 0.0) / (getScreenWidth()) * (moving)) + (-10);

                                    if (d12 < -10) {
                                        mRotationY = -10;
                                    } else {
                                        mRotationY = d12.floatValue();
                                    }
                                    Log.d("TAG", "dOpenFromClick mRotationY :" + d12.floatValue());
                                    //  Log.d("TAG","PROFILE :"+d12.floatValue());

                                    Double outerRadius = ((35 - 0.45) / (getScreenWidth()) * (moving)) + 35;

                                    if (outerRadius > 35) {
                                        mOuterRadiusValue = 35;
                                    } else mOuterRadiusValue = outerRadius.floatValue();
                                    Log.d("TAG", "dOpenFromClick mOuterRadiusValue :" + mOuterRadiusValue);

                                    Double inerRadius = ((28 - 0.45) / (getScreenWidth()) * (moving)) + 28;

                                    if (inerRadius > 28) {
                                        mInerRadius = 28;
                                    } else mInerRadius = inerRadius.floatValue();
                                    Log.d("TAG", "dOpenFromClick mInerRadius :" + mInerRadius);


                                    // for rotationY
                                    Double aa = (((0.0 - 600) / (getScreenWidth())) * (moving)) + 0;
                                    Log.d("TAG", "dOpenFromClick mRotationY :" + aa.floatValue());
                                    if (aa < 0) {
                                        transitionValue = 0;
                                    } else transitionValue = aa.floatValue();


                                } else {


                                    // for mScaleX
                                    Double d = new Double(((0.41 - 1) / (getScreenWidth()) * (moving)) + mPreviousScaleX);
                                    // Double d2 = new Double(((0.41 - 1) / (getScreenWidth()) * (deltaX)) + 1);
                                    mScaleValue = d.floatValue();
                                    mXNegative = mScaleValue;
                               /* if(mScaleValue<0.42){
                                    mScaleValue=0.42f;
                                }*/
                                    // Log.d(TAG,"SVX 2:"+mScaleValue);
                                    //  Log.d(TAG, "SVX 2:" + d2.floatValue());
                                    // for yAxis
                                    // Double yd1 = new Double((0.76 - 1) / getScreenHeight()) * moving + 0.7;
                                    //  Double yd1 = (((0.656 - 1/*1-0.76*/) / getScreenHeight()) * moving) + mScaleValueYprevious;
                                    Double yd1 = (((0.41 - 1) / getScreenHeight()) * moving) + mScaleValueYprevious;
                                    // Double yd1 = (((0.768-1)/(0.45-1)) * (mScaleValue)) + mScaleValueYprevious;
                                    mScaleValueY = yd1.floatValue();
                                    mYNegative = mScaleValueY;
                                    Log.d(TAG, "ewewewe 1 " + mScaleValueYprevious);
                                    Log.d(TAG, "ewewewe 2 " + mScaleValueY);

                               /*  if(mScaleValueY<0.656){
                                    mScaleValueY=0.656f;
                                }*/

                                    Log.d(TAG, "mScaleValueY" + mScaleValueY);

                                    // for outerRadious
                                    //  Double outerRadius = ((/*35 - 0.45*/1-35) / (getScreenWidth()) * (moving)) + 0.45;
                                    Double outerRadius = ((35 - 0.45) / (getScreenWidth()) * (moving)) + outerRadiusPreviousValue;
                                    mOuterRadiusValue = outerRadius.floatValue();
                                    Log.d(TAG, "Outer" + outerRadius);

                                    // for innerRadious

                                    Double inerRadius = ((28 - 0.45) / (getScreenWidth()) * (moving)) + inerRadiusPreviousValue;
                                    mInerRadius = inerRadius.floatValue();
                                    Log.d(TAG, "Inner" + outerRadius);

                                    // for translation
                                    // transitionValue  = (int)2400*mScaleValue-getScreenWidth();
                                    Double aa = (((600 - 0.0) / (getScreenWidth())) * (moving)) + 0.0;
                                    transitionValue = Math.abs(aa.floatValue());
                                    //  Log.d("AZ",aa.floatValue()+"");
                                    Log.d(TAG, "ACTION_MOVE: transitionValue : " + transitionValue);


                                    //  Double rotatinY= ((0.0-10.0)/getScreenWidth())*moving;
                                    Double rotatinY = ((-8 - 0.0) / (getScreenWidth()) * (moving)) + mPreviousRotationY;
                                    mRotationY = rotatinY.floatValue();
                                    openedDoorPreviousRotationY = mRotationY;
                                    Log.d(TAG, "mRotationY :" + rotatinY.floatValue());

                                    // for alphaAnimation
                                    Double alphaAnim = (((1 - 0.0) / getScreenWidth()) * moving) + previousAlphaAnimation;
                                    alphaAnimation = alphaAnim.floatValue();
                                    Log.d(TAG, "alphaAnim :" + alphaAnim.floatValue());
                                }

                            } else {
                                /*
                                 * door open but swipe left
                                 * */
                                if (isOpenThenLeftSwipe && !doorOpenFromProfilClick) {
                                    Double d = new Double(((0.41 - 1) / (getScreenWidth()) * (moving)) + mXNegative);

                                    if (d.floatValue() >= 0.41) {
                                        mScaleValue = d.floatValue();
                                    } else mScaleValue = 0.41f;
                                    // for ScaleY
                                    Double y1 = (((0.41 - 1) / (getScreenHeight())) * (moving)) + mYNegative;
                                    if (y1.floatValue() >= 0.656) {
                                        mScaleValueY = y1.floatValue();
                                    } else mScaleValueY = 0.656f;

                                    // for RotationY
                                    Double d12 = ((-8 - 0.0) / (getScreenWidth()) * (moving)) + openedDoorPreviousRotationY;

                                    if (d12.floatValue() >= -8) {
                                        mRotationY = d12.floatValue();
                                    } else mRotationY = -8;

                                    Log.d(TAG, "SVX : 7 " + d.floatValue());
                                    Log.d(TAG, "SVX : 8 " + y1.floatValue());

                                }
                            }

                        } else {
                            /*
                             * this block for opening door
                             * */

                            //for scaleX
                            Double d = ((0.41 - 1) / (getScreenWidth()) * (moving)) + 1;
                            // Double d = new Double(((0.45-mPreviousScaleX)/(1000)*(moving)+0.45));
                            mPreviousScaleX = d.floatValue();
                            mScaleValue = d.floatValue();
                            mPrevipousScaleValue = mScaleValue;
                            Log.d(TAG, "SVX 1:" + mScaleValue);
                            Log.d(TAG, "ACTION_MOVE : mScaleValue : " + mScaleValue);

                            // for scaleY
                            // Double y1 = (((0.656 - 1) / (getScreenHeight())) * (moving)) + 1;
                            // Double y1 = (((0.656 - 1) / (getScreenHeight())) * (moving)) + 1;
                            Double y1 = (((0.41 - 1) / (getScreenHeight())) * (moving)) + 1;
                            // Double y1 = (((0.768-1)/(0.45-1)) * (mScaleValue)) + 0.578181;
                            // mScaleValueY = y1.floatValue();
                            mScaleValueY = y1.floatValue();
                            if (y1.floatValue() >= 0.656 || y1.floatValue() <= 1.0f) {
                                //  mScaleValueY=y1.floatValue();
                            }
                            //  mScaleValueY=(float) (0.421*moving)+0.578f;
                            mScaleValueYprevious = mScaleValueY;
                            Log.d(TAG, "ACTION_MOVE: mPrevipousScaleValue : " + mScaleValueYprevious);

                            // for mUuterRadiusValue value
                            Double outerRadius = ((35 - 0.45) / (getScreenWidth()) * (moving)) + .45;
                            mOuterRadiusValue = outerRadius.floatValue();
                            outerRadiusPreviousValue = mOuterRadiusValue;
                            Log.d(TAG, "ACTION_MOVE: mOuterRadiusValue : " + mOuterRadiusValue);

                            // for inner radious
                            Double inerRadius = ((28 - 0.45) / (getScreenWidth()) * (moving)) + .45;
                            mInerRadius = inerRadius.floatValue();
                            inerRadiusPreviousValue = mInerRadius;
                            Log.d(TAG, "ACTION_MOVE: Inner : " + mInerRadius);

                            // for transition
                            //  transitionValue  = (int) (((600-0) / (/*getScreenWidth()/2*/542) * (moving)));
                            // transitionValue  = (int) (((600) / (getScreenWidth()) * (moving))+1);
                            // transitionValue  = (int)2400*mScaleValue-getScreenWidth();
                            Double aa = (((0.0 - 600) / (getScreenWidth())) * (moving)) + 600;
                            // previousTransitionValue=aa.floatValue();
                            transitionValue = aa.floatValue();
                            Log.d(TAG, "ACTION_MOVE: transitionValue : " + transitionValue);
                            //Log.d(TAG, "AZ: transitionValue : " + aa.floatValue());
                            Log.d(TAG, "ACTION_MOVE: moving : " + moving);

                            // for mRotationY
                            // mRotationY= ((0-10)/(float)getScreenWidth())*moving;
                            Double d12 = ((-8 - 0.0) / (getScreenWidth()) * (moving)) + 0.0;
                            mRotationY = d12.floatValue();
                            mPreviousRotationY = d12.floatValue();
                            Log.d(TAG, "ACTION_MOVE: mRotationY : " + mRotationY);

                            Double alphaAnim = (((1 - 0.0) / getScreenWidth()) * moving) + 0.0f;
                            alphaAnimation = alphaAnim.floatValue();
                            previousAlphaAnimation = alphaAnimation;
                            Log.d(TAG, "ACTION_MOVE: alphaAnimation : " + alphaAnimation);

                        }

                        if (mScaleValue <= 1.0f && mScaleValue >= 0.41) {
                            Log.d("TAG", mScaleValue + "");
                            resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, /*deltaX*//* moving*/mScaleValue, xOffset, (int) lastActionDownX, x1 / screenWidth, false, isFingureUp, mScaleValueY, mOuterRadiusValue, mInerRadius, transitionValue, mRotationY, alphaAnimation);
                        }


                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d("CLICK", "ACtion UP");
                        if (!isClickAction) {
                            isFingureUp = true;
                            doorOpenFromProfilClick = false;
                            isOpenThenLeftSwipe = false;
                            Log.d("AAAA 2", "ACTION_UP");

                            x2 = (int) event.getX();
                            y2 = (int) event.getY();

                            Log.d(TAG, "mScaleValue" + mScaleValue);
                            Log.d(TAG, "mScaleValueY" + mScaleValueY);

                            dx = x2 - x1;
                            //  int xOfffset = (int) (event.getX() - lastActionDownX);
                            int xOfffset = (int) (event.getRawX() - lastActionDownX);

                            float deltaX = -(event.getRawX() - startX);
                            int directionOffset = xOfffset;


                            if (flingState == 1  /* fling State 1 for open door and 2 for close door*/) {
                                mScaleValue = 0.41f;
                                mScaleValueY = 0.656f;
                                transitionValue = 0;

                                mRotationY = -10;
                                mInerRadius = 28;
                                directionOffset = -xOfffset;
                                isOpened = true;
                                flingState = 0;

                            } else if (flingState == 2) {
                                mScaleValue = 1.0f;
                                mScaleValueY = 1.0f;
                                transitionValue = 600 - transitionValue;
                                mRotationY = 0;
                                mInerRadius = 0;
                                directionOffset = xOfffset;
                                isOpened = false;
                                flingState = 0;
                            } else if (mScaleValue < 0.57) {

                                mScaleValue = 0.41f;
                                mScaleValueY = 0.656f;
                                transitionValue = 0;
                                mRotationY = -10;
                                mInerRadius = 28;
                                directionOffset = -xOfffset;
                                isOpened = true;


                            } else {
                                mScaleValue = 1.0f;
                                mScaleValueY = 1.0f;
                                // transitionValue=600;
                                transitionValue = 600 - transitionValue;
                                mRotationY = 0;
                                mInerRadius = 0;
                                directionOffset = xOfffset;
                                isOpened = false;

                            }

                            Log.d(TAG, "ACTION_UP mScaleValue :" + mScaleValue);
                            Log.d(TAG, "ACTION_UP mScaleValueY :" + mScaleValueY);
                            Log.d(TAG, "ACTION_UP isOpened :" + isOpened);
                            resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, mScaleValue, directionOffset, (int) lastActionDownX, x1 / screenWidth, false, true, mScaleValueY, mOuterRadiusValue, mInerRadius, transitionValue, mRotationY, alphaAnimation);


                        } else {
                            if (isOpened) {
                                resideMenu.closeMenu();
                                isOpened = false;
                            } else {
                                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                                setPreviousStateData();
                            }

                        }


                        break;


                    case MotionEvent.ACTION_CANCEL:
                        Log.d("CLICK", "ACTION_CANCEL");
                        if (!isClickAction) {
                            isFingureUp = true;
                            doorOpenFromProfilClick = false;
                            isOpenThenLeftSwipe = false;
                            Log.d("AAAA 2", "ACTION_UP");

                            x2 = (int) event.getX();
                            y2 = (int) event.getY();

                            Log.d(TAG, "mScaleValue" + mScaleValue);
                            Log.d(TAG, "mScaleValueY" + mScaleValueY);

                            dx = x2 - x1;
                            //  int xOfffset = (int) (event.getX() - lastActionDownX);
                            int xOfffset = (int) (event.getRawX() - lastActionDownX);

                            float deltaX = -(event.getRawX() - startX);
                            int directionOffset = xOfffset;


                            if (flingState == 1 /*fling State 1 for open door and 2 for close door*/) {
                                mScaleValue = 0.41f;
                                mScaleValueY = 0.656f;
                                transitionValue = 0;

                                mRotationY = -10;
                                mInerRadius = 28;
                                directionOffset = -xOfffset;
                                isOpened = true;
                                flingState = 0;

                            } else if (flingState == 2) {
                                mScaleValue = 1.0f;
                                mScaleValueY = 1.0f;
                                transitionValue = 600 - transitionValue;
                                mRotationY = 0;
                                mInerRadius = 0;
                                directionOffset = xOfffset;
                                isOpened = false;
                                flingState = 0;
                            } else if (mScaleValue < 0.57) {

                                mScaleValue = 0.41f;
                                mScaleValueY = 0.656f;
                                transitionValue = 0;
                                mRotationY = -10;
                                mInerRadius = 28;
                                directionOffset = -xOfffset;
                                isOpened = true;


                            } else {
                                mScaleValue = 1.0f;
                                mScaleValueY = 1.0f;
                                // transitionValue=600;
                                transitionValue = 600 - transitionValue;
                                mRotationY = 0;
                                mInerRadius = 0;
                                directionOffset = xOfffset;
                                isOpened = false;

                            }

                            Log.d(TAG, "ACTION_UP mScaleValue :" + mScaleValue);
                            Log.d(TAG, "ACTION_UP mScaleValueY :" + mScaleValueY);
                            Log.d(TAG, "ACTION_UP isOpened :" + isOpened);
                            resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, mScaleValue, directionOffset, (int) lastActionDownX, x1 / screenWidth, false, true, mScaleValueY, mOuterRadiusValue, mInerRadius, transitionValue, mRotationY, alphaAnimation);


                        } else {
                            if (isOpened) {
                                resideMenu.closeMenu();
                                isOpened = false;
                            } else {
                                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                                setPreviousStateData();
                            }

                        }


                        break;
                    case MotionEvent.ACTION_HOVER_ENTER:
                        Log.d("CLICK", "ACTION_HOVER_ENTER");
                        break;


                }


                return true;
            }
        });

        layoutParams = (RelativeLayout.LayoutParams) cvInner.getLayoutParams();
        fragParams = (FrameLayout.LayoutParams) cvDashboard.getLayoutParams();
        resideMenu = new ResideMenu(this, cvDashboard, cvInner);
        resideMenu = new ResideMenu(this, cvDashboard, cvInner);

        resideMenu.attachToActivity(this);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Show Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );


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

    private void setPreviousStateData() {
        mScaleValue = 0.41f;
        mScaleValueY = 0.656f;
        doorOpenFromProfilClick = true;
        isOpened = true;

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
        flingState = 2;
    }

    private void onSwipeLeft() {
        Log.d("AAAA", "Left Swipe");
        flingState = 1;

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
