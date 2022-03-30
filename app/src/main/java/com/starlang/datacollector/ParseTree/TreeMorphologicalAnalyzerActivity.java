package com.starlang.datacollector.ParseTree;

import android.os.Bundle;
import com.starlang.datacollector.R;
import java.util.ArrayList;

import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;

public class TreeMorphologicalAnalyzerActivity extends TreeActivity{
    private FsmMorphologicalAnalyzer fsm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_morphology_annotator);
        connectTreeViews();
        fsm = new FsmMorphologicalAnalyzer();
        infoTop.setText("Morphology");
        infoBottom.setText("Tree");
    }

    protected ArrayList fillSpinner(){
        return fillMorphologicalAnalyzerSpinner(fsm, ((TreeView) view).clickedNode.getLayerData(ViewLayerType.TURKISH_WORD));
    }

}
