package com.yabi.yabiuserandroid.ui.uiutils.palette;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.ui.uiutils.Typefaces;


public class CustomFontButton extends Button {

    public CustomFontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public CustomFontButton(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (!this.isInEditMode()) {
            if (attrs != null) {
                TypedArray a = getContext().obtainStyledAttributes(attrs,
                        R.styleable.CustomFontBTN);
                String fontName = a.getString(R.styleable.CustomFontBTN_fontName);
                if (fontName != null) {
                    setTypeface(Typefaces.get(getContext(), fontName));
                }
                a.recycle();
            }
        }
    }

}
