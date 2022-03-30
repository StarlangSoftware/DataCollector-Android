package com.starlang.datacollector.ParseTree;

import android.os.Bundle;

import com.starlang.datacollector.R;

import java.util.ArrayList;
import java.util.HashSet;

import PropBank.Frameset;
import PropBank.FramesetArgument;
import PropBank.FramesetList;

public class TreePropbankArgumentActivity extends TreeActivity{
    private FramesetList framesetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_propbank_argument_annotator);
        connectTreeViews();
        framesetList = new FramesetList();
        infoTop.setText("Propbank Argument");
        infoBottom.setText("Tree");
    }

    protected ArrayList fillSpinner(){
        ArrayList<String> spinnerList = new ArrayList<>();
        HashSet<Frameset> frameSets = ((TreeView) view).currentTree.getPredicateSynSets(framesetList);
        for (Frameset frameset : frameSets){
            for (FramesetArgument framesetArgument : frameset.getFramesetArguments()){
                spinnerList.add(framesetArgument.getArgumentType() + "$" + frameset.getId());
            }
        }
        spinnerList.add("NONE");
        return spinnerList;
    }

}
