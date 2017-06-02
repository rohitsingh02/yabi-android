package com.yabi.yabiuserandroid.ui.uiutils.palette;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.ui.uiutils.Typefaces;


public class CustomSpacingTextView extends TextView {
    private String fontName;
    private CharSequence originalText = "";
    private float spacing = 0.0f;

    public CustomSpacingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomSpacingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public CustomSpacingTextView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomSpacingTextView(Context context, String fontName) {
        super(context);
        this.fontName = fontName;
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.CustomSpacingTV);
            fontName = a.getString(R.styleable.CustomSpacingTV_fontName);
            originalText = a.getString(R.styleable.CustomSpacingTV_fontText);
            spacing = a.getFloat(R.styleable.CustomSpacingTV_fontSpacing, spacing);
            if (fontName != null) {
                createFromAsset(context);
            }
            applySpacing();
            a.recycle();
        } else if (fontName != null) {
            createFromAsset(context);
        }
    }

    private void createFromAsset(Context context) {
        if(fontName!=null) {
            setTypeface(Typefaces.get(getContext(), fontName));
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        applySpacing();
    }

    @Override
    public CharSequence getText() {
        return originalText;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
        applySpacing();
    }

    private void applySpacing() {
        if (this == null || this.originalText == null) return;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < originalText.length(); i++) {
            builder.append(originalText.charAt(i));
            if (i + 1 < originalText.length()) {
                builder.append("\u00A0");
            }
        }
        SpannableString finalText = new SpannableString(builder.toString());
        if (builder.toString().length() > 1) {
            for (int i = 1; i < builder.toString().length(); i += 2) {
                finalText.setSpan(new ScaleXSpan((spacing + 1) / 10), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        super.setText(finalText, BufferType.SPANNABLE);
    }

}
