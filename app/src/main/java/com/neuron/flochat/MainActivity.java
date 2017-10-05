package com.neuron.flochat;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private ViewPager mPager;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;
    
    int NUM_PAGES = 3;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 4000; // time in milliseconds between successive task executions.


    private int[] mImageResources = {
            R.mipmap.img1,
            R.mipmap.img2,
            R.mipmap.img3,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPager = (ViewPager) findViewById(R.id.pager);

        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);

        mAdapter = new ViewPagerAdapter(MainActivity.this, mImageResources);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(this);
        setUiPageViewController();
        
        /*After setting the adapter use the timer */
        final Handler handler = new Handler() {
            @Override
            public void publish(LogRecord logRecord) {

            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        };
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        // This will do automatic transition
        timer = new Timer(); // This will create a new Thread 
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                pager_indicator.post(Update);
                try {

                    // For slow page transformer
                    Field mScroller;
                    mScroller = ViewPager.class.getDeclaredField("mScroller");
                    mScroller.setAccessible(true);
                    SmoothScroller scroller = new SmoothScroller(mPager.getContext());
                    // scroller.setFixedDuration(5000);
                    mScroller.set(mPager, scroller);
                } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {
                }
            }
        }, DELAY_MS, PERIOD_MS);
    }


    private void setUiPageViewController() {
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        // To disable swipe with finger
        mPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
