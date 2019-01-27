package com.anwesh.uiprojects.linkedpinearrowview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.pinearrowview.PineArrowView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PineArrowView.create(this)
    }
}
