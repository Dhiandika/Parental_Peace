package com.dicoding.parentalpeaceapp.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.parentalpeaceapp.R

class CustomEmailEditText: AppCompatEditText {

    val emailErrorMessage = resources.getString(R.string.email_error)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttrs: Int) : super(context, attrs, defStyleAttrs) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = if (!isEmailValid(s as Editable)) {
                            emailErrorMessage
                        }else{
                            null
                        }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing
            }

        })
    }

    fun isEmailValid(email: Editable): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}