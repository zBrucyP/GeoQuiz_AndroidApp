package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    // view elements
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private TextView scoreCounterTextView;
    private TextView questionCounterTextView;
    private ConstraintLayout layout;
    private Quiz quiz;
    private int correctColor;
    private int inCorrectColor;
    private int baseBackgroundColor;
    private static final String TAG = "MainActivity";
    //private static final String KEY_INDEX = "index";
    private static final String KEY_QUIZ = "quiz";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onStart() called");
        setContentView(R.layout.activity_main);

        // maintaining data through device rotation and activity change
        if (savedInstanceState != null) {
            //mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            quiz = savedInstanceState.getParcelable(KEY_QUIZ);
        }
        else {
            // create quiz object. Arguments are static until API is introduced
            quiz = new Quiz("Geography", 6, "Easy");
        }

        // layout, layout background colors
        layout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        correctColor = getResources().getColor(R.color.correctColor);
        inCorrectColor = getResources().getColor(R.color.incorrectColor);
        baseBackgroundColor = getResources().getColor(R.color.baseBackgroundColor);

        // true & false buttons
        mTrueButton = (Button)findViewById(R.id.true_button);
        mFalseButton = (Button)findViewById(R.id.false_button);

        // next & prev buttons
        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);

        // score counter
        scoreCounterTextView = (TextView) findViewById(R.id.score_counter_view);

        // question counter for user context
        questionCounterTextView = (TextView) findViewById(R.id.question_counter_text);

        // question text
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);

        // refresh activity's displayed data
        refreshUserView();

        // true button click handling
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                // mark question as answered
                quiz.getQuestion(quiz.getCurrentIndex()).setAnswered(true);

                // handle whether the user's answer was correct or not
                boolean answer_correct = checkAnswer(true);
                if (answer_correct) {
                    //update score
                    quiz.incrementScore();

                    // update question
                    quiz.getQuestion(quiz.getCurrentIndex()).setAnsweredCorrectly(true);

                    // refresh for new score and handling of user providing answer
                    refreshUserView();
                }
                else {
                    // update question and layout
                    quiz.getQuestion(quiz.getCurrentIndex()).setAnsweredCorrectly(false);
                    refreshUserView();
                }
            }
        });

        // false button click handling
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // mark question as answered
                quiz.getQuestion(quiz.getCurrentIndex()).setAnswered(true);

                // handle whether the user's answer was correct or not
                boolean answer_correct = checkAnswer(false);
                if (answer_correct) {
                    //update score
                    quiz.incrementScore();

                    // update question
                    quiz.getQuestion(quiz.getCurrentIndex()).setAnsweredCorrectly(true);

                    // refresh for new score and handling of user providing answer
                    refreshUserView();
                }
                else {
                    // update question and layout
                    quiz.getQuestion(quiz.getCurrentIndex()).setAnsweredCorrectly(false);
                    refreshUserView();
                }
            }
        });

        // next button click handling
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiz.incrementIndex();
                refreshUserView();
            }
        });

        // previous button click handling
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiz.decrementIndex();
                refreshUserView();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        //savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putParcelable(KEY_QUIZ, quiz);
    }

    private void updateQuestion() {
        // get question data according to index
        Question question = quiz.getQuestion(quiz.getCurrentIndex());
        int question_text = question.getTextResId();
        boolean question_is_answered = question.isAnswered();
        Boolean question_answered_correctly = question.isAnsweredCorrectly();

        // update displayed question
        mQuestionTextView.setText(question_text);

        if (question_is_answered) {
            // disable buttons
            mTrueButton.setClickable(false);
            mFalseButton.setClickable(false);

            // update background
            if (question_answered_correctly == null) {
                layout.setBackgroundColor(baseBackgroundColor);
            } else if (question_answered_correctly.equals(true)) {
                layout.setBackgroundColor(correctColor);
            } else if (question_answered_correctly.equals(false)) {
                layout.setBackgroundColor(inCorrectColor);
            }
        }
        else{
            // enable buttons
            mTrueButton.setClickable(true);
            mFalseButton.setClickable(true);

            // set to default background -> question has not been answered
            layout.setBackgroundColor(baseBackgroundColor);
        }
    }

    private void updateQuestionCounterText() {
        int currQuestionNumber = quiz.getCurrentIndex() + 1;
        int totalQuestions = quiz.getNumberOfQuestions();

        String displayText = "Question "
                + currQuestionNumber
                + " of "
                + totalQuestions
                + ".";

        questionCounterTextView.setText(displayText);
    }

    private boolean checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = quiz.getQuestion(quiz.getCurrentIndex()).isAnswerTrue();
        int messageResId = 0;
        boolean res;

        // test whether user was correct or incorrect and save result
        if(userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            res = true;
        }
        else {
            messageResId = R.string.incorrect_toast;
            res = false;
        }

        // make and show text telling user if they were correct or incorrect
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        return res;
    }

    public void updateScore() {
        int current_score = quiz.getScore();
        this.scoreCounterTextView.setText(Integer.toString(current_score));
    }

    public void refreshUserView() {
        updateQuestion();
        updateQuestionCounterText();
        updateScore();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
