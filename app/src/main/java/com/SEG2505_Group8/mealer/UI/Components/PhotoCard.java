package com.SEG2505_Group8.mealer.UI.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SEG2505_Group8.mealer.R;

/**
 * Display a rounded image with subtitle.
 */
public class PhotoCard extends LinearLayout {

    private ImageView image;
    private TextView label;

    private final OnClickListener onImageClick = (View view) -> {
        Toast.makeText(view.getContext(), "Clicked on " + label.getText(), Toast.LENGTH_SHORT).show();
    };

    public PhotoCard(Context context) {
        super(context);
        init(null, 0);
        initXML(context);
    }

    public PhotoCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
        initXML(context);
    }

    public PhotoCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
        initXML(context);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
//        final TypedArray a = getContext().obtainStyledAttributes(
//                attrs, R.styleable.PhotoCard, defStyle, 0);

//        mExampleString = a.getString(
//                R.styleable.PhotoCard_exampleString);
//        mExampleColor = a.getColor(
//                R.styleable.PhotoCard_exampleColor,
//                mExampleColor);
    }

    private void initXML(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.photo_card, this);

        image = (ImageView) findViewById(R.id.photo_card_image);
        label = (TextView) findViewById(R.id.photo_card_label);

        image.setOnClickListener(onImageClick);
    }
}