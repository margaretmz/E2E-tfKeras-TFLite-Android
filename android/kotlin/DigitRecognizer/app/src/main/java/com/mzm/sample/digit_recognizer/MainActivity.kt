package com.mzm.sample.digit_recognizer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
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

    private lateinit var classifier: Classifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_classify.setOnClickListener(this)
        button_reset.setOnClickListener(this)


        try {
            classifier = Classifier(this)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onClick(view: View) {

        when (view.id) {
            R.id.button_classify -> {
                val bitmap = custom_view_draw?.getBitmap()
                if(bitmap!=null) {
                    val digit = classifier.classify(bitmap)
                    Log.i(LOG_TAG, digit.toString())
                    text_view_predicted_digit!!.text = digit.toString()
                }
            }
            R.id.button_reset -> {
                custom_view_draw?.reset()
                text_view_predicted_digit!!.text = ""
            }
        }

    }

    companion object {

        private val LOG_TAG = Classifier::class.java.simpleName
    }

}
