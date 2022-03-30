package com.starlang.datacollector.Sentence;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.ArrayList;

import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;

public class SentencePropbankPredicateView extends SentenceView{

    public SentencePropbankPredicateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.layerType = ViewLayerType.PROPBANK;
    }

    protected int getMaxLayerLength(AnnotatedWord word, Paint textPaint){
        int maxSize = getWidth(word.getName(), textPaint);
        if (word.getArgument() != null){
            int size = getWidth(word.getArgument().getArgumentType(), textPaint);
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
        if (word.getArgument() != null){
            String correct = word.getArgument().getArgumentType();
            canvas.drawText(correct, currentLeft, (lineIndex + 1) * lineSpace + 40, textPaint);
        }
    }

}
