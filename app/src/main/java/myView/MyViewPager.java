package myView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {
    private float startX;
    private float startY;

    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        System.out.println(ev.getAction());
        final int action = ev.getAction();
//        if(action==MotionEvent.ACTION_MOVE){
//            return true;
//        }
            return false;
        //return super.onInterceptTouchEvent(ev);
    }

}
