package com.starlang.datacollector.ParseTree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.LayerItemNotExistsException;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.WordNotExistsException;

public class TreeMorphologicalAnalyzerView extends TreeView{

    public TreeMorphologicalAnalyzerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.layerType = ViewLayerType.INFLECTIONAL_GROUP;
    }

    protected int getStringSize(ParseNodeDrawable parseNode, Paint currentPaint){
        int i, stringSize = 0;
        if (parseNode.numberOfChildren() == 0) {
            if (parseNode.getLayerInfo().getLayerSize(ViewLayerType.PART_OF_SPEECH) == 0){
                return getWidth(parseNode.getLayerData(ViewLayerType.TURKISH_WORD), currentPaint);
            }
            for (i = 0; i < parseNode.getLayerInfo().getLayerSize(ViewLayerType.PART_OF_SPEECH); i++)
                try {
                    if (getWidth(parseNode.getLayerInfo().getLayerInfoAt(ViewLayerType.PART_OF_SPEECH, i), currentPaint) > stringSize){
                        stringSize = getWidth(parseNode.getLayerInfo().getLayerInfoAt(ViewLayerType.PART_OF_SPEECH, i), currentPaint);
                    }
                } catch (LayerNotExistsException | LayerItemNotExistsException | WordNotExistsException e) {
                    return getWidth(parseNode.getData().getName(), currentPaint);
                }
            return stringSize;
        } else {
            return getWidth(parseNode.getData().getName(), currentPaint);
        }
    }

    protected void drawString(ParseNodeDrawable parseNode, Canvas canvas, int x, int y, Paint textPaint){
        int i;
        if (parseNode.numberOfChildren() == 0){
            if (parseNode.getLayerInfo().getLayerSize(ViewLayerType.PART_OF_SPEECH) == 0){
                canvas.drawText(parseNode.getLayerData(ViewLayerType.TURKISH_WORD), x, y, textPaint);
            }
            for (i = 0; i < parseNode.getLayerInfo().getLayerSize(ViewLayerType.PART_OF_SPEECH); i++){
                if (i > 0 && !parseNode.isGuessed()){
                    textPaint.setColor(Color.RED);
                }
                try {
                    canvas.drawText(parseNode.getLayerInfo().getLayerInfoAt(ViewLayerType.PART_OF_SPEECH, i), x, y, textPaint);
                    y += 40;
                } catch (LayerNotExistsException | LayerItemNotExistsException | WordNotExistsException e) {
                    canvas.drawText(parseNode.getData().getName(), x, y, textPaint);
                }
            }
        } else {
            canvas.drawText(parseNode.getData().getName(), x, y, textPaint);
        }
    }

    protected void setArea(ParseNodeDrawable parseNode, int x, int y, int stringSize){
        if (parseNode.numberOfChildren() == 0){
            parseNode.setArea(x - 5, y - 15, stringSize + 10, 40 * (parseNode.getLayerInfo().getLayerSize(ViewLayerType.PART_OF_SPEECH) + 1));
        } else {
            parseNode.setArea(x - 5, y - 15, stringSize + 10, 40);
        }
    }

}
