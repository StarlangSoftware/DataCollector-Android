package com.starlang.datacollector;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.starlang.datacollector.ParseTree.TreeView;
import com.starlang.datacollector.Sentence.SentenceView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;
import NamedEntityRecognition.NamedEntityType;

public class DataCollectorActivity extends AppCompatActivity {

    protected MenuItem itemOpen;
    protected MenuItem itemOpenDirectory;
    protected Uri currentUri;
    protected TextView infoTop;
    protected TextView infoBottom;
    protected Toolbar toolbar;
    protected DataCollectorView view;
    protected int baseFontSize, fontMultiplier;
    protected String lineRead;
    protected String fileName;
    protected Spinner list;
    protected View popupLayout;
    public PopupWindow popup;
    public boolean popupOpen = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        itemOpen = menu.findItem(R.id.itemOpen);
        itemOpenDirectory = menu.findItem(R.id.itemOpenDirectory);
        return true;
    }

    protected void updateHeight(){
        int others = infoTop.getMinimumHeight() + infoBottom.getMinimumHeight() + toolbar.getMinimumHeight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.getLayoutParams().height = displayMetrics.heightPixels - (int) (2.5 * others);
    }

    protected void connectSentenceViews(){
        infoTop = findViewById(R.id.infoTop);
        infoBottom = findViewById(R.id.infoBottom);
        toolbar = findViewById(R.id.toolbar);
        view = (SentenceView) findViewById(R.id.view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    protected void connectTreeViews(){
        infoTop = findViewById(R.id.infoTop);
        infoBottom = findViewById(R.id.infoBottom);
        toolbar = findViewById(R.id.toolbar);
        view = (TreeView) findViewById(R.id.view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    protected void save(String lineWritten){
        OutputStream outputStream;
        try {
            outputStream = getContentResolver().openOutputStream(currentUri);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, UTF_8));
            writer.write(lineWritten);
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected ArrayList fillMorphologicalAnalyzerSpinner(FsmMorphologicalAnalyzer fsm, String word){
        ArrayList<FsmParse> spinnerList =  new ArrayList<FsmParse>();
        FsmParseList fsmParseList = fsm.robustMorphologicalAnalysis(word);
        for (int i = 0; i < fsmParseList.size(); i++){
            spinnerList.add(fsmParseList.getFsmParse(i));
        }
        return spinnerList;
    }

    protected ArrayList fillSentimentSpinner(){
        ArrayList<String> spinnerList =  new ArrayList<String>();
        spinnerList.add("Positive");
        spinnerList.add("Negative");
        spinnerList.add("Neutral");
        return spinnerList;
    }

    protected ArrayList fillNERSpinner(){
        ArrayList<String> spinnerList =  new ArrayList<String>();
        for (int i = 0; i < NamedEntityType.values().length; i++){
            spinnerList.add(NamedEntityType.values()[i].toString());
        }
        return spinnerList;
    }

    protected ArrayList fillPosTaggerSpinner(){
        String[] pennTagList = {"CC", "CD", "DT", "EX", "FW", "IN", "JJ", "JJR", "JJS", "LS",
                "MD", "NN", "NNS", "NNP", "NNPS", "PDT", "POS", "PRP", "PRP$", "RB",
                "RBR", "RBS", "RP", "SYM", "TO", "UH", "VB", "VBD", "VBG", "VBN",
                "VBP", "VBZ", "WDT", "WP", "WP$", "WRB", "$", "#", ".", ",",
                "``", "''", ":", "-LRB-", "-RRB-", "AUX:VB", "AUX:VBP", "AUX:VBZ", "AUX:VBD", "AUX:VBG",
                "AUX:VBN"};
        ArrayList<String> spinnerList =  new ArrayList<String>();
        for (String tag : pennTagList){
            spinnerList.add(tag);
        }
        return spinnerList;
    }

    protected ArrayList fillSpinner(){
        return new ArrayList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.itemOpen:
                lineRead = "";
                fileName = "";
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, 123);
                break;
            case R.id.itemOpenDirectory:
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(intent, 234);
                break;
            case R.id.forward:
                view.next(1);
                break;
            case R.id.backward:
                view.previous(1);
                break;
            case R.id.monospace:
                view.typeface = Typeface.MONOSPACE;
                view.invalidate();
                break;
            case R.id.serif:
                view.typeface = Typeface.SERIF;
                view.invalidate();
                break;
            case R.id.sansSerif:
                view.typeface = Typeface.SANS_SERIF;
                view.invalidate();
                break;
            case R.id.fontxs:
                view.fontSize = baseFontSize;
                view.invalidate();
                break;
            case R.id.fonts:
                view.fontSize = baseFontSize + fontMultiplier;
                view.invalidate();
                break;
            case R.id.fontm:
                view.fontSize = baseFontSize + 2 * fontMultiplier;
                view.invalidate();
                break;
            case R.id.fontl:
                view.fontSize = baseFontSize + 3 * fontMultiplier;
                view.invalidate();
                break;
            case R.id.fontxl:
                view.fontSize = baseFontSize + 4 * fontMultiplier;
                view.invalidate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 123){
                try {
                    currentUri = data.getData();
                    Cursor cursor = getContentResolver().query(currentUri, null, null, null, null, null);
                    cursor.moveToFirst();
                    fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    InputStream inputStream = getContentResolver().openInputStream(currentUri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    lineRead = reader.readLine();
                    reader.close();
                    inputStream.close();
                    updateInfo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (requestCode == 234){
                    currentUri = data.getData();
                }
            }
        }
    }

    public void showError(String error){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(error);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    public void showPopup(float x, float y){
        LinearLayout viewGroup = this.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupLayout = layoutInflater.inflate(R.layout.popup_annotator, viewGroup);
        popup = new PopupWindow(this);
        popup.setContentView(popupLayout);
        list = popupLayout.findViewById(R.id.spinner);
        ArrayAdapter<FsmParse> adapter = new ArrayAdapter<FsmParse>(this, android.R.layout.simple_spinner_item, fillSpinner());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list.setAdapter(adapter);
        list.setVisibility(View.VISIBLE);
        popup.setFocusable(true);
        popup.showAtLocation(popupLayout, Gravity.NO_GRAVITY, (int) x, (int) y);
        Button close = popupLayout.findViewById(R.id.close);
        close.setOnClickListener(v -> {
            popup.dismiss();
            popupOpen = false;
        });
    }

    protected void updateInfo(){
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateHeight();
        updateInfo();
    }
}
