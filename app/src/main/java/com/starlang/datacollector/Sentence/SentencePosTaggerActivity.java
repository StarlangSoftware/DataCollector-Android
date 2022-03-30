package com.starlang.datacollector.Sentence;

import android.os.Bundle;

import com.starlang.datacollector.R;

import java.util.ArrayList;

public class SentencePosTaggerActivity extends SentenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentence_postagger_annotator);
        connectSentenceViews();
        infoTop.setText("Pos Tagger");
        infoBottom.setText("Sentence");
    }

    protected ArrayList fillSpinner(){
        return fillPosTaggerSpinner();
    }

}
