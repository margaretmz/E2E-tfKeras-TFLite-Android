package com.mzm.sample.digit_recognizer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import java.io.IOException

/**
 *
 * Margaret Maynard-Reid
 * 1/29/2019
 *
 *This sample app shows you how to implement a tflite model in an Android app:
 *
 * Refer to the mnist_keras_to_tflite.ipynb notebook on how to
 * - train a MNIST classifier
 * - and generate mnist.tflite model file
 *
 * Then follow these steps to implement the tflite model on Android:
 * - Place mnist.tflite model under /assets folder
 * - Update app level build.gradle to include tflite dependency
 * - Create a Classifier that does digit classification
 * - Create CustomView for user to draw digits
 * - Create MainActivity with a simple UI that takes user drawing digit as input
 * and displays the classified digit in the UI
 *
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var customView: CustomView? = null
    private var predictionTextView: TextView? = null

    private lateinit var classifier: Classifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.button_classify).setOnClickListener(this)
        findViewById<View>(R.id.button_reset).setOnClickListener(this)

        customView = findViewById(R.id.customview)
        predictionTextView = findViewById(R.id.predictedDigit)

        try {
            classifier = Classifier(this)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onClick(view: View) {

        when (view.id) {
            R.id.button_classify -> {
                val bitmap = customView?.getBitmap()
                if(bitmap!=null) {
                    val digit = classifier.classify(bitmap)
                    Log.i(LOG_TAG, digit.toString())
                    predictionTextView!!.text = digit.toString()
                }
            }
            R.id.button_reset -> {
                customView?.reset()
                predictionTextView!!.text = ""
            }
        }

    }

    companion object {

        private val LOG_TAG = Classifier::class.java.simpleName
    }

}
