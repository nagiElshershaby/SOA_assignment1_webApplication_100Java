package com.example.demo;

public class Language {
    private String languageName;
    private int scoreOutOf100;

    public Language(String languageName, int scoreOutOf100) {
        this.languageName = languageName;
        this.scoreOutOf100 = scoreOutOf100;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public int getScoreOutOf100() {
        return scoreOutOf100;
    }

    public void setScoreOutOf100(int scoreOutOf100) {
        this.scoreOutOf100 = scoreOutOf100;
    }

    @Override
    public String toString() {
        return "languageName='" + languageName
                + '\n' +
                "' scoreOutof100=" + scoreOutOf100;
    }
}
