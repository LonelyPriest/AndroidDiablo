package com.diablo.dt.diablo.utils;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by buxianhui on 17/4/18.
 */

public class DiabloVerticalButton extends AppCompatButton {
    public DiabloVerticalButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiabloVerticalButton(Context context) {
        super(context);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        // TODO Auto-generated method stub
        if ("".equals(text) || text == null || text.length() == 0) {
            return;
        }

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            // char c = text.charAt(i);
            // CharSequence index = text.toString().subSequence(i, i + 1);
            buffer.append(text.charAt(i));
            if (i < text.length() - 1) {
                buffer.append("\n");
            }
        }
        super.setText(buffer, type);
    }
}
