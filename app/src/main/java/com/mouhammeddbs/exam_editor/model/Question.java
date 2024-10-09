package com.mouhammeddbs.exam_editor.model;

import java.io.Serializable;

public class Question implements Serializable {
    private long id;
    private String questionText;
    private String questionImagePath;
    private String[] answers;
    private String[] answerImagePaths;

    public Question(long id, String questionText, String questionImagePath, String[] answers, String[] answerImagePaths) {
        this.id = id;
        this.questionText = questionText;
        this.questionImagePath = questionImagePath;
        this.answers = answers;
        this.answerImagePaths = answerImagePaths;
    }

    // Getters and setters for each field
    public long getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getQuestionImagePath() {
        return questionImagePath;
    }

    public String[] getAnswers() {
        return answers;
    }

    public String[] getAnswerImagePaths() {
        return answerImagePaths;
    }
}
