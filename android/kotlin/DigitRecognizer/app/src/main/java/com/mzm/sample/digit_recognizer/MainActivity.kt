package com.mzm.sample.digit_recognizer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import java.io.IOException

/**
 * Margaret Maynard-Reid
 * 1/29/2019
 *
 *
 * This sample code shows you how to implement a tflite model in an Android app:
 *
 *
 * Refer to this notebook to see how to train a MNIST classifier and generate mnist.tflite
 * https://github.com/margaretmz/deep-learning/blob/master/tfkeras-tflite-android/mnist_keras_to_tflite.ipynb
 *
 * Then follow these steps to implement the tflite model on Android:
 * - Place mnist.tflite model under assets folder
 * - Update build.gradle to include tflite dependency
 * - Create CustomView for user to draw digits
 * - Create a Classifier that does digit classification
 *
 *
 * It has a simple UI that takes user drawing digit as input
 * then displays the classified result in the UI
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var customView: CustomView? = null
    private var resultTextView: TextView? = null
    private lateinit var classifier: Classifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.button_clear).setOnClickListener(this)
        findViewById<View>(R.id.button_classify).setOnClickListener(this)

        customView = findViewById(R.id.customview)
        resultTextView = findViewById(R.id.result)

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
                    resultTextView!!.text = digit.toString()
                }
            }
            R.id.button_clear -> {
                customView?.clear()
                resultTextView!!.text = ""
            }
        }

    }

    companion object {

        private val LOG_TAG = Classifier::class.java.simpleName
    }

}
