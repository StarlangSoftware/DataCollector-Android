package com.starlang.datacollector.Sentence;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.starlang.datacollector.DataCollectorView;
import java.io.File;
import java.util.ArrayList;
import AnnotatedSentence.ViewLayerType;
import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.AnnotatedSentence;
import Corpus.FileDescription;

public class SentenceView extends DataCollectorView {

    protected AnnotatedSentence sentence = null;
    protected FileDescription fileDescription;
    protected int wordSpace = 60, lineSpace;
    protected int selectedWordIndex = -1, draggedWordIndex = -1;
    protected boolean selectionMode = false;
    protected AnnotatedWord clickedWord = null, lastClickedWord = null;
    protected SentenceActivity context;

    public SentenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = (SentenceActivity) context;
        fontSize = 14;
        typeface = Typeface.SANS_SERIF;
        this.layerType = ViewLayerType.WORD;
    }

    public void initializeSentence(AnnotatedSentence sentence, FileDescription fileDescription){
        this.sentence = sentence;
        this.fileDescription = fileDescription;
        invalidate();
    }

    protected int getMaxLayerLength(AnnotatedWord word, Paint textPaint){
        return getWidth(word.getName(), textPaint);
    }

    protected void setLineSpace() {
        lineSpace = fontSize * 6;
    }

    protected void drawLayer(AnnotatedWord word, Canvas canvas, Paint textPaint, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (sentence == null){
            return;
        }
        AnnotatedWord previousWord = null, word;
        int lineIndex, currentLeft = wordSpace, maxSize;
        ArrayList<Integer> wordSize = new ArrayList<>();
        ArrayList<Integer> wordTotal = new ArrayList<>();
        Paint currentPaint = new Paint();
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setTextSize(fontSize * getResources().getDisplayMetrics().scaledDensity);
        currentPaint.setTypeface(typeface);
        Paint textPaint = currentPaint;
        if (layerType == ViewLayerType.DEPENDENCY){
            for (int i = 0; i < sentence.wordCount(); i++) {
                wordTotal.add(currentLeft);
                word = (AnnotatedWord) sentence.getWord(i);
                maxSize = getMaxLayerLength(word, textPaint);
                wordSize.add(maxSize);
                currentLeft += maxSize + wordSpace;
            }
            lineIndex = 1;
        } else {
            lineIndex = 0;
        }
        boolean bold = false;
        setLineSpace();
        currentLeft = wordSpace;
        for (int i = 0; i < sentence.wordCount(); i++){
            if (i > 0){
                previousWord = (AnnotatedWord) sentence.getWord(i - 1);
            }
            word = (AnnotatedWord) sentence.getWord(i);
            maxSize = getMaxLayerLength(word, textPaint);
            Rect textBounds = new Rect();
            textPaint.getTextBounds(word.getName(), 0, word.getName().length(), textBounds);
            if (maxSize + currentLeft >= getWidth() && layerType != ViewLayerType.DEPENDENCY){
                lineIndex++;
                currentLeft = wordSpace;
            }
            currentPaint.setColor(Color.BLACK);
            if (layerType == ViewLayerType.PROPBANK){
                if (word.getShallowParse() != null && previousWord != null && previousWord.getShallowParse() != null){
                    if (!previousWord.getShallowParse().equals(word.getShallowParse())){
                        bold = !bold;
                    }
                }
                if (bold){
                    currentPaint.setStrokeWidth(2);
                } else {
                    currentPaint.setStrokeWidth(1);
                }
            }
            canvas.drawText(word.getName(), currentLeft, (lineIndex + 1) * lineSpace, textPaint);
            Rect currentArea = new Rect(currentLeft - 5, ((lineIndex + 1) * lineSpace - textBounds.height()), currentLeft - 5 + textBounds.width() + 10, ((lineIndex + 1) * lineSpace - textBounds.height()) + (int) (1.5 * textBounds.height()));
            word.setArea(currentLeft - 5, ((lineIndex + 1) * lineSpace - textBounds.height()), textBounds.width() + 10, (int) (1.5 * textBounds.height()));
            if (word.isSelected()){
                currentPaint.setColor(Color.BLUE);
                canvas.drawRect(currentArea, currentPaint);
            }
            currentPaint.setColor(Color.RED);
            drawLayer(word, canvas, textPaint, currentLeft, lineIndex, i, maxSize, wordSize, wordTotal);
            currentLeft += maxSize + wordSpace;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && sentence != null){
            float x = event.getRawX();
            float y = event.getRawY();
            int baseCoordinates[] = new int[2];
            getLocationInWindow(baseCoordinates);
            for (int i = 0; i < sentence.wordCount(); i++){
                if (((AnnotatedWord) sentence.getWord(i)).contains((int) x, (int) y - baseCoordinates[1])){
                    selectedWordIndex = i;
                    clickedWord = (AnnotatedWord) sentence.getWord(i);
                    context.showPopup(x, y);
                    break;
                }
            }
        }
        return true;
    }

    public void next(int count) {
        if (fileDescription != null && fileDescription.nextFileExists(count)){
            fileDescription.addToIndex(count);
            sentence = new AnnotatedSentence(new File(fileDescription.getFileName()));
            invalidate();
        }
    }

    public void previous(int count) {
        if (fileDescription != null && fileDescription.previousFileExists(count)){
            fileDescription.addToIndex(-count);
            sentence = new AnnotatedSentence(new File(fileDescription.getFileName()));
            invalidate();
        }
    }
}
