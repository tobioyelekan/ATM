package com.example.android.atm.util

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.atm.ui.viewmodels.ProcessViewModel
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("validate")
fun validate(view: TextInputLayout, text: String?) {
    text?.let {
        view.error = it
    }
}

@BindingAdapter("text")
fun setText(view: TextView, text: String?) {
    text?.let {
        view.text = "₦$text"
    }
}

@BindingAdapter("date")
fun setDate(view: TextView, date: Date) {
    val dateFormat = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
    val dateString = dateFormat.format(date)
    view.text = dateString
}

@BindingAdapter("visible")
fun setVisible(view: View, any: Any?) {
    if (any == null) view.visibility = View.VISIBLE
    else view.visibility = View.GONE
}

@BindingAdapter("visible_empty")
fun setVisibleEmpty(view: View, value: Boolean) {
    if (value) view.visibility = View.VISIBLE
    else view.visibility = View.GONE
}

@BindingAdapter("progress")
fun progressBar(progressBar: ProgressBar, value: Int?) {
    value?.let {
        if (value != 0) {
            if (value == 100)
                progressBar.progress = 100
            else
                progressBar.progress =
                    (value * 100 / (ProcessViewModel.COUNTDOWN_TIME / ProcessViewModel.ONE_SECOND)).toInt()
        }
    }
}