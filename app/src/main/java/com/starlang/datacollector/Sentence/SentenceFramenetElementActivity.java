package com.starlang.datacollector.Sentence;

import android.os.Bundle;

import com.starlang.datacollector.R;

import java.util.ArrayList;

import FrameNet.FrameNet;
import FrameNet.DisplayedFrame;

public class SentenceFramenetElementActivity extends SentenceActivity{
    private FrameNet frameNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentence_framenet_element_annotator);
        connectSentenceViews();
        frameNet = new FrameNet();
        infoTop.setText("FrameNet Element");
        infoBottom.setText("Sentence");
    }

    protected ArrayList fillSpinner(){
        ArrayList<String> spinnerList = new ArrayList<>();
        ArrayList<DisplayedFrame> currentFrames = ((SentenceView) view).sentence.getFrames(frameNet);
        for (DisplayedFrame frame : currentFrames){
            for (int i = 0; i < frame.getFrame().frameElementSize(); i++){
                String frameElement = frame.getFrame().getFrameElement(i);
                spinnerList.add(frameElement + "$" + frame.toString() + "$" + frame.getLexicalUnit());
            }
        }
        spinnerList.add("NONE");
        return spinnerList;
    }

}
