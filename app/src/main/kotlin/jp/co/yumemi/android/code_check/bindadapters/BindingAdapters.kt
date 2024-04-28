package jp.co.yumemi.android.code_check.bindadapters

import android.graphics.Color
import android.widget.Button
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
     * A Data Binding adapter for setting the background and icon of a view based on a boolean flag.
     *
     * @param view The View for which the background and icon should be set.
     * @param shouldSelected A boolean flag indicating if the view should be selected.
     */
    @BindingAdapter("itemBackground")
    @JvmStatic
    fun setLanguageButtonBackground(view: Button, shouldSelected: Boolean) {
        when {
            shouldSelected -> {
                view.setBackgroundResource(R.drawable.button_background)
                view.setTextColor(Color.parseColor("#FFFFFF"))
            }

            else -> {
                view.setBackgroundResource(R.drawable.grey_button_background)
                view.setTextColor(Color.parseColor("#000000"))
            }
        }
    }

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
                textView.setTextAppearance(R.style.LanguageSelectedTextStyle)
            }
            else -> {
                textView.setTextAppearance(R.style.LanguageUnSelectedTextStyle)
            }
        }
    }
}