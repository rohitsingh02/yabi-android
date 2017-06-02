package com.yabi.yabiuserandroid.ui.uiutils.palette;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.ui.uiutils.Typefaces;


public class CustomFontEditTextView extends EditText {

    public CustomFontEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomFontEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public CustomFontEditTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.CustomFontET);
            String fontName = a.getString(R.styleable.CustomFontET_fontName);
            if (fontName != null) {
                setTypeface(Typefaces.get(getContext(), fontName));
            }
            a.recycle();
        }
    }

}
