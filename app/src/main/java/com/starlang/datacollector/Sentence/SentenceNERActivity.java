package com.starlang.datacollector.Sentence;

import android.os.Bundle;
import com.starlang.datacollector.R;
import java.util.ArrayList;

public class SentenceNERActivity extends SentenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentence_ner_annotator);
        connectSentenceViews();
        infoTop.setText("NER");
        infoBottom.setText("Sentence");
    }

    protected ArrayList fillSpinner(){
        return fillNERSpinner();
    }

}
