package com.example.itest.modelsOftest;

public class Answer {
    private String answer;
    private boolean isCorrectAnswer;
    private String key;
    private String quetion_key;
    public Answer(){

    }
    public Answer(String answer){
        this.answer = answer;

    }

    public String getQuetion_key() {
        return quetion_key;
    }

    public void setQuetion_key(String quetion_key) {
        this.quetion_key = quetion_key;
    }

    //getters
    public String getAnswer() {
        return answer;
    }

    public String getKey() {
        return key;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }


    //setters

    public void setKey(String key) {
        this.key = key;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
    }



}
