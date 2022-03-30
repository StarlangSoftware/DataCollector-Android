package com.starlang.datacollector.ParseTree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.WordNotExistsException;

public class TreeSemanticView extends TreeView{

    public TreeSemanticView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.layerType = ViewLayerType.SEMANTICS;
    }

    protected int getStringSize(ParseNodeDrawable parseNode, Paint currentPaint){
        int i, stringSize = 0;
        if (parseNode.numberOfChildren() == 0) {
            try {
                stringSize = getWidth(parseNode.getLayerData(ViewLayerType.TURKISH_WORD), currentPaint);
                for (i = 0; i < parseNode.getLayerInfo().getNumberOfMeanings(); i++)
                    if (getWidth(parseNode.getLayerInfo().getSemanticAt(i).substring(6), currentPaint) > stringSize){
                        stringSize = getWidth(parseNode.getLayerInfo().getSemanticAt(i).substring(6), currentPaint);
                    }
            } catch (LayerNotExistsException | WordNotExistsException e) {
                e.printStackTrace();
            }
            return stringSize;
        } else {
            return getWidth(parseNode.getData().getName(), currentPaint);
        }
    }

    protected void drawString(ParseNodeDrawable parseNode, Canvas canvas, int x, int y, Paint textPaint){
        int i;
        if (parseNode.numberOfChildren() == 0){
            canvas.drawText(parseNode.getLayerData(ViewLayerType.TURKISH_WORD), x, y, textPaint);
            textPaint.setColor(Color.RED);
            for (i = 0; i < parseNode.getLayerInfo().getNumberOfMeanings(); i++){
                try {
                    y += 40;
                    canvas.drawText(parseNode.getLayerInfo().getSemanticAt(i).substring(6), x, y, textPaint);
                } catch (LayerNotExistsException | WordNotExistsException e) {
                    e.printStackTrace();
                }
            }
        } else {
            canvas.drawText(parseNode.getData().getName(), x, y, textPaint);
        }
    }

    protected void setArea(ParseNodeDrawable parseNode, int x, int y, int stringSize){
        if (parseNode.numberOfChildren() == 0){
            try {
                parseNode.setArea(x - 5, y - 15, stringSize + 10, 20 * (parseNode.getLayerInfo().getNumberOfWords() + 1));
            } catch (LayerNotExistsException e) {
                e.printStackTrace();
            }
        } else {
            parseNode.setArea(x - 5, y - 15, stringSize + 10, 20);
        }
    }

}
