package com.starlang.datacollector.Sentence;

import android.os.Bundle;

import com.starlang.datacollector.R;

import java.util.ArrayList;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import WordNet.SynSet;
import WordNet.WordNet;

public class SentenceSemanticActivity extends SentenceActivity{
    private FsmMorphologicalAnalyzer fsm;
    public WordNet wordNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentence_semantic_annotator);
        connectSentenceViews();
        infoTop.setText("Semantic");
        infoBottom.setText("Sentence");
        //wordNet = new WordNet();
        fsm = new FsmMorphologicalAnalyzer();
    }

    protected ArrayList fillSpinner(){
        ArrayList<SynSet> spinnerList = new ArrayList<>();
        AnnotatedSentence sentence = ((SentenceView) view).sentence;
        int wordIndex = ((SentenceView) view).selectedWordIndex;
        for (int i = wordIndex - 4; i <= wordIndex; i++){
            if (i >= 0 && i + 4 < sentence.wordCount()){
                AnnotatedWord word1 = (AnnotatedWord) sentence.getWord(i);
                AnnotatedWord word2 = (AnnotatedWord) sentence.getWord(i + 1);
                AnnotatedWord word3 = (AnnotatedWord) sentence.getWord(i + 2);
                AnnotatedWord word4 = (AnnotatedWord) sentence.getWord(i + 3);
                AnnotatedWord word5 = (AnnotatedWord) sentence.getWord(i + 4);
                if (word1.getParse() != null && word2.getParse() != null && word3.getParse() != null && word4.getParse() != null && word5.getParse() != null){
                    ArrayList<SynSet> idioms = wordNet.constructIdiomSynSets(word1.getParse(), word2.getParse(), word3.getParse(), word4.getParse(), word5.getParse(), word1.getMetamorphicParse(), word2.getMetamorphicParse(), word3.getMetamorphicParse(), word4.getMetamorphicParse(), word5.getMetamorphicParse(), fsm);
                    spinnerList.addAll(idioms);
                }
            }
        }
        for (int i = wordIndex - 3; i <= wordIndex; i++){
            if (i >= 0 && i + 3 < sentence.wordCount()){
                AnnotatedWord word1 = (AnnotatedWord) sentence.getWord(i);
                AnnotatedWord word2 = (AnnotatedWord) sentence.getWord(i + 1);
                AnnotatedWord word3 = (AnnotatedWord) sentence.getWord(i + 2);
                AnnotatedWord word4 = (AnnotatedWord) sentence.getWord(i + 3);
                if (word1.getParse() != null && word2.getParse() != null && word3.getParse() != null && word4.getParse() != null){
                    ArrayList<SynSet> idioms = wordNet.constructIdiomSynSets(word1.getParse(), word2.getParse(), word3.getParse(), word4.getParse(), word1.getMetamorphicParse(), word2.getMetamorphicParse(), word3.getMetamorphicParse(), word4.getMetamorphicParse(), fsm);
                    spinnerList.addAll(idioms);
                }
            }
        }
        for (int i = wordIndex - 2; i <= wordIndex; i++){
            if (i >= 0 && i + 2 < sentence.wordCount()){
                AnnotatedWord word1 = (AnnotatedWord) sentence.getWord(i);
                AnnotatedWord word2 = (AnnotatedWord) sentence.getWord(i + 1);
                AnnotatedWord word3 = (AnnotatedWord) sentence.getWord(i + 2);
                if (word1.getParse() != null && word2.getParse() != null && word3.getParse() != null){
                    ArrayList<SynSet> idioms = wordNet.constructIdiomSynSets(word1.getParse(), word2.getParse(), word3.getParse(), word1.getMetamorphicParse(), word2.getMetamorphicParse(), word3.getMetamorphicParse(), fsm);
                    spinnerList.addAll(idioms);
                }
            }
        }
        for (int i = wordIndex - 1; i <= wordIndex; i++){
            if (i >= 0 && i + 1 < sentence.wordCount()){
                AnnotatedWord word1 = (AnnotatedWord) sentence.getWord(i);
                AnnotatedWord word2 = (AnnotatedWord) sentence.getWord(i + 1);
                if (word1.getParse() != null && word2.getParse() != null){
                    ArrayList<SynSet> idioms = wordNet.constructIdiomSynSets(word1.getParse(), word2.getParse(), word1.getMetamorphicParse(), word2.getMetamorphicParse(), fsm);
                    spinnerList.addAll(idioms);
                }
            }
        }
        AnnotatedWord word = ((SentenceView) view).clickedWord;
        if (word.getParse() != null){
            ArrayList<SynSet> synSets = wordNet.constructSynSets(word.getParse().getWord().getName(), word.getParse(), word.getMetamorphicParse(), fsm);
            spinnerList.addAll(synSets);
        }
        return spinnerList;
    }

}
