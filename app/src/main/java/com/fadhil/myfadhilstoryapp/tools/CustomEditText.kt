package com.fadhil.myfadhilstoryapp.tools

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.fadhil.myfadhilstoryapp.R
import java.util.regex.Pattern

class CustomEditText : AppCompatEditText  {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        Log.d(TAG, "onDraw: whats $inputType")



    }

    override fun setError(error: CharSequence?, icon: Drawable?) {
        super.setError(error, icon)

    }

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                when(inputType){
                    129 -> {
                        if ((text?.length ?: 0) < 8) {
                            setError(context.getString(R.string.error_password_character),null)
                        }

                    }
                    33 ->{
                        if (!isValidEmailId(text.toString().trim())){
                            error = context.getString(R.string.format_email_worng)
                        }
                    }
                }


            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    private fun isValidEmailId(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }


}