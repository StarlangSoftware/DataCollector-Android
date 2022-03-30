package com.starlang.datacollector.Sentence;

import android.os.Bundle;

import com.starlang.datacollector.R;

import java.util.ArrayList;
import java.util.HashSet;

import PropBank.Frameset;
import PropBank.FramesetArgument;
import PropBank.FramesetList;

public class SentencePropbankArgumentActivity extends SentenceActivity{
    private FramesetList framesetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentence_propbank_argument_annotator);
        connectSentenceViews();
        framesetList = new FramesetList();
        infoTop.setText("Propbank Argument");
        infoBottom.setText("Sentence");
    }

    protected ArrayList fillSpinner(){
        ArrayList<String> spinnerList = new ArrayList<>();
        HashSet<Frameset> frameSets = ((SentenceView) view).sentence.getPredicateSynSets(framesetList);
        for (Frameset frameset : frameSets){
            for (FramesetArgument framesetArgument : frameset.getFramesetArguments()){
                spinnerList.add(framesetArgument.getArgumentType() + "$" + frameset.getId());
            }
        }
        spinnerList.add("NONE");
        return spinnerList;
    }

}
