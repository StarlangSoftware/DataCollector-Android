package com.starlang.datacollector.Sentence;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.ArrayList;

import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;

public class SentenceMorphologicalAnalyzerView extends SentenceView{

    public SentenceMorphologicalAnalyzerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.layerType = ViewLayerType.INFLECTIONAL_GROUP;
    }

    protected void setLineSpace() {
        int maxSize = 1;
        for (int i = 0; i < sentence.wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
            if (word.getParse() != null && word.getParse().size() > maxSize){
                maxSize = word.getParse().size();
            }
        }
        lineSpace = 40 * (maxSize + 1);
    }

    protected int getMaxLayerLength(AnnotatedWord word, Paint textPaint){
        int maxSize = getWidth(word.getName(), textPaint);
        if (word.getParse() != null){
            for (int j = 0; j < word.getParse().size(); j++){
                int size = getWidth(word.getParse().getInflectionalGroupString(j), textPaint);
                if (size > maxSize){
                    maxSize = size;
                }
            }
        }
        return maxSize;
    }

    protected void drawLayer(AnnotatedWord word, Canvas canvas, Paint textPaint, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
        if (word.getParse() != null){
            for (int j = 0; j < word.getParse().size(); j++){
                canvas.drawText(word.getParse().getInflectionalGroupString(j), currentLeft, (lineIndex + 1) * lineSpace + 30 * (j + 1), textPaint);
            }
        }
    }

}
