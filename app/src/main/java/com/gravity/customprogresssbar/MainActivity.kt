package com.gravity.customprogresssbar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.gravity.customprogressbar.CustomProgressBar
import com.gravity.customprogressbar.CustomProgressBarScrollable
import com.gravity.customprogressbar.CustomProgressModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CustomProgressBar.OnProgressLabelInteractionListener {

    private lateinit var focusChange: FocusChange

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var customProgressModel1 = CustomProgressModel(3, "1st form")
        var customProgressModel2 = CustomProgressModel(4, "2nd form")
        var customProgressModel3 = CustomProgressModel(5, "3rd form")
        var customProgressModel4 = CustomProgressModel(3, "4th form")

        var customProgressModel5 = CustomProgressModel(5, "5th form")
        var customProgressModel6 = CustomProgressModel(5, "6th form")
        var customProgressModel7 = CustomProgressModel(5, "7th form")
        var customProgressModel8 = CustomProgressModel(5, "8th form")

        var formsModelList: List<CustomProgressModel> = ArrayList()
        formsModelList += customProgressModel1
        formsModelList += customProgressModel2
        formsModelList += customProgressModel3
        formsModelList += customProgressModel4
        //formsModelList += customProgressModel5
        //formsModelList += customProgressModel6
        //formsModelList += customProgressModel7
        //formsModelList += customProgressModel8
        custom_progress.addProgressForms(this, formsModelList)

        focusChange = FocusChange()
        et11.onFocusChangeListener = focusChange
        et12.onFocusChangeListener = focusChange
        et13.onFocusChangeListener = focusChange
        et21.onFocusChangeListener = focusChange
        et22.onFocusChangeListener = focusChange
        et23.onFocusChangeListener = focusChange
        et24.onFocusChangeListener = focusChange
        et31.onFocusChangeListener = focusChange
        et32.onFocusChangeListener = focusChange
        et33.onFocusChangeListener = focusChange
        et34.onFocusChangeListener = focusChange
        et35.onFocusChangeListener = focusChange
        et41.onFocusChangeListener = focusChange
        et42.onFocusChangeListener = focusChange
        et43.onFocusChangeListener = focusChange

        submit.setOnClickListener {
            custom_progress.changeFormProgress(15)
        }

    }

    inner class FocusChange : View.OnFocusChangeListener {
        override fun onFocusChange(v: View, hasFocus: Boolean) {
            when (v.id) {
                R.id.et11 -> if (hasFocus) {
                    custom_progress.changeFormProgress(0)
                }
                R.id.et12 -> if (hasFocus) {
                    custom_progress.changeFormProgress(1)
                }
                R.id.et13 -> if (hasFocus) {
                    custom_progress.changeFormProgress(2)
                }
                R.id.et21 -> if (hasFocus) {
                    custom_progress.changeFormProgress(3)
                }
                R.id.et22 -> if (hasFocus) {
                    custom_progress.changeFormProgress(4)
                }
                R.id.et23 -> if (hasFocus) {
                    custom_progress.changeFormProgress(5)
                }
                R.id.et24 -> if (hasFocus) {
                    custom_progress.changeFormProgress(6)
                }
                R.id.et31 -> if (hasFocus) {
                    custom_progress.changeFormProgress(7)
                }
                R.id.et32 -> if (hasFocus) {
                    custom_progress.changeFormProgress(8)
                }
                R.id.et33 -> if (hasFocus) {
                    custom_progress.changeFormProgress(9)
                }
                R.id.et34 -> if (hasFocus) {
                    custom_progress.changeFormProgress(10)
                }
                R.id.et35 -> if (hasFocus) {
                    custom_progress.changeFormProgress(11)
                }
                R.id.et41 -> if (hasFocus) {
                    custom_progress.changeFormProgress(12)
                }
                R.id.et42 -> if (hasFocus) {
                    custom_progress.changeFormProgress(13)
                }
                R.id.et43 -> if (hasFocus) {
                    custom_progress.changeFormProgress(14)
                }
            }
        }
    }

    override fun onProgressLabelClick(formIndex: Int, classificationName: String?) {
        Toast.makeText(this, formIndex.toString() + " " + classificationName, Toast.LENGTH_SHORT).show()
    }
}
