package com.overdevx.mystoryapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.overdevx.mystoryapp.R

class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var startIcon: Drawable
    private var errorIcon: Drawable
    private var validIcon: Drawable
    private var currentEndIcon: Drawable? = null
    var isValid: Boolean = false
        private set
    init {

        setBackgroundResource(R.drawable.rounded_edit_text_background)
        startIcon = ContextCompat.getDrawable(context, R.drawable.ic_email) as Drawable
        errorIcon = ContextCompat.getDrawable(context, R.drawable.ic_error) as Drawable
        validIcon = ContextCompat.getDrawable(context, R.drawable.ic_check) as Drawable

        setCompoundDrawablesWithIntrinsicBounds(startIcon, null, null, null)
        val drawablePadding = resources.getDimensionPixelSize(R.dimen.drawablepadding)
        setCompoundDrawablePadding(drawablePadding)
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validateEmail(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
    private fun validateEmail(email: String) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        isValid = email.matches(emailPattern.toRegex())
        currentEndIcon = if (email.matches(emailPattern.toRegex())) validIcon else errorIcon
        setCompoundDrawablesWithIntrinsicBounds(startIcon, null, currentEndIcon, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.input_your_email_address)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return false
    }
}