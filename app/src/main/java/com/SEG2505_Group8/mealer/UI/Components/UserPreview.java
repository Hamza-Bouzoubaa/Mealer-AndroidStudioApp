package com.SEG2505_Group8.mealer.UI.Components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.SEG2505_Group8.mealer.R;

/**
 * Preview of a user's info, typically displayed inside of a list.
 */
public class UserPreview extends ConstraintLayout {

    private TextView name;
    private TextView email;

    public UserPreview(Context context) {
        super(context);
        init(context, null, 0);
    }

    public UserPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public UserPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // Load attributes

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.user_preview, this);

        name = findViewById(R.id.user_preview_name);
        email = findViewById(R.id.user_preview_email);
    }
}