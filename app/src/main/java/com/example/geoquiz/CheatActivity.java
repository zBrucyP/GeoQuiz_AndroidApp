package com.example.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class CheatActivity extends Activity {

    public static final String EXTRA_ANSWER_IS_TRUE = "EXTRA_ANSWER_IS_TRUE";
    public static final String EXTRA_ANSWER_SHOWN = "EXTRA_ANSWER_SHOWN";
    private boolean mAnswerIsTrue;
    private boolean cheated;
    private TextView mAnswerTextView;
    private Button mShowAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            cheated = savedInstanceState.getBoolean("CHEATED");
            mAnswerIsTrue = savedInstanceState.getBoolean("ISANSWERTRUE");
        }
        else {
            cheated = false;
        }

        // retrieves from calling activity what the answer is
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        // component to show answer
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);
        Log.d("tag", cheated + " " + mAnswerIsTrue);
        if (cheated && mAnswerIsTrue) {
            mAnswerTextView.setText(R.string.true_button);
        }
        else if (cheated && mAnswerIsTrue == false) {
            mAnswerTextView.setText(R.string.false_button);
        }

        // button to verify decision
        mShowAnswer = (Button) findViewById(R.id.showAnswerButton);

        // default is false, answer not shown and recorded until user presses button
        if (cheated) {
            setAnswerShownResult(true);
        }
        else {
            setAnswerShownResult(false);
        }

        // showAnswerButton click handler
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheated = true;
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                }
                else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true);
            }
        });
    }

    private void setAnswerShownResult (boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("CHEATED", cheated);
        savedInstanceState.putBoolean("ISANSWERTRUE", mAnswerIsTrue);
    }
}
