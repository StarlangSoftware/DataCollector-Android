package com.starlang.datacollector.ParseTree;

import android.os.Bundle;

import com.starlang.datacollector.R;

import java.util.ArrayList;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.LayerInfo;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.WordNotExistsException;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import WordNet.WordNet;
import WordNet.SynSet;

public class TreeSemanticActivity extends TreeActivity{
    private FsmMorphologicalAnalyzer fsm;
    private WordNet wordNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_semantic_annotator);
        connectTreeViews();
        infoTop.setText("Semantic");
        infoBottom.setText("Tree");
        //wordNet = new WordNet();
        fsm = new FsmMorphologicalAnalyzer();
    }

    protected ArrayList fillSpinner() {
        ArrayList<SynSet> spinnerList = new ArrayList<SynSet>();
        LayerInfo info = ((TreeView) view).clickedNode.getLayerInfo();
        if (info.getLayerData(ViewLayerType.INFLECTIONAL_GROUP) != null) {
            try {
                ArrayList<SynSet> synSets = wordNet.constructSynSets(info.getMorphologicalParseAt(0).getWord().getName(), info.getMorphologicalParseAt(0), info.getMetamorphicParseAt(0), fsm);
                spinnerList.addAll(synSets);
            } catch (LayerNotExistsException | WordNotExistsException e) {
                e.printStackTrace();
            }
        }
        ParseNodeDrawable previous = ((TreeView) view).currentTree.previousLeafNode(((TreeView) view).clickedNode);
        if (previous != null){
            ArrayList<SynSet> idioms1 = new ArrayList<SynSet>();
            try {
                idioms1 = wordNet.constructIdiomSynSets(previous.getLayerInfo().getMorphologicalParseAt(0), info.getMorphologicalParseAt(0), previous.getLayerInfo().getMetamorphicParseAt(0), info.getMetamorphicParseAt(0), fsm);
            } catch (LayerNotExistsException | WordNotExistsException e) {
                e.printStackTrace();
            }
            spinnerList.addAll(idioms1);
        }
        ParseNodeDrawable next = ((TreeView) view).currentTree.nextLeafNode(((TreeView) view).clickedNode);
        if (next != null){
            ArrayList<SynSet> idioms2 = new ArrayList<SynSet>();
            try {
                idioms2 = wordNet.constructIdiomSynSets(info.getMorphologicalParseAt(0), next.getLayerInfo().getMorphologicalParseAt(0), info.getMetamorphicParseAt(0), next.getLayerInfo().getMetamorphicParseAt(0), fsm);
            } catch (LayerNotExistsException | WordNotExistsException e) {
                e.printStackTrace();
            }
            spinnerList.addAll(idioms2);
        }
        return spinnerList;
    }

}
