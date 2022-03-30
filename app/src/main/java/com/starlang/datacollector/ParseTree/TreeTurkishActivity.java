package com.starlang.datacollector.ParseTree;

import android.os.Bundle;
import com.starlang.datacollector.R;

public class TreeTurkishActivity extends TreeActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_turkish_annotator);
        connectTreeViews();
        infoTop.setText("Turkish");
        infoBottom.setText("Tree");
    }

}
