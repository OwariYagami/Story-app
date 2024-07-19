package com.overdevx.mystoryapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.overdevx.mystoryapp.R

class MyNameEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var startIcon: Drawable
    init {

        setBackgroundResource(R.drawable.rounded_edit_text_background)
        startIcon = ContextCompat.getDrawable(context, R.drawable.ic_name) as Drawable

        setCompoundDrawablesWithIntrinsicBounds(startIcon, null, null, null)
        val drawablePadding = resources.getDimensionPixelSize(R.dimen.drawablepadding)
        setCompoundDrawablePadding(drawablePadding)
        setOnTouchListener(this)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.input_your_name)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return false
    }
}