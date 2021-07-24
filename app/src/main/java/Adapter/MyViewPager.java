package Adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {
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

        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);  //false表示不拦截跑下试试

        //return super.onInterceptTouchEvent(ev);
    }
}
