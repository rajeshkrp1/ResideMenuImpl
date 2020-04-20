package com.rkp.residemenuimpl;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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




    private ResideMenu.OnMenuDublicateListener menuDublicateListener =new ResideMenu.OnMenuDublicateListener() {
        @Override
        public void openDublicateMenu() {

        }

        @Override
        public void closeDublicateMenu() {

        }
    };

    private boolean isOpened;
    private int screenWidth;

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
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();

                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):
                        lastActionDownX = event.getX();
                        lastActionDownY = event.getY();
                        x1 = (int) event.getX();
                        y1 = (int) event.getY();
                        Log.d(TAG, "ACTION_DOWN: " + x1 + " ,  " + y1);
                        break;

                    case (MotionEvent.ACTION_MOVE):
                        Log.d(TAG, "ACTION_MOVE: " + x1 + " ,  " + y1);

                        int xOffset = (int) (event.getX() - lastActionDownX);
                        int yOffset = (int) (event.getY() - lastActionDownY);
                        Log.d(TAG, "ACTION_MOVE: offsetX " + xOffset );
                       resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1,xOffset,x1/screenWidth,false);
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

                        resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT, x1,xOfffset,x1/screenWidth,true);


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

        Log.d(TAG,screenWidth+"    screen");


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
        mSideMenuView = inflate(this, R.layout.layout_side_menu, null);

        resideMenu.setUse3D(true);
        resideMenu.addMenuItem(mSideMenuView, ResideMenu.DIRECTION_RIGHT);
        resideMenu.setShadowVisible(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_icon:
                //resideMenu.openDublicateMenu(ResideMenu.DIRECTION_RIGHT);
                break;

        }
    }


}
