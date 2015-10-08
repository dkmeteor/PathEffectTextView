package com.dk.view.demo.patheffect;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.dk.view.patheffect.PathTextView;

public class MainActivity extends ActionBarActivity {
    private PathTextView mPathTextView;
    private EditText mEditText, mColorEditText, mSizeEditText, mWeightEditText;
    private RadioButton mSingle, mMultiply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPathTextView = (PathTextView) findViewById(R.id.path);
        mEditText = (EditText) findViewById(R.id.input);
        mColorEditText = (EditText) findViewById(R.id.color);
        mSizeEditText = (EditText) findViewById(R.id.size);
        mWeightEditText = (EditText) findViewById(R.id.weight);
        mSingle = (RadioButton) findViewById(R.id.radio_single);
        mMultiply = (RadioButton) findViewById(R.id.radio_multiply);


        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String colorText = mColorEditText.getText().toString();
                String sizeText = mSizeEditText.getText().toString();
                String weightText = mWeightEditText.getText().toString();

                int color = Color.BLACK;
                try {
                    if (colorText.contains("#")) {
//                        colorText = colorText.replace("#","");
                        color = Color.parseColor(colorText);
                    } else {
                        color = Integer.parseInt(colorText);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                int size = 72;
                try {
                    size = Integer.parseInt(sizeText);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                int weight = 2;
                try {
                    weight = Integer.parseInt(weightText);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (mSingle.isChecked())
                    mPathTextView.setPaintType(PathTextView.Type.SINGLE);
                else
                    mPathTextView.setPaintType(PathTextView.Type.MULTIPLY);

                mPathTextView.setTextColor(color);
                mPathTextView.setTextSize(size);
                mPathTextView.setTextWeight(weight);
                mPathTextView.init(mEditText.getText().toString());
            }
        });

        mPathTextView.setTextWeight(8);
        mPathTextView.setTextColor(Color.BLUE);
        mPathTextView.setTextSize(144);
    }


}
