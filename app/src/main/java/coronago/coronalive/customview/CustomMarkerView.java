package coronago.coronalive.customview;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import coronago.coronalive.R;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;
    public CustomMarkerView (Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        MarginLayoutParams params = (MarginLayoutParams) tvContent.getLayoutParams();
        if(e.getX()==0)
        {
            params.leftMargin=30;
            params.rightMargin=0;
        }
        else{
            params.leftMargin=0;
            params.rightMargin=70;
        }


        tvContent.setText(Math.round(e.getY())+""); // set the entry-value as the display text
        super.refreshContent(e, highlight);

    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}