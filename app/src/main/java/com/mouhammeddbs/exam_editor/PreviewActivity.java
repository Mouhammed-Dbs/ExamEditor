package com.mouhammeddbs.exam_editor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.mouhammeddbs.exam_editor.model.Question;

import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {

    private ListView previewListView;
    private ArrayList<Question> questions;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        previewListView = findViewById(R.id.preview_list_view);

        // Retrieve the questions ArrayList from the Intent
        questions = (ArrayList<Question>) getIntent().getSerializableExtra("questions");
        if (questions != null) {
            ArrayList<String> displayList = new ArrayList<>();
            int numQ =0;
            for (Question question : questions) {
                StringBuilder questionDisplay = new StringBuilder();
                questionDisplay.append(String.format("السؤال (%d):  ", ++numQ)).append(question.getQuestionText()).append("\n");
                questionDisplay.append("\n");
                int numA = 0;
                for (String answer : question.getAnswers()) {
                    questionDisplay.append(++numA).append(" - ").append(answer).append("\n");
                }
                questionDisplay.append("\nمعرف السؤال:  ").append(question.getId());
                displayList.add(questionDisplay.toString());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
            previewListView.setAdapter(adapter);
        }
    }
}
