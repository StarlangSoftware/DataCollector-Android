package com.starlang.datacollector.Sentence;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.ArrayList;

import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;
import WordNet.SynSet;
import WordNet.WordNet;

public class SentenceSemanticView extends SentenceView{

    public SentenceSemanticView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.layerType = ViewLayerType.SEMANTICS;
    }

    protected void setLineSpace() {
        lineSpace = fontSize * 12;
    }

    protected int getMaxLayerLength(AnnotatedWord word, Paint textPaint){
        int maxSize = getWidth(word.getName(), textPaint);
        if (word.getSemantic() != null){
            int size = getWidth(word.getSemantic(), textPaint);
            if (size > maxSize){
                maxSize = size;
            }
        }
        return maxSize;
    }

    protected void drawLayer(AnnotatedWord word, Canvas canvas, Paint textPaint, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
        if (word.getSemantic() != null){
            String correct = word.getSemantic();
            canvas.drawText(correct, currentLeft, (lineIndex + 1) * lineSpace + 40, textPaint);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (sentence == null) {
            return;
        }
        WordNet wordNet = ((SentenceSemanticActivity) context).wordNet;
        int lineIndex = 0, currentLeft = wordSpace, multiple = 1;
        String current;
        AnnotatedWord word;
        Paint currentPaint = new Paint();
        currentPaint.setColor(Color.BLUE);
        for (int i = 0; i < sentence.wordCount(); i++){
            word = (AnnotatedWord) sentence.getWord(i);
            int maxSize = getMaxLayerLength(word, currentPaint);
            if (maxSize + currentLeft >= getWidth()){
                lineIndex++;
                currentLeft = wordSpace;
                multiple = 1;
            }
            multiple--;
            if (word.getSemantic() != null && multiple == 0) {
                SynSet synSet = wordNet.getSynSetWithId(word.getSemantic());
                if (synSet != null){
                    multiple = 1;
                    if (i + 1 < sentence.wordCount()){
                        AnnotatedWord next = (AnnotatedWord) sentence.getWord(i + 1);
                        if (next.getSemantic() != null && synSet.equals(wordNet.getSynSetWithId(next.getSemantic()))){
                            multiple = 2;
                        }
                    }
                    if (i + 2 < sentence.wordCount()){
                        AnnotatedWord twoNext = (AnnotatedWord) sentence.getWord(i + 2);
                        if (twoNext.getSemantic() != null && multiple == 2 && synSet.equals(wordNet.getSynSetWithId(twoNext.getSemantic()))){
                            multiple = 3;
                        }
                    }
                    if (i + 3 < sentence.wordCount()){
                        AnnotatedWord threeNext = (AnnotatedWord) sentence.getWord(i + 3);
                        if (threeNext.getSemantic() != null && multiple == 3 && synSet.equals(wordNet.getSynSetWithId(threeNext.getSemantic()))){
                            multiple = 4;
                        }
                    }
                    if (i + 4 < sentence.wordCount()){
                        AnnotatedWord fourNext = (AnnotatedWord) sentence.getWord(i + 4);
                        if (fourNext.getSemantic() != null && multiple == 4 && synSet.equals(wordNet.getSynSetWithId(fourNext.getSemantic()))){
                            multiple = 5;
                        }
                    }
                    if (synSet.getDefinition() != null){
                        if (synSet.getDefinition().length() < 24 + (multiple - 1) * 35){
                            current = synSet.getDefinition();
                        } else {
                            current = synSet.getDefinition().substring(0, 24 + (multiple - 1) * 35);
                        }
                        canvas.drawText(current, currentLeft, (lineIndex + 1) * lineSpace + 50, currentPaint);
                    }
                }
            } else {
                if (word.getSemantic() == null){
                    multiple = 1;
                }
            }
            currentLeft += maxSize + wordSpace;
        }
    }
}
