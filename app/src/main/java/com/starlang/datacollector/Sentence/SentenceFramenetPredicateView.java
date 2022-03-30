package com.starlang.datacollector.Sentence;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.ArrayList;

import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;

public class SentenceFramenetPredicateView extends SentenceView{

    public SentenceFramenetPredicateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.layerType = ViewLayerType.FRAMENET;
    }

    protected int getMaxLayerLength(AnnotatedWord word, Paint textPaint){
        int maxSize = getWidth(word.getName(), textPaint);
        if (word.getFrameElement() != null){
            int size = getWidth(word.getFrameElement().getFrameElementType(), textPaint);
            if (size > maxSize){
                maxSize = size;
            }
        }
        return maxSize;
    }

    protected void setLineSpace() {
        lineSpace = fontSize * 12;
    }

    protected void drawLayer(AnnotatedWord word, Canvas canvas, Paint textPaint, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
        if (word.getFrameElement() != null){
            String correct = word.getFrameElement().getFrameElementType();
            canvas.drawText(correct, currentLeft, (lineIndex + 1) * lineSpace + 40, textPaint);
        }
    }

}
