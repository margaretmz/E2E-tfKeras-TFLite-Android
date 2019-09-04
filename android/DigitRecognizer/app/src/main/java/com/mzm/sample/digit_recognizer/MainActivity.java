package com.mzm.sample.digit_recognizer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

/**
 * Margaret Maynard-Reid
 * 1/29/2019
 * <p>
 * This sample code shows you how to implement a tflite model in an Android app:
 * <p>
 * Refer to this notebook to see how to train a MNIST classifier and generate mnist.tflite
 * https://github.com/margaretmz/deep-learning/blob/master/tfkeras-tflite-android/mnist_keras_to_tflite.ipynb
 *
 * Then follow these steps to implement the tflite model on Android:
 * - Place mnist.tflite model under assets folder
 * - Update build.gradle to include tflite dependency
 * - Create CustomView for user to draw digits
 * - Create a Classifier that does digit classification
 * <p>
 * It has a simple UI that takes user drawing digit as input
 * then displays the classified result in the UI
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = Classifier.class.getSimpleName();

    private CustomView customView;
    private TextView resultTextView;
    private Classifier classifier;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_clear).setOnClickListener(this);
        findViewById(R.id.button_classify).setOnClickListener(this);

        customView = findViewById(R.id.customView);
        resultTextView = findViewById(R.id.result);

        try {
            classifier = new Classifier(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_classify:
                Bitmap scaledBitmap = customView.getBitmap(classifier.DIM_IMG_SIZE_X, classifier.DIM_IMG_SIZE_Y);
                int digit = classifier.classify(scaledBitmap);
                Log.i(LOG_TAG, String.valueOf(digit));
                resultTextView.setText(String.valueOf(digit));
                break;
            case R.id.button_clear:
                customView.clear();
                resultTextView.setText("");
                break;
        }

    }

}
