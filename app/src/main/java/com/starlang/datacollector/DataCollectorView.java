package com.starlang.datacollector;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ScrollView;

import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;

public class DataCollectorView extends ScrollView {

    public ViewLayerType layerType;
    public Typeface typeface;
    public int fontSize;

    public DataCollectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void next(int count){
    }

    protected void previous(int count){
    }

    protected int getWidth(String text, Paint textPaint){
        Rect textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        return textBounds.width();
    }

}
