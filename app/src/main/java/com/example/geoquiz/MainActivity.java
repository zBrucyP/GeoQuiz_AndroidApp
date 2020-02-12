package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

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

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private TextView scoreCounterTextView;
    private TextView questionCounterTextView;
    private Quiz quiz;
    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";

    private int mCurrentIndex = 0;
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onStart() called");
        setContentView(R.layout.activity_main);

        // maintaining data through device rotation
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        // score counter
        scoreCounterTextView = (TextView) findViewById(R.id.score_counter_view);

        // question counter for user context
        questionCounterTextView = (TextView) findViewById(R.id.question_counter_text);
        updateQuestionCounterText();

        // question text
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        updateQuestion();

        // true button click handling
        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                boolean answer_correct = checkAnswer(true);
                if (answer_correct) {
                    int currentScore = Integer.parseInt(scoreCounterTextView.getText().toString());
                    scoreCounterTextView.setText(Integer.toString(currentScore+1));
                }
            }
        });

        // false button click handling
        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean answer_correct = checkAnswer(false);
                if (answer_correct) {
                    int currentScore = Integer.parseInt(scoreCounterTextView.getText().toString());
                    scoreCounterTextView.setText(Integer.toString(currentScore+1));
                }
            }
        });

        // next button click handling
        mNextButton = (ImageButton)findViewById(R.id.next_button);
        updateQuestionCounterText();
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        // previous button click handling
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        updateQuestionCounterText();
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prevent carouseling
                if (mCurrentIndex > 0) {
                    mCurrentIndex = (mCurrentIndex - 1);
                    updateQuestion();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void updateQuestionCounterText() {
        int currQuestionNumber = mCurrentIndex + 1;
        int totalQuestions = mQuestionBank.length;

        String displayText = "Question "
                + currQuestionNumber
                + " of "
                + totalQuestions
                + ".";

        questionCounterTextView.setText(displayText);
    }

    private boolean checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
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
