package com.starlang.datacollector.Sentence;

import android.os.Bundle;
import com.starlang.datacollector.R;
import java.util.ArrayList;

import MorphologicalAnalysis.FsmMorphologicalAnalyzer;

public class SentenceMorphologicalAnalyzerActivity extends SentenceActivity{
    private FsmMorphologicalAnalyzer fsm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentence_morphology_annotator);
        connectSentenceViews();
        fsm = new FsmMorphologicalAnalyzer();
        infoTop.setText("Morphology");
        infoBottom.setText("Sentence");
    }

    protected ArrayList fillSpinner(){
        return fillMorphologicalAnalyzerSpinner(fsm, ((SentenceView) view).clickedWord.getName());
    }

}
