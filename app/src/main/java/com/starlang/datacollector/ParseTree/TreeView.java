package com.starlang.datacollector.ParseTree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import com.starlang.datacollector.DataCollectorView;
import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;

public class TreeView extends DataCollectorView {

    public ParseTreeDrawable currentTree = null;
    protected int widthDecrease, heightDecrease;
    final int NODE_WIDTH = 90;
    final int NODE_HEIGHT = 120;
    protected int nodeWidth;
    protected int nodeHeight;
    private final ScaleGestureDetector scaleDetector;
    private double scaleFactor;
    protected TreeActivity context;
    protected ParseNodeDrawable clickedNode;

    public TreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = (TreeActivity) context;
        fontSize = 14;
        typeface = Typeface.SANS_SERIF;
        widthDecrease = 30;
        heightDecrease = 30;
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        scaleFactor = 1.0;
        nodeWidth = (int) (NODE_WIDTH * scaleFactor);
        nodeHeight = (int) (NODE_HEIGHT * scaleFactor);
        layerType = ViewLayerType.WORD;
    }

    public void initializeTree(ParseTreeDrawable parseTree, String name){
        this.currentTree = parseTree;
        this.currentTree.setName(name);
        invalidate();
    }

    protected void next(int count){
        currentTree.nextTree(count);
        invalidate();
    }

    protected void previous(int count){
        currentTree.previousTree(count);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (currentTree == null){
            return;
        }
        int newWidth, newHeight;
        newWidth = (int) ((currentTree.getMaxInOrderTraversalIndex() + 1.5) * nodeWidth);
        this.setMinimumWidth(newWidth + widthDecrease);
        switch (layerType){
            case INFLECTIONAL_GROUP:
                newHeight = (currentTree.maxDepth() + 1) * nodeHeight;
                this.setMinimumHeight(newHeight + heightDecrease);
                break;
            case NER:
            case SEMANTICS:
            case PROPBANK:
            case FRAMENET:
                newHeight = (int) ((currentTree.maxDepth() + 0.5) * nodeHeight);
                this.setMinimumHeight(newHeight + heightDecrease);
                break;
            default:
                newHeight = currentTree.maxDepth() * nodeHeight;
                this.setMinimumHeight(newHeight + heightDecrease);
        }
        paint(currentTree, canvas, nodeWidth, nodeHeight, layerType);
    }

    protected int getStringSize(ParseNodeDrawable parseNode, Paint currentPaint){
        return getWidth(parseNode.getData().getName(), currentPaint);
    }

    protected void drawString(ParseNodeDrawable parseNode, Canvas canvas, int x, int y, Paint textPaint){
        canvas.drawText(parseNode.getData().getName(), x, y, textPaint);
    }

    protected void setArea(ParseNodeDrawable parseNode, int x, int y, int stringSize){
        parseNode.setArea(x - 5, y - 15, stringSize + 10, 20);
    }

    protected void paint(ParseTreeDrawable parseTree, Canvas canvas, int nodeWidth, int nodeHeight, ViewLayerType viewLayer){
        paint(((ParseNodeDrawable)parseTree.getRoot()), canvas, nodeWidth, nodeHeight, parseTree.maxDepth(), viewLayer);
    }

    protected void drawRect(Canvas canvas, Paint currentPaint, int x, int y, int width, int height){
        canvas.drawRect(x, y, x + width, y + height, currentPaint);
    }

    public void paint(ParseNodeDrawable parseNode, Canvas canvas, int nodeWidth, int nodeHeight, int maxDepth, ViewLayerType viewLayer){
        int stringSize, addY, x, y;
        ViewLayerType originalLayer = viewLayer;
        if (parseNode.numberOfChildren() == 0 && viewLayer != ViewLayerType.WORD){
            viewLayer = parseNode.getLayerInfo().checkLayer(viewLayer);
        }
        Paint currentPaint = new Paint();
        Paint textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(fontSize * getResources().getDisplayMetrics().scaledDensity);
        textPaint.setTypeface(typeface);
        stringSize = getStringSize(parseNode, textPaint);
        if (parseNode.getDepth() == 0){
            addY = 15;
        } else {
            if (parseNode.getDepth() == maxDepth){
                addY = -5;
            } else {
                addY = 10;
            }
        }
        x = (parseNode.getInOrderTraversalIndex() + 1) * nodeWidth - stringSize / 2;
        y = parseNode.getDepth() * nodeHeight + addY + heightDecrease;
        setArea(parseNode, x, y, stringSize);
        if (parseNode.isSearched()){
            currentPaint.setColor(Color.BLUE);
            drawRect(canvas, currentPaint, x - 5, y - 15, stringSize + 10, 20);
            currentPaint.setColor(Color.BLACK);
        } else {
            if (parseNode.isEditable()){
                currentPaint.setColor(Color.RED);
                drawRect(canvas, currentPaint, x - 5,  y - 15, stringSize + 10, 20);
                currentPaint.setColor(Color.BLACK);
            } else {
                if (parseNode.isDragged()){
                    currentPaint.setColor(Color.MAGENTA);
                    if (parseNode.getSelectedIndex() == -1)
                        drawRect(canvas, currentPaint, x - 5,y - 15,stringSize + 10,20);
                    else {
                        if (originalLayer != ViewLayerType.TURKISH_WORD){
                            drawRect(canvas, currentPaint,x - 5,y - 15 + 20 * parseNode.getSelectedIndex(), stringSize + 10,20);
                        } else {
                            drawRect(canvas, currentPaint, x - 5 + parseNode.getSelectedIndex() * (stringSize + 10) / (parseNode.numberOfChildren() + 1),
                                    y - 15,
                                    (stringSize + 10) / (parseNode.numberOfChildren() + 1),
                                    20);
                        }
                    }
                    currentPaint.setColor(Color.BLACK);
                } else {
                    if (parseNode.isSelected()){
                        if (parseNode.getSelectedIndex() == -1){
                            drawRect(canvas, currentPaint, x - 5, y - 15, stringSize + 10, 20);
                        } else {
                            drawRect(canvas, currentPaint, x - 5, y - 15 + 20 * parseNode.getSelectedIndex(),stringSize + 10, 20);
                        }
                    }
                }
            }
        }
        if (parseNode.numberOfChildren() == 0){
            if (parseNode.isGuessed()){
                currentPaint.setColor(Color.MAGENTA);
            } else {
                if (originalLayer != viewLayer && (originalLayer == ViewLayerType.TURKISH_WORD || originalLayer == ViewLayerType.PERSIAN_WORD)){
                    currentPaint.setColor(Color.RED);
                } else {
                    currentPaint.setColor(Color.BLUE);
                }
            }
        } else {
            if (parseNode.getParent() != null && parseNode == parseNode.getParent().headChild()){
                currentPaint.setColor(Color.GRAY);
            }
        }
        drawString(parseNode, canvas, x, y, textPaint);
        currentPaint.setColor(Color.BLACK);
        for (int j = 0; j < parseNode.numberOfChildren(); j++) {
            ParseNodeDrawable aChild = (ParseNodeDrawable) parseNode.getChild(j);
            canvas.drawLine((parseNode.getInOrderTraversalIndex() + 1) * nodeWidth,
                    parseNode.getDepth() * nodeHeight + 20 + heightDecrease,
                    (aChild.getInOrderTraversalIndex() + 1) * nodeWidth,
                    aChild.getDepth() * nodeHeight - 20 + heightDecrease,
                    currentPaint);
        }
        for (int j = 0; j < parseNode.numberOfChildren(); j++) {
            ParseNodeDrawable aChild = (ParseNodeDrawable) parseNode.getChild(j);
            paint(aChild, canvas, nodeWidth, nodeHeight, maxDepth, viewLayer);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        clickedNode = currentTree.getLeafNodeAt((int) ev.getX(), (int) ev.getY());
        if (clickedNode != null && !context.popupOpen){
            context.popupOpen = true;
            context.showPopup(ev.getX(), ev.getY());
        } else {
            scaleDetector.onTouchEvent(ev);
        }
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float x = detector.getCurrentSpanX();
            float y = detector.getCurrentSpanY();
            scaleFactor *= detector.getScaleFactor();
            nodeWidth = (int) (NODE_WIDTH * (scaleFactor * (x / (x + y))));
            nodeHeight = (int) (NODE_HEIGHT * (scaleFactor * (y / (x + y))));
            invalidate();
            return true;
        }
    }
}
