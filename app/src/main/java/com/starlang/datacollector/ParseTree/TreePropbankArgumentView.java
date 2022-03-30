package com.starlang.datacollector.ParseTree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class TreePropbankArgumentView extends TreeView {

    public TreePropbankArgumentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.layerType = ViewLayerType.PROPBANK;
    }

    protected int getStringSize(ParseNodeDrawable parseNode, Paint currentPaint){
        if (parseNode.numberOfChildren() == 0) {
            if (parseNode.getLayerInfo().getArgument() != null){
                return getWidth(parseNode.getLayerInfo().getArgument().getArgumentType(), currentPaint);
            } else {
                return getWidth(parseNode.getData().getName(), currentPaint);
            }
        } else {
            return getWidth(parseNode.getData().getName(), currentPaint);
        }
    }

    protected void drawString(ParseNodeDrawable parseNode, Canvas canvas, int x, int y, Paint textPaint){
        if (parseNode.numberOfChildren() == 0){
            canvas.drawText(parseNode.getLayerData(ViewLayerType.TURKISH_WORD), x, y, textPaint);
            textPaint.setColor(Color.RED);
            y += 40;
            if (parseNode.getLayerInfo().getArgument() != null){
                canvas.drawText(parseNode.getLayerInfo().getArgument().getArgumentType(), x, y, textPaint);
                if (parseNode.getLayerInfo().getArgument().getId() != null){
                    canvas.drawText(parseNode.getLayerInfo().getArgument().getId(), x - 15, y + 40, textPaint);
                }
            }
        } else {
            canvas.drawText(parseNode.getData().getName(), x, y, textPaint);
        }
    }

    protected void setArea(ParseNodeDrawable parseNode, int x, int y, int stringSize){
        parseNode.setArea(x - 5, y - 15, stringSize + 10, 20);
    }

}
