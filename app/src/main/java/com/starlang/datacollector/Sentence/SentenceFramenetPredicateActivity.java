package com.starlang.datacollector.Sentence;

import android.os.Bundle;

import com.starlang.datacollector.R;

import java.util.ArrayList;

import AnnotatedSentence.AnnotatedWord;

public class SentenceFramenetPredicateActivity extends SentenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentence_framenet_predicate_annotator);
        connectSentenceViews();
        infoTop.setText("FrameNet Predicate");
        infoBottom.setText("Sentence");
    }

    protected ArrayList fillSpinner(){
        ArrayList<String> spinnerList = new ArrayList<>();
        AnnotatedWord word = ((SentenceView) view).clickedWord;
        String semantics = word.getSemantic();
        spinnerList.add("PREDICATE$NONE$" + semantics);
        spinnerList.add("NONE");
        return spinnerList;
    }

}
