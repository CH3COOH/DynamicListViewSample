package jp.ch3cooh.dynamiclistviewsample;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SampleCell extends LinearLayout {

    @InjectView(R.id.titleView)
    public TextView titleView;

    public SampleCell(Context context) {
        super(context);
    }

    public SampleCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SampleCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this, this);
    }
}
