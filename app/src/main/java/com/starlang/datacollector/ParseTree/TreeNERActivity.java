package com.starlang.datacollector.ParseTree;

import android.os.Bundle;
import com.starlang.datacollector.R;
import java.util.ArrayList;

public class TreeNERActivity extends TreeActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_ner_annotator);
        connectTreeViews();
        infoTop.setText("NER");
        infoBottom.setText("Tree");
    }

    protected ArrayList fillSpinner(){
        return fillNERSpinner();
    }

}
