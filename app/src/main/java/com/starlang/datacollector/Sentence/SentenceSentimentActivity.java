package com.starlang.datacollector.Sentence;

import android.os.Bundle;

import com.starlang.datacollector.R;

import java.util.ArrayList;

public class SentenceSentimentActivity extends SentenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentence_sentiment_annotator);
        connectSentenceViews();
        infoTop.setText("Sentiment");
        infoBottom.setText("Sentence");
    }

    protected ArrayList fillSpinner(){
        return fillSentimentSpinner();
    }

}
