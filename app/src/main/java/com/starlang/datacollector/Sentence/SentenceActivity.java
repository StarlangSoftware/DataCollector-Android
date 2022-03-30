package com.starlang.datacollector.Sentence;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.starlang.datacollector.DataCollectorActivity;
import com.starlang.datacollector.R;
import AnnotatedSentence.AnnotatedSentence;
import Corpus.FileDescription;
import MorphologicalAnalysis.FsmParse;
import WordNet.SynSet;

public class SentenceActivity extends DataCollectorActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentence_annotator);
        connectSentenceViews();
        baseFontSize = 10;
        fontMultiplier = 4;
        updateHeight();
        infoTop.setText("Turkish/English");
        infoBottom.setText("Sentence");
    }

    protected void saveSentence(){
        save(((SentenceView) view).sentence.toString() + "\n");
    }

    public void showPopup(float x, float y){
        super.showPopup(x, y);
        EditText editText = popupLayout.findViewById(R.id.editText);
        String previousText = ((SentenceView) view).clickedWord.getName();
        editText.setText(((SentenceView) view).clickedWord.getName());
        Button save = popupLayout.findViewById(R.id.save);
        save.setOnClickListener(v -> {
            SentenceView sentenceView = (SentenceView) view;
            String newText = editText.getText().toString();
            if (!newText.equals(previousText)){
                if (!newText.contains(" ")){
                    sentenceView.clickedWord.setName(newText);
                } else {
                    sentenceView.sentence.insertWord(newText, sentenceView.clickedWord, sentenceView.selectedWordIndex);
                }
                updateInfo();
            }
            switch (sentenceView.layerType){
                case INFLECTIONAL_GROUP:
                    if (list.getSelectedItem() != null){
                        FsmParse fsmParse = (FsmParse) list.getSelectedItem();
                        if (!sentenceView.clickedWord.getParse().toString().equals(fsmParse.toString())){
                            sentenceView.clickedWord.setParse(fsmParse.toString());
                            sentenceView.clickedWord.setMetamorphicParse(fsmParse.withList());
                        }
                    }
                    break;
                case NER:
                    if (list.getSelectedItem() != null){
                        if (sentenceView.clickedWord.getNamedEntityType() == null || !sentenceView.clickedWord.getNamedEntityType().toString().equals(list.getSelectedItem().toString())){
                            sentenceView.clickedWord.setNamedEntityType(list.getSelectedItem().toString());
                        }
                    }
                    break;
                case POS_TAG:
                    if (list.getSelectedItem() != null){
                        if (sentenceView.clickedWord.getPosTag() == null || !sentenceView.clickedWord.getPosTag().equals(list.getSelectedItem().toString())){
                            sentenceView.clickedWord.setPosTag(list.getSelectedItem().toString());
                        }
                    }
                    break;
                case POLARITY:
                    if (list.getSelectedItem() != null){
                        if (sentenceView.clickedWord.getPolarity() == null || !sentenceView.clickedWord.getPolarityString().equals(list.getSelectedItem().toString())){
                            sentenceView.clickedWord.setPolarity(list.getSelectedItem().toString());
                        }
                    }
                    break;
                case SEMANTICS:
                    if (list.getSelectedItem() != null){
                        SynSet synSet = (SynSet) list.getSelectedItem();
                        if (sentenceView.clickedWord.getSemantic() == null || !sentenceView.clickedWord.getSemantic().equals(synSet.getId())){
                            sentenceView.clickedWord.setSemantic(synSet.getId());
                        }
                    }
                    break;
                case FRAMENET:
                    if (list.getSelectedItem() != null){
                        if (sentenceView.clickedWord.getFrameElement() == null || !sentenceView.clickedWord.getFrameElement().toString().equals(list.getSelectedItem().toString())){
                            sentenceView.clickedWord.setFrameElement(list.getSelectedItem().toString());
                        }
                    }
                    break;
                case PROPBANK:
                    if (list.getSelectedItem() != null){
                        if (sentenceView.clickedWord.getArgument() == null || !sentenceView.clickedWord.getArgument().toString().equals(list.getSelectedItem().toString())){
                            sentenceView.clickedWord.setArgument(list.getSelectedItem().toString());
                        }
                    }
                    break;
            }
            saveSentence();
            view.invalidate();
            popup.dismiss();
        });
        Button delete = popupLayout.findViewById(R.id.delete);
        delete.setOnClickListener(v -> {
            SentenceView sentenceView = (SentenceView) view;
            sentenceView.sentence.removeWord(sentenceView.selectedWordIndex);
            updateInfo();
            saveSentence();
            popup.dismiss();
        });
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (lineRead.length() > 0 && fileName.length() > 0) {
            ((SentenceView) view).initializeSentence(new AnnotatedSentence(lineRead), new FileDescription("", fileName));
            updateInfo();
        }
    }

    public void updateInfo() {
        SentenceView sentenceView = (SentenceView) view;
        if (sentenceView.sentence != null) {
            toolbar.setTitle(sentenceView.fileDescription.getRawFileName());
            infoTop.setText(sentenceView.sentence.toWords());
            infoBottom.setText(sentenceView.sentence.toWords());
        }
        view.invalidate();
    }

}
