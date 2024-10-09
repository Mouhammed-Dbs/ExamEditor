package com.mouhammeddbs.exam_editor.db;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "exam_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_QUESTIONS = "questions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_QUESTION_IMAGE = "question_image";
    public static final String COLUMN_ANSWER_1 = "answer_1";
    public static final String COLUMN_ANSWER_1_IMAGE = "answer_1_image";
    public static final String COLUMN_ANSWER_2 = "answer_2";
    public static final String COLUMN_ANSWER_2_IMAGE = "answer_2_image";
    public static final String COLUMN_ANSWER_3 = "answer_3";
    public static final String COLUMN_ANSWER_3_IMAGE = "answer_3_image";
    public static final String COLUMN_ANSWER_4 = "answer_4";
    public static final String COLUMN_ANSWER_4_IMAGE = "answer_4_image";
    private static final String COLUMN_CORRECT_ANSWER = "correct_answer";

    private static final String CREATE_TABLE_QUESTIONS =
            "CREATE TABLE " + TABLE_QUESTIONS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_QUESTION + " TEXT,"
                    + COLUMN_QUESTION_IMAGE + " TEXT,"
                    + COLUMN_ANSWER_1 + " TEXT,"
                    + COLUMN_ANSWER_1_IMAGE + " TEXT,"
                    + COLUMN_ANSWER_2 + " TEXT,"
                    + COLUMN_ANSWER_2_IMAGE + " TEXT,"
                    + COLUMN_ANSWER_3 + " TEXT,"
                    + COLUMN_ANSWER_3_IMAGE + " TEXT,"
                    + COLUMN_ANSWER_4 + " TEXT,"
                    + COLUMN_ANSWER_4_IMAGE + " TEXT,"
                    + COLUMN_CORRECT_ANSWER + " INTEGER"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUESTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        onCreate(db);
    }

    public long insertQuestion(String question, String questionImage, String answer1, String answer1Image,
                               String answer2, String answer2Image, String answer3, String answer3Image,
                               String answer4, String answer4Image, int correctAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, question);
        values.put(COLUMN_QUESTION_IMAGE, questionImage);
        values.put(COLUMN_ANSWER_1, answer1);
        values.put(COLUMN_ANSWER_1_IMAGE, answer1Image);
        values.put(COLUMN_ANSWER_2, answer2);
        values.put(COLUMN_ANSWER_2_IMAGE, answer2Image);
        values.put(COLUMN_ANSWER_3, answer3);
        values.put(COLUMN_ANSWER_3_IMAGE, answer3Image);
        values.put(COLUMN_ANSWER_4, answer4);
        values.put(COLUMN_ANSWER_4_IMAGE, answer4Image);
        values.put(COLUMN_CORRECT_ANSWER, correctAnswer);

        long id = db.insert(TABLE_QUESTIONS, null, values);
        db.close();
        return id;
    }

    public Cursor getAllQuestions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS, null);
    }

    public Cursor getQuestionById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int updateQuestion(long id, String question, String questionImage, String answer1, String answer1Image,
                              String answer2, String answer2Image, String answer3, String answer3Image,
                              String answer4, String answer4Image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, question);
        values.put(COLUMN_QUESTION_IMAGE, questionImage);
        values.put(COLUMN_ANSWER_1, answer1);
        values.put(COLUMN_ANSWER_1_IMAGE, answer1Image);
        values.put(COLUMN_ANSWER_2, answer2);
        values.put(COLUMN_ANSWER_2_IMAGE, answer2Image);
        values.put(COLUMN_ANSWER_3, answer3);
        values.put(COLUMN_ANSWER_3_IMAGE, answer3Image);
        values.put(COLUMN_ANSWER_4, answer4);
        values.put(COLUMN_ANSWER_4_IMAGE, answer4Image);

        return db.update(TABLE_QUESTIONS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }


    public int getQuestionCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_QUESTIONS, null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

}
