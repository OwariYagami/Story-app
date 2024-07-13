package com.overdevx.mystoryapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.overdevx.mystoryapp.R

class MyButtonSubmit : AppCompatButton {

    constructor(context: Context) : super(context) // untuk di Activity/Fragment
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)  // untuk di XML

    private var txtColor: Int = 0
    private var enabledBackground: Drawable
    private var disabledBackground: Drawable
    private var progressBar:CircularProgressIndicator?=null
    init {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if(isEnabled) enabledBackground else disabledBackground
        setTextColor(txtColor)
        textSize = 18f
        gravity = Gravity.CENTER
        text = if(isEnabled) "Submit" else "Fill all first"
    }

    fun showProgressBar(show: Boolean) {
        if (parent is RelativeLayout) {
            progressBar = (parent as RelativeLayout).findViewById(R.id.progressindicator)
            if (show) {
                progressBar?.visibility = View.VISIBLE
                text = ""
            } else {
                progressBar?.visibility = View.GONE
                text = if (isEnabled) "Submit" else "Fill all first"
            }
        }
    }
}