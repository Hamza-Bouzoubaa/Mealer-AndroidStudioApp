package com.SEG2505_Group8.mealer.UI.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.SEG2505_Group8.mealer.R;

public class ProgressiveHorizontalScrollView extends HorizontalScrollView implements ViewTreeObserver.OnScrollChangedListener {

    public ProgressiveHorizontalScrollView(Context context) {
        this(context, null);
    }

    public ProgressiveHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public ProgressiveHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, null, 0, 0);
    }

    public ProgressiveHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initXML(context);
    }

    @Override
    public void onScrollChanged() {
        getChildAt(getChildCount() - 1);

        //TODO: Get more content when scrolled to the end.

        if (getScrollX() <= 0) {
            Toast.makeText(getContext(), "Reached begining.", Toast.LENGTH_SHORT).show();
        } else if (getScrollX() >= getMaxScrollAmount()) {
            Toast.makeText(getContext(), "Reached end.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initXML(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.progressive_horizontal_scroll_view, this);

        getViewTreeObserver().addOnScrollChangedListener(this);
    }
}
