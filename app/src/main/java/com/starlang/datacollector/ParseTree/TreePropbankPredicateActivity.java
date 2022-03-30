package com.starlang.datacollector.ParseTree;

import android.os.Bundle;

import com.starlang.datacollector.R;

import java.util.ArrayList;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class TreePropbankPredicateActivity extends TreeActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_propbank_predicate_annotator);
        connectTreeViews();
        infoTop.setText("Propbank Predicate");
        infoBottom.setText("Tree");
    }

    protected ArrayList fillSpinner(){
        ArrayList<String> spinnerList = new ArrayList<>();
        ParseNodeDrawable parseNode = ((TreeView) view).clickedNode;
        String semantics = parseNode.getLayerData(ViewLayerType.SEMANTICS);
        spinnerList.add("PREDICATE$" + semantics);
        spinnerList.add("NONE");
        return spinnerList;
    }

}
