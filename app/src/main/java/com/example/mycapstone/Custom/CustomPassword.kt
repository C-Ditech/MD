package com.example.mycapstone.Custom

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.mycapstone.R

class CustomPassword : AppCompatEditText, View.OnTouchListener {
    var isPassValid: Boolean = false
    private lateinit var IconPassword: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        IconPassword =
            ContextCompat.getDrawable(context, R.drawable.passwordlogo) as Drawable
        transformationMethod = PasswordTransformationMethod.getInstance()
        setDrawables(IconPassword)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length < 8) error =
                    context.getString(R.string.pass_length)
            }

            override fun afterTextChanged(s: Editable) {

            }

        })
    }

    private fun setDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )

    }

    private fun checkPass() {
        val pass = text?.trim()
        when {
            pass.isNullOrEmpty() -> {
                isPassValid = false
                error = resources.getString(R.string.input_pass)
            }
            else -> {
                isPassValid = true
            }
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!focused) checkPass()
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }
}