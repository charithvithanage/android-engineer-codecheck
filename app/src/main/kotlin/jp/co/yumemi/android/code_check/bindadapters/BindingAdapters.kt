package jp.co.yumemi.android.code_check.bindadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import jp.co.yumemi.android.code_check.R

/**
 * This object provides custom Data Binding adapters that can be used to bind data and behavior
 * to views in your Android application layout XML files. These adapters are used to simplify
 * the process of working with custom attributes defined in your XML layouts.
 */
object BindingAdapters {

    /**
     * Sets the text appearance of a TextView based on whether it should be selected or not.
     * This function is annotated with @BindingAdapter("selected") to be used in data binding.
     *
     * @param textView The TextView to set the text appearance for.
     * @param shouldSelected A boolean indicating whether the TextView should be selected or not.
     */
    @BindingAdapter("selected")
    @JvmStatic
    fun languageSelectedTextView(textView: TextView, shouldSelected: Boolean) {
        when {
            shouldSelected -> {
                textView.setTextAppearance(R.style.languageSelectedTextStyle)
            }
            else -> {
                textView.setTextAppearance(R.style.languageUnSelectedTextStyle)
            }
        }
    }
}