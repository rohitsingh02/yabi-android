package com.yabi.yabiuserandroid.ui.uiutils.palette;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.ui.uiutils.Typefaces;


public class CustomFontTextView extends TextView {
    private String fontName;

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public CustomFontTextView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomFontTextView(Context context, String fontName) {
        super(context);
        this.fontName = fontName;
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (!this.isInEditMode()) {
//            this.setTypeface(Typeface.createFromAsset(context.getAssets(),context.getString(R.string.path_assets_fonts) + context.getString(R.string.font_avenir_medium)));

            if (attrs != null) {
                TypedArray a = getContext().obtainStyledAttributes(attrs,
                        R.styleable.CustomFontTV);
                fontName = a.getString(R.styleable.CustomFontTV_fontName);
                if (fontName != null) {
                    createFromAsset(context);
                }
                a.recycle();
            } else if (fontName != null) {
                createFromAsset(context);
            }
        }
    }

    private void createFromAsset(Context context) {
        if(fontName!=null) {
            setTypeface(Typefaces.get(context, fontName));
        }
    }
}
