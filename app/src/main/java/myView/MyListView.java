package myView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class MyListView extends ListView {

    private float startX;
    private float startY;
    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = ev.getRawX();
                startY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //判断是左右滑动还是上下滑动
                float endX = ev.getRawX();
                float endY = ev.getRawY();
                if (Math.abs(endX - startX) > Math.abs(endY - startY) ){
                    //如果是左右滑动，请求父控件拦截自己的
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else {
                    //如果是上下滑动，拦截设置为true
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
}
