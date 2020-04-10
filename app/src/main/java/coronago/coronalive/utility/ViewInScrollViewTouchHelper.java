package coronago.coronalive.utility;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.core.widget.NestedScrollView;

public class ViewInScrollViewTouchHelper implements View.OnTouchListener {

    private final NestedScrollView scrollView;
    private final View viewInScrollView;
    int dragthreshold = 130;
    int downX;
    int downY;

    public ViewInScrollViewTouchHelper(View viewInScrollView) {

        if (viewInScrollView == null) {
            throw new IllegalArgumentException("viewInScrollView cannot be null.");
        }

        ViewParent parent = viewInScrollView.getParent();
        NestedScrollView scrollView = null;
        do {
            if (parent instanceof NestedScrollView) {
                scrollView = (NestedScrollView) parent;
                break;
            }
        } while(parent != null && (parent = parent.getParent()) != null);

        if (scrollView == null) {
            throw new IllegalArgumentException("View does not have a ScrollView in its parent hierarchy.");
        }

        this.scrollView = scrollView;
        this.viewInScrollView = viewInScrollView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = Math.abs((int) event.getRawX() - downX);
                int distanceY = Math.abs((int) event.getRawY() - downY);
              //  Log.d("distance","X= "+ distanceX + " y= "+distanceY);


                if (distanceY > distanceX && distanceY > dragthreshold) {
                    viewInScrollView.getParent().requestDisallowInterceptTouchEvent(false);
                    scrollView.getParent().requestDisallowInterceptTouchEvent(true);
                } else if (distanceX > distanceY && distanceX > dragthreshold) {
                    viewInScrollView.getParent().requestDisallowInterceptTouchEvent(true);
                    scrollView.getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                scrollView.getParent().requestDisallowInterceptTouchEvent(false);
                viewInScrollView.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return false;
    }
}