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
    private EditText mEditText, mColorEditText, mSizeEditText, mWeightEditText,mShadowWeight,mShadowColor;
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
        mShadowWeight = (EditText) findViewById(R.id.shadow_weight);
        mShadowColor = (EditText) findViewById(R.id.shadow_color);
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
//                    e.printStackTrace();
                }
                int size = 72;
                try {
                    size = Integer.parseInt(sizeText);
                } catch (NumberFormatException e) {
//                    e.printStackTrace();
                }
                int weight = 2;
                try {
                    weight = Integer.parseInt(weightText);
                } catch (NumberFormatException e) {
//                    e.printStackTrace();
                }

                String shadowWeightText = mShadowWeight.getText().toString();

                int shadowWeight = weight / 2;
                try {
                    shadowWeight = Integer.parseInt(shadowWeightText);
                } catch (NumberFormatException e) {
//                    e.printStackTrace();
                }

                String shdowColorText =mShadowColor.getText().toString();
                int shadowColor = Color.GRAY;
                try {
                    if (shdowColorText.contains("#")) {
//                        colorText = colorText.replace("#","");
                        shadowColor = Color.parseColor(shdowColorText);
                    } else {
                        shadowColor = Integer.parseInt(shdowColorText);
                    }
                } catch (NumberFormatException e) {
//                    e.printStackTrace();
                }



                if (mSingle.isChecked())
                    mPathTextView.setPaintType(PathTextView.Type.SINGLE);
                else
                    mPathTextView.setPaintType(PathTextView.Type.MULTIPLY);

                mPathTextView.setTextColor(color);
                mPathTextView.setTextSize(size);
                mPathTextView.setTextWeight(weight);
                mPathTextView.setDuration(2000);
                mPathTextView.setShadow(shadowWeight,shadowWeight,shadowWeight,shadowColor);
                mPathTextView.init(mEditText.getText().toString());
            }
        });

        mPathTextView.setTextWeight(8);
        mPathTextView.setTextColor(Color.BLUE);
        mPathTextView.setTextSize(144);
    }


}
