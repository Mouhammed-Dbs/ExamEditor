package com.mouhammeddbs.exam_editor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mouhammeddbs.exam_editor.db.DatabaseHelper;
import com.mouhammeddbs.exam_editor.model.Question;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    private EditText questionID, questionText, answer1, answer2, answer3, answer4;
    private Button addQuestion, preview, attachImage, attachImage1, attachImage2, attachImage3, attachImage4;
    private TextView questionNumber;
    private String imagePath, answer1ImagePath, answer2ImagePath, answer3ImagePath, answer4ImagePath;

    private ActivityResultLauncher<String> questionImageLauncher;
    private ActivityResultLauncher<String> answer1ImageLauncher;
    private ActivityResultLauncher<String> answer2ImageLauncher;
    private ActivityResultLauncher<String> answer3ImageLauncher;
    private ActivityResultLauncher<String> answer4ImageLauncher;

    private DatabaseHelper db;
    private ArrayList<Question> questions;
    private long currentQuestionId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        questions = new ArrayList<>();

        questionID = findViewById(R.id.question_id);
        questionText = findViewById(R.id.question_text);
        answer1 = findViewById(R.id.answer_1);
        answer2 = findViewById(R.id.answer_2);
        answer3 = findViewById(R.id.answer_3);
        answer4 = findViewById(R.id.answer_4);
        addQuestion = findViewById(R.id.add_question);
        preview = findViewById(R.id.preview);
        attachImage = findViewById(R.id.attach_image);
        attachImage1 = findViewById(R.id.attach_image_1);
        attachImage2 = findViewById(R.id.attach_image_2);
        attachImage3 = findViewById(R.id.attach_image_3);
        attachImage4 = findViewById(R.id.attach_image_4);
        questionNumber = findViewById(R.id.question_number);

        questionID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String idText = s.toString();
                if (!idText.isEmpty()) {
                    try {
                        long id = Long.parseLong(idText);
                        loadQuestionForEditing(id);
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    currentQuestionId = -1;
                    addQuestion.setText("Add");
                    resetFields(false);
                }
            }
        });


        questionImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> imagePath = uri != null ? uri.toString() : null);

        answer1ImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> answer1ImagePath = uri != null ? uri.toString() : null);

        answer2ImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> answer2ImagePath = uri != null ? uri.toString() : null);

        answer3ImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> answer3ImagePath = uri != null ? uri.toString() : null);

        answer4ImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> answer4ImagePath = uri != null ? uri.toString() : null);

        checkPermissions();

        addQuestion.setOnClickListener(v -> addQuestion());

        preview.setOnClickListener(v -> {
            loadQuestions();
            openPreviewActivity();
        });

        attachImage.setOnClickListener(v -> questionImageLauncher.launch("image/*"));
        attachImage1.setOnClickListener(v -> answer1ImageLauncher.launch("image/*"));
        attachImage2.setOnClickListener(v -> answer2ImageLauncher.launch("image/*"));
        attachImage3.setOnClickListener(v -> answer3ImageLauncher.launch("image/*"));
        attachImage4.setOnClickListener(v -> answer4ImageLauncher.launch("image/*"));
        loadQuestions();
        updateQuestionNumber();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadQuestionForEditing(long id) {
        Cursor cursor = db.getQuestionById(id);
        if (cursor != null && cursor.moveToFirst()) {
            currentQuestionId = id;

            int questionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_QUESTION);
            int imagePathIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_QUESTION_IMAGE);
            int answer1Index = cursor.getColumnIndex(DatabaseHelper.COLUMN_ANSWER_1);
            int answer2Index = cursor.getColumnIndex(DatabaseHelper.COLUMN_ANSWER_2);
            int answer3Index = cursor.getColumnIndex(DatabaseHelper.COLUMN_ANSWER_3);
            int answer4Index = cursor.getColumnIndex(DatabaseHelper.COLUMN_ANSWER_4);

            if (questionIndex != -1) {
                String question = cursor.getString(questionIndex);
                questionText.setText(question);
            } else {
                Toast.makeText(this, "Column 'question' not found", Toast.LENGTH_SHORT).show();
            }

            if (imagePathIndex != -1) {
                String imagePath = cursor.getString(imagePathIndex);
                this.imagePath = imagePath;
            } else {
                Toast.makeText(this, "Column 'question image' not found", Toast.LENGTH_SHORT).show();
            }

            if (answer1Index != -1) {
                String answer1 = cursor.getString(answer1Index);
                this.answer1.setText(answer1);
            } else {
                Toast.makeText(this, "Column 'answer 1' not found", Toast.LENGTH_SHORT).show();
            }

            if (answer2Index != -1) {
                String answer2 = cursor.getString(answer2Index);
                this.answer2.setText(answer2);
            } else {
                Toast.makeText(this, "Column 'answer 2' not found", Toast.LENGTH_SHORT).show();
            }

            if (answer3Index != -1) {
                String answer3 = cursor.getString(answer3Index);
                this.answer3.setText(answer3);
            } else {
                Toast.makeText(this, "Column 'answer 3' not found", Toast.LENGTH_SHORT).show();
            }

            if (answer4Index != -1) {
                String answer4 = cursor.getString(answer4Index);
                this.answer4.setText(answer4);
            } else {
                Toast.makeText(this, "Column 'answer 4' not found", Toast.LENGTH_SHORT).show();
            }

            addQuestion.setText("Edit");
        } else {
            currentQuestionId = -1;
            addQuestion.setText("Add");
            resetFields();
            Toast.makeText(this, "No question found with this ID", Toast.LENGTH_SHORT).show();
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void addQuestion() {
        String question = questionText.getText().toString();
        String answer1Text = answer1.getText().toString();
        String answer2Text = answer2.getText().toString();
        String answer3Text = answer3.getText().toString();
        String answer4Text = answer4.getText().toString();

        if (question.isEmpty() || (answer1Text.isEmpty() && answer1ImagePath == null) ||
                (answer2Text.isEmpty() && answer2ImagePath == null) ||
                (answer3Text.isEmpty() && answer3ImagePath == null) ||
                (answer4Text.isEmpty() && answer4ImagePath == null)) {
            Toast.makeText(this, "Please fill all fields or provide images", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentQuestionId == -1) {
            long id = db.insertQuestion(question, imagePath, answer1Text, answer1ImagePath, answer2Text, answer2ImagePath, answer3Text, answer3ImagePath, answer4Text, answer4ImagePath, 1);
            loadQuestions();
            updateQuestionNumber();
            Toast.makeText(this, "Question Added", Toast.LENGTH_SHORT).show();
        } else {
            db.updateQuestion(currentQuestionId, question, imagePath, answer1Text, answer1ImagePath, answer2Text, answer2ImagePath, answer3Text, answer3ImagePath, answer4Text, answer4ImagePath);
            Toast.makeText(this, "Question Updated", Toast.LENGTH_SHORT).show();
            currentQuestionId = -1;
            addQuestion.setText("Add");
        }

        resetFields();
    }

    private void resetFields() {
        resetFields(true);
    }

    private void resetFields(boolean withQuesID) {
        if(withQuesID)
            questionID.setText("");
        questionText.setText("");
        answer1.setText("");
        answer2.setText("");
        answer3.setText("");
        answer4.setText("");
        imagePath = null;
        answer1ImagePath = null;
        answer2ImagePath = null;
        answer3ImagePath = null;
        answer4ImagePath = null;
        updateQuestionNumber();
    }



    private void loadQuestions() {
        questions.clear();
        Cursor cursor = db.getAllQuestions();

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                int questionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_QUESTION);
                int imageIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_QUESTION_IMAGE);
                int answer1Index = cursor.getColumnIndex(DatabaseHelper.COLUMN_ANSWER_1);
                int answer2Index = cursor.getColumnIndex(DatabaseHelper.COLUMN_ANSWER_2);
                int answer3Index = cursor.getColumnIndex(DatabaseHelper.COLUMN_ANSWER_3);
                int answer4Index = cursor.getColumnIndex(DatabaseHelper.COLUMN_ANSWER_4);

                if (idIndex != -1 && questionIndex != -1 && imageIndex != -1 && answer1Index != -1 && answer2Index != -1 && answer3Index != -1 && answer4Index != -1) {
                    long id = cursor.getLong(idIndex);
                    String question = cursor.getString(questionIndex);
                    String imagePath = cursor.getString(imageIndex);
                    String answer1 = cursor.getString(answer1Index);
                    String answer2 = cursor.getString(answer2Index);
                    String answer3 = cursor.getString(answer3Index);
                    String answer4 = cursor.getString(answer4Index);

                    String[] answers = {answer1, answer2, answer3, answer4};
                    String[] answerImages = {null, null, null, null};

                    questions.add(new Question(id, question, imagePath, answers, answerImages));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void openPreviewActivity() {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra("questions", questions);
        startActivity(intent);
    }

    private void updateQuestionNumber() {
        questionNumber.setText(""+questions.size());
    }
}
