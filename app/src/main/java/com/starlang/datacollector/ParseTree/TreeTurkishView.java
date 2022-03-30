package com.starlang.datacollector.ParseTree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class TreeTurkishView extends TreeView{

    public TreeTurkishView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.layerType = ViewLayerType.TURKISH_WORD;
    }

    protected int getStringSize(ParseNodeDrawable parseNode, Paint currentPaint){
        if (parseNode.numberOfChildren() != 0){
            return getWidth(parseNode.getData().getName(), currentPaint);
        } else {
            return getWidth(parseNode.getLayerData(ViewLayerType.TURKISH_WORD), currentPaint);
        }
    }

    protected void drawString(ParseNodeDrawable parseNode, Canvas canvas, int x, int y, Paint textPaint){
        if (parseNode.numberOfChildren() != 0){
            canvas.drawText(parseNode.getData().getName(), x, y, textPaint);
        } else {
            canvas.drawText(parseNode.getLayerData(ViewLayerType.TURKISH_WORD), x, y, textPaint);
        }
    }

}
