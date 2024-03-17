package com.project.English365.entities;

import java.util.List;

import lombok.Data;

@Data
public class Quiz {

    private String id;
    private String correctAnswer;
    private List<String> incorrectAnswers;
    private QuestionDetails question;

}
