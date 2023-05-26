package com.example.wheeloffortune

import android.text.TextUtils
import android.widget.EditText
import androidx.fragment.app.Fragment

fun Fragment.validateForm(inputField: EditText): Boolean {
    var valid = true
    if (TextUtils.isEmpty(inputField.text)) {
        inputField.error = resources.getString(R.string.required)
        valid = false
    } else {
        inputField.error = null
    }
    return valid
}