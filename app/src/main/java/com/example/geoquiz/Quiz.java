package com.example.geoquiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Quiz {

    private ArrayList<Question> question_bank = new ArrayList<>();

    private int quiz_category;
    private int question_amount;
    private String quiz_difficulty;
    private int score;

    private Map<String, Integer> quiz_cateories = createCategoryMap();
    

    public Quiz(String category, int q_amount, String difficulty) {
        this.quiz_category = this.quiz_cateories.get(category);
        this.question_amount = q_amount;
        this.quiz_difficulty = difficulty;
    }

    private static Map<String, Integer> createCategoryMap() {
        Map<String, Integer> res_map = new HashMap<String, Integer>();

        res_map.put("Geography", 22);

        return res_map;
    }
}
