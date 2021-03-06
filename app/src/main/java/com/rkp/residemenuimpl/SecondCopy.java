package com.rkp.residemenuimpl;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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

public class SecondCopy extends AppCompatActivity implements View.OnClickListener {
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    private static final String TAG = "MainActivity";
    CardView cvInner, cvDashboard;
    FrameLayout fl_image;
    int x1, x2, y1, y2, dx, dy;
    float startX,deltaX,previousDeltaX=-1080,initialDeltaX;
    String direction;
    Context mContext;
    private ResideMenu resideMenu;
    private RelativeLayout.LayoutParams layoutParams;
    private FrameLayout.LayoutParams fragParams;
    private View mSideMenuView = null;
    private float mScaleValue = 0.41f;
    private TouchDisableView viewActivity;
    private ObjectAnimator lftToRgt, rgtToLft;
    private float halfW;
    private AnimatorSet animatorSet;
    private int percentage;
    private float lastActionDownX, lastActionDownY;


    private ResideMenu.OnMenuDublicateListener menuDublicateListener = new ResideMenu.OnMenuDublicateListener() {
        @Override
        public void openDublicateMenu() {

        }

        @Override
        public void closeDublicateMenu() {

        }
    };



    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener(){

        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }
    };



    private boolean isOpened;
    private int screenWidth;
    private boolean first,second,third,fourth,fifth,six,seven,eight;

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
                 float X =  event.getRawX();
                final float Y =  event.getRawY();

                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):
                        lastActionDownX = event.getRawX();
                        lastActionDownY = event.getRawY();
                        startX =  event.getRawX();
                        x1 = (int) event.getX();
                        y1 = (int) event.getY();
                        Log.d(TAG, "ACTION_DOWN: " + x1 + " ,  " + y1);
                        Log.d(TAG, "ACTION_DOWN: getX " +startX);
                        break;

                    case (MotionEvent.ACTION_MOVE):
                        Log.d(TAG, "ACTION_MOVE: " + x1 + " ,  " + y1);

                       // int xOffset = (int) (event.getX() - lastActionDownX);
                        int xOffset = (int) (event.getRawX() - lastActionDownX);
                        int yOffset = (int) (event.getY() - lastActionDownY);

                        float moving=-(event.getRawX()-startX);
                        deltaX=moving-(previousDeltaX);

                        //previousDeltaX=deltaX;

                        Log.d(TAG, "ACTION_MOVE: deltaX " + deltaX);
                        Log.d(TAG, "ACTION_MOVE: moving " + moving);
                        Log.d(TAG, "ACTION_MOVE: offsetX " + xOffset);
                        Log.d(TAG, "ACTION_MOVE: lastActionDownX " + lastActionDownX);
                        Log.d(TAG, "ACTION_MOVE: event.getX() " + event.getRawX());

                        resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, /*deltaX*/moving, xOffset, (int)lastActionDownX,x1 / screenWidth, false,false,0.0f,0.0f);
                        //  resideMenu.closeDublicateMenu(ResideMenu.DIRECTION_RIGHT,x1,xOffset,false);
                        // resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                       /* if(x1>0){
                            percentage=(x1/screenWidth)*100;
                            Log.d(TAG,percentage+"   per");
                        }*/

                        break;


                    case (MotionEvent.ACTION_UP): {
                        x2 = (int) event.getX();
                        y2 = (int) event.getY();
                        dx = x2 - x1;
                        dy = y2 - y1;
                        int xOfffset = (int) (event.getX() - lastActionDownX);
                        Log.d(TAG, "ACTION_UP: " + x1 + " ,  " + y1);
                        Log.d(TAG, "ACTION_UP:  event.getX()  " +  event.getX());
                        Log.d(TAG, "ACTION_UP:  lastActionDownX  " +  lastActionDownX);
                        //resideMenu.setScaleDirectionByRawX(event.getRawX());
                        resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, deltaX, xOfffset, (int)lastActionDownX,x1 / screenWidth, false,false,0.0f,0.0f);




                        //  resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, xOfffset,(int)lastActionDownX, x1 / screenWidth, true);


                        // Use dx and dy to determine the direction of the move
                        if (Math.abs(dx) > Math.abs(dy)) {

                            if (dx > 0) {
                                direction = "right";
                                // resideMenu.closeDublicateMenu(ResideMenu.DIRECTION_RIGHT,x1,xOfffset,true);

                            } else {
                                direction = "left";
                                //  resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1,xOfffset,x1/screenWidth,true);

                            }

                        } else {
                            if (dy > 0)
                                direction = "down";
                            else
                                direction = "up";
                        }
                    }
                }
                return false;
            }

        });

    }

    private void initViews() {
        animatorSet = new AnimatorSet();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        Log.d(TAG, screenWidth + "    screen");


        mContext = SecondCopy.this;
        cvInner = findViewById(R.id.cv_inner);
        cvDashboard = findViewById(R.id.cv_dashboard);
        fl_image = findViewById(R.id.fl_image);

        fl_image.setOnClickListener(this);

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
            case R.id.fl_image:
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                break;

        }
    }


  /*  @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }*/






  private void changeVariableValue(){
        if(!first){
            mScaleValue=1.0f;
           // resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);
            first=true;
        }else if(!second){
            mScaleValue=0.9542f;
           // resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

            second=true;
        }else if(!third){
            mScaleValue=0.7932f;
            //resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

            third=true;
        }else if(!fourth){
            mScaleValue=0.565f;
            //resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

            fourth=true;
        }else if(!fifth){
            mScaleValue=0.52631f;
           // resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

            fifth=true;
        }else if(!six){
            mScaleValue=0.49625498f;
            //resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

            six=true;
        }else if(!seven){
            mScaleValue=0.48369092f;
           /// resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

            seven=true;
        }else if(!eight){
            mScaleValue=0.46452343f;
           // resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

            eight=true;
        }else {
            mScaleValue = 0.41f;
           // resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1, 00,(int)lastActionDownX, mScaleValue, true);

        }



  }

}
