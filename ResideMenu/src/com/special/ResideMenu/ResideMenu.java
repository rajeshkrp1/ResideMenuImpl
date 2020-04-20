package com.special.ResideMenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

/**
 * User: special
 * Date: 13-12-10
 * Time: 下午10:44
 * Mail: specialcyci@gmail.com
 */
public class ResideMenu extends FrameLayout {


    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    private static final int PRESSED_MOVE_HORIZONTAL = 2;
    private static final int PRESSED_DOWN = 3;
    private static final int PRESSED_DONE = 4;
    private static final int PRESSED_MOVE_VERTICAL = 5;
    private static final int ROTATE_Y_ANGLE = 10;
    private EdgeSwipeController edgeSwipeController;
    private ImageView imageViewShadow;
    private ImageView imageViewBackground;
    private LinearLayout layoutLeftMenu;
    private LinearLayout layoutRightMenu;
    private View scrollViewLeftMenu;
    private View scrollViewRightMenu;
    private View scrollViewMenu;
    private int scrW;
    /**
     * Current attaching activity.
     */
    private Activity activity;
    /**
     * The DecorView of current activity.
     */
    private ViewGroup viewDecor;
    private TouchDisableView viewActivity;
    /**
     * The flag of menu opening status.
     */
    private boolean isOpened;
    private float shadowAdjustScaleX;
    private float shadowAdjustScaleY;
    /**
     * Views which need stop to intercept touch events.
     */
    private List<View> ignoredViews;
    private List<ResideMenuItem> leftMenuItems;
    private List<ResideMenuItem> rightMenuItems;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private OnMenuListener menuListener;
    private OnMenuDublicateListener menuDublicateListener;
    private float lastRawX;
    private boolean isInIgnoredView = false;
    private int scaleDirection = DIRECTION_LEFT;
    private int pressedState = PRESSED_DOWN;
    private List<Integer> disabledSwipeDirection = new ArrayList<Integer>();
    // Valid scale factor is between 0.0f and 1.0f.
    //private float mScaleValue = 0.41f;
    private float mScaleValue = 0.41f;
    private float multiPlyerValye = 1.6f;
    private boolean mUse3D;
    private float lastActionDownX, lastActionDownY;
    private RelativeLayout.LayoutParams layoutParams;
    private CardView cvInner;
    private CardView cvDashboard;
    private ScrollView svRightMenu;
    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened()) {
                showScrollViewMenu(scrollViewMenu);
                if (menuListener != null)
                    menuListener.openMenu();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // reset the view;
            if (isOpened()) {
                viewActivity.setTouchDisable(true);
                viewActivity.setOnClickListener(viewActivityOnClickListener);
            } else {
                viewActivity.setTouchDisable(false);
                viewActivity.setOnClickListener(null);
                hideScrollViewMenu(scrollViewLeftMenu);
                hideScrollViewMenu(scrollViewRightMenu);
                if (menuListener != null) {
                    menuListener.closeMenu();
                }
            }
            svRightMenu.fullScroll(View.FOCUS_UP);

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }


        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };


    private Animator.AnimatorListener animationDublicateListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened()) {
                showScrollViewMenu(scrollViewMenu);
                if (menuDublicateListener != null)
                    menuDublicateListener.openDublicateMenu();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // reset the view;
            if (isOpened()) {
                viewActivity.setTouchDisable(true);
                viewActivity.setOnClickListener(viewActivityOnClickListener);
            } else {
                viewActivity.setTouchDisable(false);
                viewActivity.setOnClickListener(null);
                hideScrollViewMenu(scrollViewLeftMenu);
                hideScrollViewMenu(scrollViewRightMenu);
                if (menuDublicateListener != null) {
                    menuDublicateListener.closeDublicateMenu();
                }
            }
            svRightMenu.fullScroll(View.FOCUS_UP);

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }


        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };







    private OnClickListener viewActivityOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isOpened()){
                closeMenu();
                closeDublicateMenu(ResideMenu.DIRECTION_RIGHT,00,00,true);

            } closeMenu();
        }
    };
    private boolean openMenuStarted;

    public ResideMenu(Context context, CardView cvDashboard, CardView cvInner) {
        super(context);
        this.cvInner = cvInner;
        this.cvDashboard = cvDashboard;
        initViews(context, -1, -1);
        edgeSwipeController = new EdgeSwipeController((Activity) context);
        edgeSwipeController.setSwipeListener(new EdgeSwipeController.OnSwipeListener() {
            @Override
            public void rightEdgeSwipe() {
                if (!isOpened)
                    openMenu(ResideMenu.DIRECTION_RIGHT);
            }

            @Override
            public void leftEdgeSwipe() {
                if (isOpened)
                    closeMenu();

            }
        });
    }

    /**
     * This constructor provides you to create menus with your own custom
     * layouts, but if you use custom menu then do not call addMenuItem because
     * it will not be able to find default views
     */
    public ResideMenu(Context context, int customLeftMenuId,
                      int customRightMenuId) {
        super(context);
        initViews(context, customLeftMenuId, customRightMenuId);
    }

    private void initViews(Context context, int customLeftMenuId,
                           int customRightMenuId) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu_custom, this);

        if (customLeftMenuId >= 0) {
            scrollViewLeftMenu = inflater.inflate(customLeftMenuId, this, false);
        } else {
            scrollViewLeftMenu = inflater.inflate(
                    R.layout.residemenu_custom_left_scrollview, this, false);
            layoutLeftMenu = scrollViewLeftMenu.findViewById(R.id.layout_left_menu);
        }

        if (customRightMenuId >= 0) {
            scrollViewRightMenu = inflater.inflate(customRightMenuId, this, false);
        } else {
            scrollViewRightMenu = inflater.inflate(
                    R.layout.residemenu_custom_right_scrollview, this, false);
            layoutRightMenu = scrollViewRightMenu.findViewById(R.id.layout_right_menu);
            svRightMenu = scrollViewRightMenu.findViewById(R.id.sv_right_menu);
        }

        imageViewShadow = findViewById(R.id.iv_shadow);
        imageViewBackground = findViewById(R.id.iv_background);

        RelativeLayout menuHolder = findViewById(R.id.sv_menu_holder);
        menuHolder.addView(scrollViewLeftMenu);
        menuHolder.addView(scrollViewRightMenu);
        imageViewShadow.setVisibility(GONE);
        imageViewBackground.setVisibility(GONE);

    }

    public View getRightMenu() {
        return layoutRightMenu;
    }

    /**
     * Returns left menu view so you can findViews and do whatever you want with
     */
    public View getLeftMenuView() {
        return scrollViewLeftMenu;
    }

    /**
     * Returns right menu view so you can findViews and do whatever you want with
     */
    public View getRightMenuView() {
        return scrollViewRightMenu;
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        // Applies the content insets to the view's padding, consuming that
        // content (modifying the insets to be 0),
        // and returning true. This behavior is off by default and can be
        // enabled through setFitsSystemWindows(boolean)
        // in api14+ devices.

        // This is added to fix soft navigationBar's overlapping to content above LOLLIPOP
       /* int bottomPadding = viewActivity.getPaddingBottom() + insets.bottom;
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        if (!hasBackKey || !hasHomeKey) {//there's a navigation bar
            bottomPadding += getNavigationBarHeight();
        }

        this.setPadding(viewActivity.getPaddingLeft() + insets.left,
                viewActivity.getPaddingTop() + insets.top,
                viewActivity.getPaddingRight() + insets.right,
                bottomPadding);
        insets.left = insets.top = insets.right = insets.bottom = 0;*/
        return true;
    }

    private int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    /**
     * Set up the activity;
     *
     * @param activity
     */
    public void attachToActivity(Activity activity) {
        initValue(activity);
        setShadowAdjustScaleXByOrientation();
        viewDecor.addView(this, 0);
    }

    private void initValue(Activity activity) {
        this.activity = activity;
        leftMenuItems = new ArrayList<ResideMenuItem>();
        rightMenuItems = new ArrayList<ResideMenuItem>();
        ignoredViews = new ArrayList<View>();
        viewDecor = (ViewGroup) activity.getWindow().getDecorView();
        viewActivity = new TouchDisableView(this.activity);

        View mContent = viewDecor.getChildAt(0);
        viewDecor.removeViewAt(0);
        viewActivity.setContent(mContent);
        addView(viewActivity);

        ViewGroup parent = (ViewGroup) scrollViewLeftMenu.getParent();
        parent.removeView(scrollViewLeftMenu);
        parent.removeView(scrollViewRightMenu);
    }

    private void setShadowAdjustScaleXByOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            shadowAdjustScaleX = 0.030f;
            shadowAdjustScaleY = 0.12f;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            shadowAdjustScaleX = 0.04f;
            shadowAdjustScaleY = 0.07f;
        }
    }

    /**
     * Set the background image of menu;
     *
     * @param imageResource
     */
    public void setBackground(int imageResource) {
        imageViewBackground.setImageResource(imageResource);
    }

    /**
     * The visibility of the shadow under the activity;
     *
     * @param isVisible
     */
    public void setShadowVisible(boolean isVisible) {
        if (isVisible)
            imageViewShadow.setBackgroundResource(R.drawable.shadow);
        else
            imageViewShadow.setBackgroundResource(0);
    }

    /**
     * Add a single item to the left menu;
     * <p/>
     * WARNING: It will be removed from v2.0.
     *
     * @param menuItem
     */
    @Deprecated
    public void addMenuItem(ResideMenuItem menuItem) {
        this.leftMenuItems.add(menuItem);
        layoutLeftMenu.addView(menuItem);
    }

    /**
     * Add a single items;
     *
     * @param menuItem
     * @param direction
     */
    public void addMenuItem(View menuItem, int direction) {
        layoutRightMenu.removeAllViews();
        layoutRightMenu.addView(menuItem);

    }

    /**
     * Set menu items by a array;
     *
     * @param menuItems
     * @param direction
     */
    public void setMenuItems(List<ResideMenuItem> menuItems, int direction) {
        if (direction == DIRECTION_LEFT)
            this.leftMenuItems = menuItems;
        else
            this.rightMenuItems = menuItems;
        rebuildMenu();
    }

    private void rebuildMenu() {
        if (layoutLeftMenu != null) {
            layoutLeftMenu.removeAllViews();
            for (ResideMenuItem leftMenuItem : leftMenuItems)
                layoutLeftMenu.addView(leftMenuItem);
        }

        if (layoutRightMenu != null) {
            layoutRightMenu.removeAllViews();
            for (ResideMenuItem rightMenuItem : rightMenuItems)
                layoutRightMenu.addView(rightMenuItem);
        }
    }

    /**
     * WARNING: It will be removed from v2.0.
     *
     * @return
     */
    @Deprecated
    public List<ResideMenuItem> getMenuItems() {
        return leftMenuItems;
    }

    /**
     * WARNING: It will be removed from v2.0.
     *
     * @param menuItems
     */
    @Deprecated
    public void setMenuItems(List<ResideMenuItem> menuItems) {
        this.leftMenuItems = menuItems;
        rebuildMenu();
    }

    /**
     * Return instances of menu items;
     *
     * @return
     */
    public List<ResideMenuItem> getMenuItems(int direction) {
        if (direction == DIRECTION_LEFT)
            return leftMenuItems;
        else
            return rightMenuItems;
    }

    public OnMenuListener getMenuListener() {
        return menuListener;
    }

    /**
     * If you need to do something on closing or opening menu,
     * set a listener here.
     *
     * @return
     */
    public void setMenuListener(OnMenuListener menuListener) {
        this.menuListener = menuListener;
    }

    public void setMenuDublicateListener(OnMenuDublicateListener menuListener) {
        this.menuDublicateListener = menuListener;
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Edge Swipe
        if (edgeSwipeController.onEdgeSwipeEvent(ev)) {
            return super.dispatchTouchEvent(ev);
        }

        return false;
        //new implementation

      /*  float currentActivityScaleX = ViewHelper.getScaleX(viewActivity);
        Log.e("TAGScaleX",currentActivityScaleX+"");
        if (currentActivityScaleX == 1.0f) setScaleDirectionByRawX(ev.getRawX());

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("TAGdown","action down");

                lastActionDownX = ev.getX();
                lastActionDownY = ev.getY();
                Log.e("TAGev.getX",ev.getX()+"");
                Log.e("TAGev.getY",ev.getY()+"");
                isInIgnoredView = isInIgnoredView(ev) && !isOpened();
                pressedState = PRESSED_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e("TAGmove","action move");

                if (lastActionDownX > getScreenWidth() / 2) {
                    break;
                }
                if (isInIgnoredView || isInDisableDirection(scaleDirection)) break;

                if (pressedState != PRESSED_DOWN && pressedState != PRESSED_MOVE_HORIZONTAL) break;

                int xOffset = (int) (ev.getX() - lastActionDownX);
                int yOffset = (int) (ev.getY() - lastActionDownY);
                Log.e("TAGxOffset",xOffset+"");
                Log.e("TAGyOffset",yOffset+"");

                if (pressedState == PRESSED_DOWN) {
                    if (yOffset > 25 || yOffset < -25) {
                        pressedState = PRESSED_MOVE_VERTICAL;
                        break;
                    }
                    if (xOffset < -50 || xOffset > 50) {
                        pressedState = PRESSED_MOVE_HORIZONTAL;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else
                if (pressedState == PRESSED_MOVE_HORIZONTAL) {
                    Log.e("TAGpressedState","PRESSED_MOVE_HORIZONTAL");
                    if (currentActivityScaleX < 0.95) showScrollViewMenu(scrollViewMenu);

                    float targetScale = getTargetScale(ev.getRawX());
                    Log.e("ATGtargetScale",targetScale+"");
                    if (mUse3D) {
                        int angle = scaleDirection == DIRECTION_LEFT ? -ROTATE_Y_ANGLE :
                                ROTATE_Y_ANGLE;
                        angle *= (1 - targetScale) * 2;
                        // ViewHelper.setRotationY(viewActivity, angle);

                        ViewHelper.setScaleX(imageViewShadow, targetScale - shadowAdjustScaleX);
                        ViewHelper.setScaleY(imageViewShadow, targetScale - shadowAdjustScaleY);
                    } else {
                        ViewHelper.setScaleX(imageViewShadow, targetScale + shadowAdjustScaleX);
                        ViewHelper.setScaleY(imageViewShadow, targetScale + shadowAdjustScaleY);
                    }
                    ViewHelper.setScaleX(viewActivity, targetScale);
                    ViewHelper.setScaleY(viewActivity, targetScale);
                    ViewHelper.setAlpha(scrollViewMenu, (1 - targetScale) * 2.0f);

                    lastRawX = ev.getRawX();
                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:
                Log.e("TAup","action UP");


                if (lastActionDownX > getScreenWidth() / 2) {
                    break;
                }
                if (isInIgnoredView) break;
                if (pressedState != PRESSED_MOVE_HORIZONTAL) break;

                pressedState = PRESSED_DONE;
                if (isOpened()) {
                    if (currentActivityScaleX > 0.56f) closeMenu();
                    else openMenu(scaleDirection);

                } else {
                    if (currentActivityScaleX < 0.94f) {
                        openMenu(scaleDirection);
                    } else {
                        closeMenu();
                    }

                }

                break;

        }
        lastRawX = ev.getRawX();
        return super.dispatchTouchEvent(ev);


*/




    }

    /**
     * Show the menu;
     */
    public void  openMenu(int direction) {
        cvDashboard.setCardElevation(activity.getResources().getDimension(R.dimen._10sdp));
        cvDashboard.setElevation(activity.getResources().getDimension(R.dimen._10sdp));
        cvInner.setCardElevation(activity.getResources().getDimension(R.dimen._10sdp));
        scrollViewRightMenu.setElevation(-activity.getResources().getDimension(R.dimen._10sdp));


        imageViewShadow.setVisibility(VISIBLE);
        imageViewBackground.setVisibility(VISIBLE);
        PropertyValuesHolder translateX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 600, 0);
        ObjectAnimator translation = ObjectAnimator.ofPropertyValuesHolder(layoutRightMenu, translateX);
        layoutParams = (RelativeLayout.LayoutParams) cvInner.getLayoutParams();
        ValueAnimator outerRadiusAnimator = ValueAnimator.ofInt(0, (int) getResources().getDimension(R.dimen._10sdp));
        outerRadiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                cvDashboard.setRadius((int) valueAnimator.getAnimatedValue());
                cvDashboard.requestLayout();
            }
        });
        ValueAnimator animator = ValueAnimator.ofInt(0, (int) getResources().getDimension(R.dimen._8sdp));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.setMargins(0, 0, (int) valueAnimator.getAnimatedValue(), 0);
                cvInner.setRadius((int) valueAnimator.getAnimatedValue());
                cvInner.requestLayout();
            }
        });

        setScaleDirection(direction);
        isOpened = true;

        AnimatorSet scaleDown_activity = buildScaleDownAnimation(viewActivity, mScaleValue, mScaleValue * 1.6f);
        AnimatorSet scaleDown_shadow = buildScaleDownAnimation(imageViewShadow,
                (mScaleValue + shadowAdjustScaleX), (mScaleValue + shadowAdjustScaleY) * 1.6f);
        AnimatorSet alpha_menu = buildMenuAnimationOpen(scrollViewMenu, 1.0f);
        AnimatorSet aplha_rightMenu = buildMenuAnimationOpen(layoutRightMenu, 1.0f);
        scaleDown_activity.addListener(animationListener);
        scaleDown_activity.playTogether(scaleDown_shadow, alpha_menu, aplha_rightMenu, animator, outerRadiusAnimator, translation);
        scaleDown_activity.setDuration(400);
        scaleDown_activity.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.decelerate_interpolator));
       scaleDown_activity.start();


    }



    public void  openDublicateMenu(int direction,int axis_x,int xOffSet,int scaleX,boolean openstatus) {
/*            float  mScaleValueY=1.0f;
            multiPlyerValye=1.0f;
        if(xOffSet>0 &&  xOffSet<200){
            mScaleValue=0.7f;
            mScaleValueY=mScaleValue*1.1f;

            multiPlyerValye=1.2f;
        }else if(xOffSet>200 && xOffSet<400){
            mScaleValue=0.6f;
            mScaleValueY=mScaleValue*1.2f;
            multiPlyerValye=1.4f;
        }else if(xOffSet>400 && xOffSet<600){
            mScaleValue=0.5f;
            mScaleValueY=mScaleValue*1.3f;
            multiPlyerValye=1.5f;
        }else {
            mScaleValue = 0.4f;
            mScaleValueY=mScaleValue*1.4f;
            multiPlyerValye=1.6f;
        }*/
        if(!openMenuStarted){
            cvDashboard.setCardElevation(activity.getResources().getDimension(R.dimen._10sdp));
            cvDashboard.setElevation(activity.getResources().getDimension(R.dimen._10sdp));
            cvInner.setCardElevation(activity.getResources().getDimension(R.dimen._10sdp));
            scrollViewRightMenu.setElevation(-activity.getResources().getDimension(R.dimen._10sdp));

            imageViewShadow.setVisibility(VISIBLE);
            imageViewBackground.setVisibility(VISIBLE);
            openMenuStarted=true;
        }

        PropertyValuesHolder translateX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 600-axis_x, 0);
        ObjectAnimator translation = ObjectAnimator.ofPropertyValuesHolder(layoutRightMenu, translateX);

        layoutParams = (RelativeLayout.LayoutParams) cvInner.getLayoutParams();
        ValueAnimator outerRadiusAnimator = ValueAnimator.ofInt(0, (int) getResources().getDimension(R.dimen._10sdp));
        outerRadiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                cvDashboard.setRadius((int) valueAnimator.getAnimatedValue());
                cvDashboard.requestLayout();
            }
        });
        ValueAnimator animator = ValueAnimator.ofInt(0, (int) getResources().getDimension(R.dimen._8sdp));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.setMargins(0, 0, (int) valueAnimator.getAnimatedValue(), 0);
                cvInner.setRadius((int) valueAnimator.getAnimatedValue());
                cvInner.requestLayout();
            }
        });

        setdublicateScaleDirection(direction,axis_x,xOffSet);
        if(openstatus)
       isOpened = true;

        AnimatorSet scaleDown_activity = buildScaleDownAnimation(viewActivity ,/*axis_x/scrW*/mScaleValue,mScaleValue /*mScaleValue*//*(axis_x/scrW)*/*multiPlyerValye/*1.6f*/);
        AnimatorSet scaleDown_shadow = buildScaleDownAnimation(imageViewShadow,
                (mScaleValue/*(axis_x/scrW)*/ + shadowAdjustScaleX), (/*(axis_x/scrW)*/mScaleValue + shadowAdjustScaleY) *multiPlyerValye/*1.6f*/);
        AnimatorSet alpha_menu = buildMenuAnimationOpen(scrollViewMenu, 1.0f);
        AnimatorSet aplha_rightMenu = buildMenuAnimationOpen(layoutRightMenu, 1.0f);
        //scaleDown_activity.addListener(animationListener);
        scaleDown_activity.addListener(animationDublicateListener);

        scaleDown_activity.playTogether(scaleDown_shadow, alpha_menu, aplha_rightMenu, animator, outerRadiusAnimator, translation);
       // scaleDown_activity.setDuration(800);
        scaleDown_activity.setDuration(0);
        scaleDown_activity.setInterpolator(AnimationUtils.loadInterpolator(activity, android.R.anim.decelerate_interpolator));
        scaleDown_activity.start();


    }


    public void closeDublicateMenu(int direction,int axis_x,int offSet,boolean menuOpenSatatus) {


        cvDashboard.setCardElevation(activity.getResources().getDimension(R.dimen._10sdp));
        cvDashboard.setElevation(activity.getResources().getDimension(R.dimen._10sdp));
        cvInner.setCardElevation(activity.getResources().getDimension(R.dimen._10sdp));
        scrollViewRightMenu.setElevation(-activity.getResources().getDimension(R.dimen._10sdp));
        ValueAnimator animator = ValueAnimator.ofInt((int) getResources().getDimension(R.dimen._10sdp), 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                layoutParams.setMargins(0, 0, (int) valueAnimator.getAnimatedValue(), 0);
                cvInner.setRadius((int) valueAnimator.getAnimatedValue());
                cvInner.requestLayout();
            }
        });
        ValueAnimator outerRadiusAnimator = ValueAnimator.ofInt((int) getResources().getDimension(R.dimen._10sdp), 0);
        outerRadiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                cvDashboard.setRadius((int) valueAnimator.getAnimatedValue());
                cvDashboard.requestLayout();
            }
        });

        animator.setDuration(0);
        outerRadiusAnimator.setDuration(0);
        PropertyValuesHolder translateX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0, 600-axis_x);
        ObjectAnimator translation = ObjectAnimator.ofPropertyValuesHolder(layoutRightMenu, translateX);

        if(menuOpenSatatus)
        isOpened = false;
        AnimatorSet scaleUp_activity = buildScaleUpAnimation(viewActivity, mScaleValue/*1.0f*//*axis_x/scrW*/, 1.0f);
        AnimatorSet scaleUp_shadow = buildScaleUpAnimation(imageViewShadow, mScaleValue/*1.0f*//*axis_x/scrW*/, 1.0f);
        AnimatorSet alpha_menu = buildMenuAnimationOpen(scrollViewMenu, 0.0f);
        AnimatorSet alpha_right_menu = buildMenuAnimationOpen(layoutRightMenu, 0.0f);

        //scaleUp_activity.addListener(animationListener);
        scaleUp_activity.addListener(animationDublicateListener);

        scaleUp_activity.playTogether(scaleUp_shadow, alpha_menu, alpha_right_menu, animator, outerRadiusAnimator, translation);
        scaleUp_activity.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.accelerate_interpolator
        ));
        scaleUp_activity.setDuration(0);
        scaleUp_activity.start();



    }






    /**
     * Close the menu;
     */
    public void closeMenu() {
        cvDashboard.setCardElevation(activity.getResources().getDimension(R.dimen._10sdp));
        cvDashboard.setElevation(activity.getResources().getDimension(R.dimen._10sdp));
        cvInner.setCardElevation(activity.getResources().getDimension(R.dimen._10sdp));
        scrollViewRightMenu.setElevation(-activity.getResources().getDimension(R.dimen._10sdp));
        ValueAnimator animator = ValueAnimator.ofInt((int) getResources().getDimension(R.dimen._10sdp), 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                layoutParams.setMargins(0, 0, (int) valueAnimator.getAnimatedValue(), 0);
                cvInner.setRadius((int) valueAnimator.getAnimatedValue());
                cvInner.requestLayout();
            }
        });
        ValueAnimator outerRadiusAnimator = ValueAnimator.ofInt((int) getResources().getDimension(R.dimen._10sdp), 0);
        outerRadiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                cvDashboard.setRadius((int) valueAnimator.getAnimatedValue());
                cvDashboard.requestLayout();
            }
        });

        animator.setDuration(250);
        outerRadiusAnimator.setDuration(250);
        PropertyValuesHolder translateX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0, 600);
        ObjectAnimator translation = ObjectAnimator.ofPropertyValuesHolder(layoutRightMenu, translateX);

        isOpened = false;
        AnimatorSet scaleUp_activity = buildScaleUpAnimation(viewActivity, 1.0f, 1.0f);
        AnimatorSet scaleUp_shadow = buildScaleUpAnimation(imageViewShadow, 1.0f, 1.0f);
        AnimatorSet alpha_menu = buildMenuAnimationOpen(scrollViewMenu, 0.0f);
        AnimatorSet alpha_right_menu = buildMenuAnimationOpen(layoutRightMenu, 0.0f);

        scaleUp_activity.addListener(animationListener);
        scaleUp_activity.playTogether(scaleUp_shadow, alpha_menu, alpha_right_menu, animator, outerRadiusAnimator, translation);
        scaleUp_activity.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.accelerate_interpolator
        ));
        scaleUp_activity.setDuration(350);
        scaleUp_activity.start();

    }

    @Deprecated
    public void setDirectionDisable(int direction) {
        disabledSwipeDirection.add(direction);
    }

    public void setSwipeDirectionDisable(int direction) {
        disabledSwipeDirection.add(direction);
    }

    private boolean isInDisableDirection(int direction) {
        return disabledSwipeDirection.contains(direction);
    }



    public void setdublicateScaleDirection(int direction,int axis_x,int x_ofset) {

        int screenWidth = getScreenWidth();
        scrW=getScreenHeight();

        float pivotX=0.5f;
        float pivotY = getScreenHeight() * 0.5f;

        if (direction == DIRECTION_LEFT) {
            scrollViewMenu = scrollViewLeftMenu;
            pivotX = (screenWidth-axis_x) * -0.5f;
            Log.d("ASD",pivotX+"     pivot x");
        } else {
            scrollViewMenu = scrollViewRightMenu;
           // pivotX = screenWidth * -0.0002f;
           // pivotX = screenWidth * -0.5f;
            if (x_ofset/2>=(screenWidth*-0.5f)){
                pivotX=x_ofset/2;
            }
            Log.d("ASD",pivotX+"     pivot x");
            Log.d("ASD",pivotY+"     pivot Y");



        }

        ViewHelper.setPivotX(viewActivity, pivotX);
        ViewHelper.setPivotY(viewActivity, pivotY);
        ViewHelper.setPivotX(imageViewShadow, pivotX);
        ViewHelper.setPivotY(imageViewShadow, pivotY);
        scaleDirection = direction;

    }

    public void setScaleDirection(int direction) {

        int screenWidth = getScreenWidth();
        float pivotX;
        float pivotY = getScreenHeight() * 0.5f;

        if (direction == DIRECTION_LEFT) {
            scrollViewMenu = scrollViewLeftMenu;
            pivotX = screenWidth * 1.5f;
        } else {
            scrollViewMenu = scrollViewRightMenu;
            pivotX = screenWidth * -0.5f;

        }
        Log.e("PIVOTx",pivotX+"");
        Log.e("PIVOTy",pivotY+"");


        ViewHelper.setPivotX(viewActivity, pivotX);
        ViewHelper.setPivotY(viewActivity, pivotY);
        ViewHelper.setPivotX(imageViewShadow, pivotX);
        ViewHelper.setPivotY(imageViewShadow, pivotY);
        scaleDirection = direction;

    }

    /**
     * return the flag of menu status;
     *
     * @return
     */
    public boolean isOpened() {
        return isOpened;
    }

    /**
     * A helper method to build scale down animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleDownAnimation(View target, float targetScaleX, float targetScaleY) {

        AnimatorSet scaleDown = new AnimatorSet();
        AnimatorSet scaleXY = new AnimatorSet();
        scaleXY.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );


        if (mUse3D) {
            int angle = scaleDirection == DIRECTION_LEFT ? -ROTATE_Y_ANGLE : ROTATE_Y_ANGLE;
            scaleDown.playTogether(scaleXY, ObjectAnimator.ofFloat(target, "rotationY", 0f, -(float) 8));
        }


        return scaleDown;
    }

    /**
     * A helper method to build scale up animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleUpAnimation(View target, float targetScaleX, float targetScaleY) {


        AnimatorSet scaleUp = new AnimatorSet();
        AnimatorSet scaleXY = new AnimatorSet();
        scaleXY.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );
        if (mUse3D) {
            scaleUp.playTogether(ObjectAnimator.ofFloat(target, "rotationY", 0), scaleXY);
        }


        return scaleUp;
    }


    private AnimatorSet buildMenuAnimationOpen(View target, float alpha) {

        AnimatorSet alphaAnimation = new AnimatorSet();
        alphaAnimation.play(
                ObjectAnimator.ofFloat(target, "alpha", alpha)
        );
        alphaAnimation.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.accelerate_decelerate_interpolator));
        alphaAnimation.setDuration(250);
        return alphaAnimation;
    }

    /**
     * If there were some view you don't want reside menu
     * to intercept their touch event, you could add it to
     * ignored views.
     *
     * @param v
     */
    public void addIgnoredView(View v) {
        ignoredViews.add(v);
    }

    /**
     * Remove a view from ignored views;
     *
     * @param v
     */
    public void removeIgnoredView(View v) {
        ignoredViews.remove(v);
    }

    /**
     * Clear the ignored view list;
     */
    public void clearIgnoredViewList() {
        ignoredViews.clear();
    }

    /**
     * If the motion event was relative to the view
     * which in ignored view list,return true;
     *
     * @param ev
     * @return
     */
    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : ignoredViews) {
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY()))
                return true;
        }
        return false;
    }

    private void setScaleDirectionByRawX(float currentRawX) {
        if (currentRawX < lastRawX)
            setScaleDirection(DIRECTION_RIGHT);
        else
            setScaleDirection(DIRECTION_LEFT);
    }

    private float getTargetScale(float currentRawX) {
        float scaleFloatX = ((currentRawX - lastRawX) / getScreenWidth()) * 0.75f;
        scaleFloatX = scaleDirection == DIRECTION_RIGHT ? -scaleFloatX : scaleFloatX;

        float targetScale = ViewHelper.getScaleX(viewActivity) - scaleFloatX;
        targetScale = targetScale > 1.0f ? 1.0f : targetScale;
        targetScale = targetScale < 0.5f ? 0.5f : targetScale;
        return targetScale;
    }


  /*  @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentActivityScaleX = ViewHelper.getScaleX(viewActivity);
        if (currentActivityScaleX == 1.0f)
            setScaleDirectionByRawX(ev.getRawX());
        if (ev.getAction() == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() == MotionEvent.EDGE_RIGHT) {
            openMenu(ResideMenu.DIRECTION_RIGHT);
             return super.dispatchTouchEvent(ev);
    }


        }*/

       /* switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastActionDownX = ev.getX();
                lastActionDownY = ev.getY();
                isInIgnoredView = isInIgnoredView(ev) && !isOpened();
                pressedState = PRESSED_DOWN;
                Log.d("MotionEvent", "ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                if (isInIgnoredView || isInDisableDirection(scaleDirection))
                    break;

                if (pressedState != PRESSED_DOWN &&
                        pressedState != PRESSED_MOVE_HORIZONTAL)
               k     break;

                int xOffset = (int) (ev.getX() - lastActionDownX);
                int yOffset = (int) (ev.getY() - lastActionDownY);

                if (pressedState == PRESSED_DOWN) {
                    if (yOffset > 25 || yOffset < -25) {
                        pressedState = PRESSED_MOVE_VERTICAL;
                        break;
                    }
                    if (xOffset < -50 || xOffset > 50) {
                        pressedState = PRESSED_MOVE_HORIZONTAL;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else if (pressedState == PRESSED_MOVE_HORIZONTAL) {
                    if (currentActivityScaleX < 0.95)
                        showScrollViewMenu(scrollViewMenu);

                    float targetScale = getTargetScale(ev.getRawX());
                    if (mUse3D) {
                        int angle = scaleDirection == DIRECTION_LEFT ? -ROTATE_Y_ANGLE : ROTATE_Y_ANGLE;
                        angle *= (1 - targetScale) * 2;
                        ViewHelper.setRotationY(viewActivity, angle);

                        ViewHelper.setScaleX(imageViewShadow, targetScale - shadowAdjustScaleX);
                        ViewHelper.setScaleY(imageViewShadow, targetScale - shadowAdjustScaleY);
                    } else {
                        ViewHelper.setScaleX(imageViewShadow, targetScale + shadowAdjustScaleX);
                        ViewHelper.setScaleY(imageViewShadow, targetScale + shadowAdjustScaleY);
                    }
                    ViewHelper.setScaleX(viewActivity, targetScale);
                    ViewHelper.setScaleY(viewActivity, targetScale);
                    ViewHelper.setAlpha(scrollViewMenu, (1 - targetScale) * 2.0f);

                    lastRawX = ev.getRawX();
                    return true;
                }
                Log.d("MotionEvent", "ACTION_MOVE");

                break;

            case MotionEvent.ACTION_UP:

                if (isInIgnoredView) break;
                if (pressedState != PRESSED_MOVE_HORIZONTAL) break;

                pressedState = PRESSED_DONE;
                if (isOpened()) {
                    if (currentActivityScaleX > 0.56f)
                        closeMenu();
                    else
                        openMenu(scaleDirection);
                } else {
                    if (currentActivityScaleX < 0.94f) {
                        openMenu(scaleDirection);
                    } else {
                        closeMenu();
                    }
                }
                Log.d("MotionEvent", "MotionEventACTION_UP");

                break;

        }
        lastRawX = ev.getRawX();*/

    public int getScreenHeight() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public int getScreenWidth() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void setScaleValue(float scaleValue) {
        this.mScaleValue = scaleValue;
    }

    public void setUse3D(boolean use3D) {
        mUse3D = use3D;
    }

    private void showScrollViewMenu(View scrollViewMenu) {
        if (scrollViewMenu != null && scrollViewMenu.getParent() == null) {
            addView(scrollViewMenu);
        }
    }

    private void hideScrollViewMenu(View scrollViewMenu) {
        if (scrollViewMenu != null && scrollViewMenu.getParent() != null) {
            removeView(scrollViewMenu);
        }
    }

    public void slideShadowLeft(Context homePageActivity, int anim) {
        imageViewShadow.startAnimation(AnimationUtils.loadAnimation(
                homePageActivity, anim
        ));
    }

    public void slideShadowRight(Context homePageActivity, int anim) {
        imageViewShadow.startAnimation(AnimationUtils.loadAnimation(
                homePageActivity, anim
        ));
    }

    public interface OnMenuListener {

        /**
         * This method will be called at the finished time of opening menu animations.
         */
        void openMenu();

        /**
         * This method will be called at the finished time of closing menu animations.
         */
        void closeMenu();
    }


    public interface OnMenuDublicateListener {

        /**
         * This method will be called at the finished time of opening menu animations.
         */
        void openDublicateMenu();

        /**
         * This method will be called at the finished time of closing menu animations.
         */
        void closeDublicateMenu();
    }







}
