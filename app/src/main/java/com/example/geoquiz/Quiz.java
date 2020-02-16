package com.example.geoquiz;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the quiz game.
 * Handles the data and most logic for the UI
 */
public class Quiz implements Parcelable {
// https://stackoverflow.com/questions/10126845/handle-screen-rotation-without-losing-data-android

    private String quiz_category; // will be int when API introduced
    private int question_amount;
    private String quiz_difficulty;
    private int current_index;
    private int score;
    private Question[] question_bank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };
    private Map<String, Integer> quiz_cateories = createCategoryMap();
    

    // recreate object from parcel
    private Quiz(Parcel in) {
        //mData = in.readInt();
    }

    /**
     * Constructor
     * @param category a category of question types: geography, politics, etc.
     * @param q_amount how much questions to use
     * @param difficulty how difficult the questions & quiz should be
     */
    public Quiz(String category, int q_amount, String difficulty) {
        this.quiz_category = category;
        this.question_amount = q_amount;
        this.quiz_difficulty = difficulty;
        this.current_index = 0;
        this.score = 0;
    }

    /**
     *  This will generate the mapping of category string to ID in the API
     *  TODO: Insert more categories, utilize this method
     * @return a map of Category : Category ID in API
     */
    private static Map<String, Integer> createCategoryMap() {
        Map<String, Integer> res_map = new HashMap<String, Integer>();

        res_map.put("Geography", 22);

        return res_map;
    }

    /**
     * Likely unused, more useful function is getQuestion(int)
     * @return array of questions in this quiz
     */
    public Question[] getQuestion_bank() {
        return question_bank;
    }

    public int getCurrentIndex() {
        return this.current_index;
    }

    public void incrementIndex() {
        this.current_index = (current_index + 1) % this.question_bank.length;
    }

    public void decrementIndex() {
        if (current_index > 0) {
            this.current_index--;
        }
        else {
            this.current_index = this.question_bank.length - 1;
        }
    }

    public void setCurrent_index(int current_index) {
        if(current_index >= 0 && current_index < this.question_bank.length) {
            this.current_index = current_index;
        }
    }

    public void setQuestionAnswered(){
        this.question_bank[current_index].setAnswered(true);
    }

    public boolean getQuestionAnswered() {
        return this.question_bank[current_index].isAnswered();
    }

    public Question getQuestion(int index) {
        if (index < question_bank.length && index >= 0) {
            return this.question_bank[index];
        }
        else{
            return null;
        }
    }

    public void incrementScore() {
        this.score++;
    }

    public int getNumberOfQuestions() {
        return this.question_bank.length;
    }

    public int getScore() {
        return this.score;
    }

    /**
     * The below 3 pieces of code are necessary to implement the Parcelable functionality
     * Parcelable is needed to efficiently maintain quiz data through
     * activity changes, screen rotations, etc.
     */
    public static final Parcelable.Creator<Quiz> CREATOR
            = new Parcelable.Creator<Quiz>() {
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags){
        // out.writeInt(mData);
    }
}
