package com.starlang.datacollector.Sentence;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.ArrayList;

import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;

public class SentencePosTaggerView extends SentenceView{

    public SentencePosTaggerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.layerType = ViewLayerType.POS_TAG;
    }

    protected int getMaxLayerLength(AnnotatedWord word, Paint textPaint){
        int maxSize = getWidth(word.getName(), textPaint);
        if (word.getPosTag() != null){
            int size = getWidth(word.getPosTag(), textPaint);
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
        if (word.getPosTag() != null){
            String correct = word.getPosTag();
            canvas.drawText(correct, currentLeft, (lineIndex + 1) * lineSpace + 40, textPaint);
        }
    }

}
