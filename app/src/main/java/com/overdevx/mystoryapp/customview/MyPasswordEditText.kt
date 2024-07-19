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

class MyPasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var startIcon: Drawable
    private var showPasswordIcon: Drawable
    private var hidePasswordIcon: Drawable
    private var isPasswordVisible: Boolean = false
    private var currentEndIcon: Drawable? = null
    var isValid: Boolean = false
        private set

    init {
        setBackgroundResource(R.drawable.rounded_edit_text_background)
        startIcon = ContextCompat.getDrawable(context, R.drawable.ic_password) as Drawable
        showPasswordIcon =
            ContextCompat.getDrawable(context, R.drawable.ic_show_password) as Drawable
        hidePasswordIcon =
            ContextCompat.getDrawable(context, R.drawable.ic_hide_password) as Drawable

        setCompoundDrawablesWithIntrinsicBounds(startIcon, null, showPasswordIcon, null)
        val drawablePadding = resources.getDimensionPixelSize(R.dimen.drawablepadding)
        setCompoundDrawablePadding(drawablePadding)
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.input_your_password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun validatePassword(password: String) {
        isValid = password.length >= 8
        if (isValid) {
            error = null
        } else {
            setError(context.getString(R.string.password_info), null)
        }
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = startIcon,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        val endIcon = if (isPasswordVisible) hidePasswordIcon else showPasswordIcon
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endIcon,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val drawableRight = compoundDrawables[2]
            if (drawableRight != null && event.rawX >= (right - drawableRight.bounds.width() - paddingEnd)) {
                togglePasswordVisibility()

                return true
            }
        }
        return false
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        transformationMethod =
            if (isPasswordVisible) null else android.text.method.PasswordTransformationMethod.getInstance()
        setButtonDrawables(endOfTheText = currentEndIcon)
        setSelection(text?.length ?: 0)
    }
}
