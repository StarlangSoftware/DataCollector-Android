package com.starlang.datacollector.ParseTree;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.starlang.datacollector.DataCollectorActivity;
import com.starlang.datacollector.R;
import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseTreeDrawable;
import MorphologicalAnalysis.FsmParse;
import ParseTree.Symbol;
import WordNet.SynSet;

public class TreeActivity extends DataCollectorActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_annotator);
        connectTreeViews();
        baseFontSize = 8;
        fontMultiplier = 2;
        updateHeight();
        infoTop.setText("English");
        infoBottom.setText("Tree");
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (lineRead.length() > 0 && fileName.length() > 0) {
            ParseTreeDrawable parseTree = new ParseTreeDrawable(lineRead);
            if (parseTree.getRoot() != null){
                ((TreeView) view).initializeTree(parseTree, fileName);
                updateInfo();
            } else {
                showError("File " + fileName + " can not be opened");
            }
        }
    }

    protected void saveTree(){
        save("( " + ((TreeView) view).currentTree.toString() + " )\n");
    }

    public void showPopup(float x, float y){
        String previousText;
        super.showPopup(x, y);
        EditText editText = popupLayout.findViewById(R.id.editText);
        switch (view.layerType){
            default:
                previousText = ((TreeView) view).clickedNode.getData().getName();
                editText.setText(((TreeView) view).clickedNode.getData().getName());
                break;
            case TURKISH_WORD:
                previousText = ((TreeView) view).clickedNode.getLayerData(ViewLayerType.TURKISH_WORD);
                editText.setText(((TreeView) view).clickedNode.getLayerData(ViewLayerType.TURKISH_WORD));
                break;
        }
        Button save = popupLayout.findViewById(R.id.save);
        save.setOnClickListener(v -> {
            TreeView treeView = (TreeView) view;
            String newText = editText.getText().toString();
            if (!newText.equals(previousText)){
                switch (view.layerType){
                    default:
                        treeView.clickedNode.setData(new Symbol(newText));
                        break;
                    case TURKISH_WORD:
                        treeView.clickedNode.getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, newText);
                        break;
                }
                updateInfo();
            }
            switch (treeView.layerType){
                case INFLECTIONAL_GROUP:
                    if (list.getSelectedItem() != null){
                        FsmParse fsmParse = (FsmParse) list.getSelectedItem();
                        if (!treeView.clickedNode.getLayerData(ViewLayerType.INFLECTIONAL_GROUP).equals(fsmParse.toString())){
                            treeView.clickedNode.getLayerInfo().setMorphologicalAnalysis(fsmParse);
                            treeView.clickedNode.getLayerInfo().setLayerData(ViewLayerType.META_MORPHEME, fsmParse.withList());
                        }
                    }
                    break;
                case NER:
                    if (list.getSelectedItem() != null){
                        if (treeView.clickedNode.getLayerData(ViewLayerType.NER) == null || !treeView.clickedNode.getLayerData(ViewLayerType.NER).equals(list.getSelectedItem().toString())){
                            treeView.clickedNode.getLayerInfo().setLayerData(ViewLayerType.NER, list.getSelectedItem().toString());
                        }
                    }
                    break;
                case SEMANTICS:
                    if (list.getSelectedItem() != null){
                        SynSet synSet = (SynSet) list.getSelectedItem();
                        if (treeView.clickedNode.getLayerData(ViewLayerType.SEMANTICS) == null || !treeView.clickedNode.getLayerData(ViewLayerType.SEMANTICS).equals(synSet.getId())){
                            treeView.clickedNode.getLayerInfo().setLayerData(ViewLayerType.SEMANTICS, synSet.getId());
                        }
                    }
                    break;
                case PROPBANK:
                    if (list.getSelectedItem() != null){
                        treeView.clickedNode.getLayerInfo().setLayerData(ViewLayerType.PROPBANK, list.getSelectedItem().toString());
                    }
                    break;
            }
            saveTree();
            view.invalidate();
            popup.dismiss();
            popupOpen = false;
        });
        Button delete = popupLayout.findViewById(R.id.delete);
        delete.setVisibility(View.INVISIBLE);
    }

    public void updateInfo() {
        TreeView treeView = (TreeView) view;
        if (treeView.currentTree != null) {
            toolbar.setTitle(treeView.currentTree.getName());
            infoTop.setText(treeView.currentTree.toSentence());
            infoBottom.setText(treeView.currentTree.toSentence());
        }
        view.invalidate();
    }

}
